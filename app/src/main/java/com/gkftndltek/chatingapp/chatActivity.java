package com.gkftndltek.chatingapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class chatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<chatdata> chatdataset;

    private SoundPool sp;
    private int soundid;
    private AudioManager am;

    private Date mDate;
    private long now_time;
    private SimpleDateFormat simpleDate;

    private DatabaseReference myUser, myRoom;
    private EditText EditText_chat;
    //private Button Button_send;
    private ImageView ImageView_send;
    private roomData roomdata;
    private Userdata userData;
    private chatdata data;

    private List<String> list;
    //private DatabaseReference isToken;
    private FirebaseDatabase database;
    private FushMessage fushMessage;
    private String uid;

    private Handler handlerPushMessage = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){ // 그거 찾아야함 머시냐 현재 들어온 리스트에는
                System.out.println("씨Fire 들어왔냐?1111111111");
                final ThreadCommuicationData data = (ThreadCommuicationData)msg.obj;

                new Thread() {
                    public void run() {
                        try {
                            fushMessage.pushNotification(data.getData(),data.getTokne());
                        } catch (Exception e) {

                        }
                    }
                }.start();
            }
            else if(msg.what == 2){
                String uid = msg.obj.toString();
                System.out.println("씨발 유아이디가 이상한 건가 왜 똑같은게 2개 나옴 시발 " + uid);
                myUser.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userdata usd = dataSnapshot.getValue(Userdata.class);
                        String token = usd.getToken();

                        System.out.println("토큰이 제대로 안들어가는 거 같은데 하나가 빠짐;;; 시발 " + token);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        ThreadCommuicationData cdata = new ThreadCommuicationData();
                        cdata.setData(data); cdata.setTokne(token);
                        msg.obj = cdata;
                        handlerPushMessage.sendMessage(msg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        roomdata = (roomData) bun.get("data");
        uid = (String) bun.get("uid");

        database = FirebaseDatabase.getInstance();

        myUser = database.getReference("users");
        myRoom = database.getReference("room");

        init(); // 초기화 해주는 함수
        EditTextTouch();  // 에딧텍스가 눌렸을 경우 (스크롤이 맨 아래로 내려감)
        myRoom.child(roomdata.getRid()).child("uids").child(uid).setValue("1");
        // 룸에 유저 유아디를 넣어줌 그 이유는 방에 입장을 뜻함
        // 이 유아이디로 푸시 알림을 보냄

        // roomdata 에 들어있는 uid 를 가지고 users에 접근해서 닉네임 토큰을 가져옴
        myUser.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(Userdata.class); // 가저온 유저 데이터
                if (userData != null) { // 유저데이터가 null 이 아닌 경우
                    getChat(); // 맨 처음에 그냥 대회 내용을 가져옴
                    sendMessage();
                    mAdapter = new chatAdapter(chatdataset, chatActivity.this, userData.getUsername());
                    recyclerView.setAdapter(mAdapter);
                } else { // 가져온 유저 데이터가 null 인 경우 있을 수 없는 경우다.
                    // 일단 예외처리 생각해봄
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMessage() {
        ImageView_send.setClickable(true);
        ImageView_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();
                if (!msg.isEmpty()) {
                    now_time = System.currentTimeMillis();
                    mDate.setTime(now_time);
                    String getTime = simpleDate.format(mDate);

                    data = new chatdata();
                    data.setMessage(msg);
                    data.setNickname(userData.getUsername());
                    data.setTime(getTime);

                    myRoom.child(roomdata.getRid()).child("message").push().setValue(data);
                    EditText_chat.setText("");

                    myRoom.child(roomdata.getRid()).child("uids").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapData : dataSnapshot.getChildren()) {
                                String key = snapData.getKey();
                                Message msg = Message.obtain();
                                msg.what = 2; msg.obj = key;
                                System.out.println("키키키키키키키 : " + key);
                                handlerPushMessage.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    private void EditTextTouch() {
        EditText_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 200);
                }
                return false;
            }
        });
    }

    private void init() {
        fushMessage = new FushMessage();
        chatdataset = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        EditText_chat = findViewById(R.id.EditText_chat);
        ImageView_send = findViewById(R.id.ImageView_send);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mDate = new Date();
        simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    private void getChat() {
        myRoom.child(roomdata.getRid()).child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chatdata chat = dataSnapshot.getValue(chatdata.class);
                ((chatAdapter) mAdapter).addChat(chat);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }, 200);
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
}
