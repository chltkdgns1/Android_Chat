package com.gkftndltek.chatingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class roomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<roomData> roomdataset;
    private FirebaseAuth mAuth;
    //private SoundPool sp;
    //private int soundid;
    //private AudioManager am;

    private Date mDate;
    private long now_time;
    private SimpleDateFormat simpleDate;

    private DatabaseReference myRef, logined, myRoom;

    private Userdata userData;
    private roomData roomdata;
    //private DatabaseReference myUser;
    //private Button Button_send;
    //private ImageView ImageView_send;

    //private Userdata userData;

    //private List<String> list;
    //private DatabaseReference isToken;
    private FirebaseDatabase database;

    //private FushMessage fushMessage;

    private String uid;

    private Button Button_logout;
    private Toolbar toolbar; // 툴바

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //룸 메뉴
        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("채팅");
        setSupportActionBar(toolbar);

        TotalLoginActivity ac = (TotalLoginActivity) TotalLoginActivity.TotalLog;
        ac.finish();
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        userData = (Userdata) bun.get("data");
        uid = (String) bun.get("uid");

        database = FirebaseDatabase.getInstance();
        logined = database.getReference("logined");
        myRef = database.getReference("users");

        mAuth = FirebaseAuth.getInstance();

        if (uid != null) {
            logined.child(uid).setValue("1");
            // 접속 완료된 uid
        }

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_room_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mDate = new Date();
        simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        roomdataset = new ArrayList<>();
        mAdapter = new roomAdapter(roomdataset, roomActivity.this, uid);
        recyclerView.setAdapter(mAdapter);

        myRoom = database.getReference("room");

        myRef.child(uid).child("room").child("1").setValue("1");
        getRoom();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.logout:
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if (isLoggedIn) { // 페이스북에 로그인이 된 경우
                    LoginManager.getInstance().logOut();
                    mAuth.signOut();
                } else mAuth.signOut();

                Intent it = new Intent(roomActivity.this, TotalLoginActivity.class);
                startActivity(it);
                finish();
                return true;
            case R.id.menu_make_friend:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 메뉴 시작
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("정말로 종료하시겠습니까?");

        alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alert_ex.setTitle("예제어플 알림!");
        AlertDialog alert = alert_ex.create();
        alert.show();
    }

    public void getRoom() {
        myRef.child(uid).child("room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String rid = dataSnapshot.getKey();

                if (rid != null) {
                    myRoom.child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            roomData roomdata = dataSnapshot.getValue(roomData.class);
                            if (roomdata != null) {
                                System.out.println("민원기병신민원기병신민원기병신민원기병신민원기병신");
                                ((roomAdapter) mAdapter).addRoom(roomdata);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // getChat(); // 맨 처음에 그냥 대회 내용을 가져옴



        /*
        ImageView_send.setClickable(true);
        ImageView_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();
                if(!msg.isEmpty()){
                    now_time = System.currentTimeMillis();
                    mDate.setTime(now_time);
                    String getTime = simpleDate.format(mDate);

                    data = new chatdata();
                    data.setMessage(msg); data.setNickname(userData.getUsername()); data.setTime(getTime);
                    myRef.push().setValue(data);
                    EditText_chat.setText("");



                    isToken.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapData : dataSnapshot.getChildren()){
                                String token = snapData.getKey();
                                fushMessage.pushNotification(data, token);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    isToken.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            for(DataSnapshot snapData : dataSnapshot.getChildren()){
                                list.add(snapData.getKey());
                            }

                            new Thread() {
                                public void run() {
                                    try {
                                        for(int i = 0;i<list.size();i++) {
                                            Log.d("로그로그 ", list.get(i));
                                            if(!userData.getToken().equals(list.get(i)))
                                                fushMessage.pushNotification(data, list.get(i));
                                        }
                                    }
                                    catch(Exception e){

                                    }
                                }
                            }.start();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
         */
}
