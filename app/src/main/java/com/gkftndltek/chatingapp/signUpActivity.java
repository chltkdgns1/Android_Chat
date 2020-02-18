package com.gkftndltek.chatingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

public class signUpActivity extends AppCompatActivity {

    private TextInputEditText TextInputEdit_Signup_id,TextInputEdit_Signup_pass,TextInputEdit_Signup_name;
    private LinearLayout LinearLayout_signup_complete,LinearLayout_Back;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LinearLayout_signup_complete = findViewById(R.id.LinearLayout_signup_complete);
        LinearLayout_Back = findViewById((R.id.LinearLayout_Back));
        TextInputEdit_Signup_id= findViewById(R.id.TextInputEdit_Signup_id);
        TextInputEdit_Signup_name= findViewById(R.id.TextInputEdit_Signup_name);
        TextInputEdit_Signup_pass = findViewById(R.id.TextInputEdit_Signup_pass);
        TextInputEdit_Signup_pass .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout_signup_complete.setClickable(true);
        LinearLayout_signup_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = TextInputEdit_Signup_id.getText().toString();
                String pass = TextInputEdit_Signup_pass.getText().toString();
                String name = TextInputEdit_Signup_name.getText().toString();

                if(id.isEmpty() && pass.isEmpty() && name.isEmpty())
                    Toast.makeText(getApplicationContext(),"Input the id or pass or name",Toast.LENGTH_LONG).show();
                else{
                    // 회원가입 디비에 올리기 전에 중볻된 아이디가 있는지 확인
                    // 회원가입 디비에 올림
                    finish(); // 종료
                }
            }
        });

        LinearLayout_Back.setClickable(true);
        LinearLayout_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();// 시스템 종료
            }
        });

    }
}
