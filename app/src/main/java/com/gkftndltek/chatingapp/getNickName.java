package com.gkftndltek.chatingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class getNickName extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private LinearLayout LinearLayout_signup_complete;
    private EditText TextInputEdit_getNickname_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getnickname);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        LinearLayout_signup_complete = findViewById(R.id.LinearLayout_signup_complete);
        TextInputEdit_getNickname_id = findViewById(R.id.TextInputEdit_getNickname_id);
        LinearLayout_signup_complete.setClickable(true);

        Intent intent = getIntent();
        Bundle bun = intent.getExtras();
        final String uid = bun.getString("uid");
        final String token = bun.getString("token");

        LinearLayout_signup_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Userdata data = new Userdata();

                String nick = TextInputEdit_getNickname_id.getText().toString();
                if(!nick.isEmpty()) {
                    data.setUsername(TextInputEdit_getNickname_id.getText().toString());
                    data.setToken(token);
                    myRef.child(uid).setValue(data);

                    Intent it = new Intent(getNickName.this,roomActivity.class);
                    it.putExtra("data",data);
                    it.putExtra("uid",uid);
                    startActivity(it);
                    finish();
                }
            }
        });
    }
}
