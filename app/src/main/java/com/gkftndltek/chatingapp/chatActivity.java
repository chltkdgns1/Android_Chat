package com.gkftndltek.chatingapp;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class chatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private  List<chatdata> chatdataset;

    private SoundPool sp;
    private int soundid;
    private AudioManager am;

    private Date mDate;
    private long now_time;
    private SimpleDateFormat simpleDate;

    private DatabaseReference myRef,myUser;
    private EditText EditText_chat;
    //private Button Button_send;
    private ImageView ImageView_send;
    private Userdata userData;
    private chatdata data;

    private List<String> list;
    private DatabaseReference isToken;
    private FirebaseDatabase database;

    private FushMessage fushMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent it = getIntent();
        Bundle bun = it.getExtras();
        userData = (Userdata) bun.get("data");

        database = FirebaseDatabase.getInstance();
        myUser =database.getReference("users");
        myRef = database.getReference("message");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);
        ImageView_send = findViewById(R.id.ImageView_send);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mDate = new Date();
        simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        chatdataset = new ArrayList<>();
        mAdapter = new chatAdapter(chatdataset,chatActivity.this, userData.getUsername());
        recyclerView.setAdapter(mAdapter);

        sp = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
        soundid = sp.load(this,R.raw.kaotalk,1);

        fushMessage = new FushMessage();

        isToken = database.getReference("Tokens"); // 토큰들의 집합

        getChat(); // 맨 처음에 그냥 대회 내용을 가져옴

        EditText_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                        }
                    }, 200);
                }
                return false;
            }
        });

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


                    /*
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
*/

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
    }

    public void getChat() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chatdata chat = dataSnapshot.getValue(chatdata.class);
                ((chatAdapter) mAdapter).addChat(chat);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
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
