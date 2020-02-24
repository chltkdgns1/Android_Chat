package com.gkftndltek.chatingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FushMessage {
    private final String AUTH_KEY_FCM = "AAAATZUFEW4:APA91bHpJpMQTh8BC8YpTedpBk2ghc6c3hQxuN9hSeYFbnCYKjEhvHY2cgFNkHMJCVETbuJ0WnVb0jtK-KpqyNPRG_aGWgoV20_IPWh5HMXZ0pHMW4hjNfi09eiFIMNVH4QgdrzC_nSI";
    private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    private String userDeviceIdKey = "cYBYj-HEFoc:APA91bHYBPaQd4IZ6R4JszYpIvMVaaFsptlDyn3SAOWu43mPlogqDi8TKKNM0avmWO38zzCEWJWm1RNvSpuSDeib7CSEkBWsMOUvsHVmNOc4kIRpVW0O1ZnAqSoPibjujCJ4e7JNEuOM";
    private OutputStreamWriter wr;
    private BufferedReader br;
    private HttpURLConnection conn;

    // private DatabaseReference isToken;
    // private FirebaseDatabase database;

    private JSONObject json;
    private JSONObject info;
    private chatdata data;

    public synchronized void pushNotification(chatdata data, String token ) throws Exception {
        this.data = data;
        URL url = new URL(API_URL_FCM);
        conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);

        //알림 + 데이터 메세지 형태의 전달
        json = new JSONObject();
        info = new JSONObject();

        // database = FirebaseDatabase.getInstance();
        // isToken = database.getReference("Tokens"); // 토큰들의 집합

        info.put("title", "메시지왔어요!");
        info.put("body", data.getNickname() + " : " + data.getMessage()); // Notification body
        info.put("sound", "default");
        json.put("notification", info);
        json.put("to", token);

        //System.out.println("잘 보내지긴하는데 토큰은 정상임? " + token);

        try{
            wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write(json.toString());
            wr.flush();
        }catch(Exception e){
            connFinish();
            throw new Exception("OutputStreamException : " + e);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            //400, 401, 500 등
            connFinish();
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }else{
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
        }
    }

    private void connFinish(){
        if(br != null){
            try {
                br.close();
                br = null;
            } catch (IOException e) {
            }
        }
        if(wr != null){
            try {
                wr.close();
                wr = null;
            } catch (IOException e) {
            }

        }
        if(conn != null){
            conn.disconnect();
            conn = null;
        }
    }
}