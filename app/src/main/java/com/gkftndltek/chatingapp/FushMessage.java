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
    public void pushNotification(chatdata data, String token ) throws Exception {
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
        json.put("to", token );

        System.out.println("잘 보내지긴하는데 토큰은 정상임? " + token);

        try{
            wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write(json.toString());
           // System.out.println("잘 보내짐?");
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
            //200 + error 는 재전송 등등의 로직
        }

        /*
        json.put("to", userDeviceIdKey);

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
            //200 + error 는 재전송 등등의 로직
        }
        */

        //}

        //  conn.setRequestProperty("Authorization", "bearer " + AUTH_KEY_FCM);
        //conn.connect();
        // 이렇게 보내면 주제를 ALL로 지정해놓은 모든 사람들한테 알림을 날려준다.
        //String input = "{\"notification\" : {\"title\" : \"여기다 제목 넣기 \", \"body\" : \"여기다 내용 넣기\"}, \"to\":\"/topics/ALL\"}";

        // 이걸로 보내면 특정 토큰을 가지고있는 어플에만 알림을 날려준다  위에 둘중에 한개 골라서 날려주자

        //String input = "{\"notification\" : {\"title\" : \"" + title + " \", \"body\" : \"" + body + "\"}, \"to\":\"" + userDeviceIdKey  +"\"}";

/*
                String input =    "{\n" + "\"message\":{\n" + " \"notification\": {\n" + " \"title\": \"FCM Message\",\n"
                + " \"body\": \"" + body + "\",\n"
                + "  },\n" + " \"token\": \"" + userDeviceIdKey + "\"\n" + "  }\n" + "}\n";

 */

        // Log.d("dsadadadada",input);

        /*
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // print result
        System.out.println(response.toString());

                /*
        if(responseCode==200) { // 정상 호출
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes("UTF-8"));
            os.flush();
            os.close();
        } else {  // 에러 발생

        }

         */
        // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자

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