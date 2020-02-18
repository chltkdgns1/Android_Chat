package com.gkftndltek.chatingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

public class TotalLoginActivity extends AppCompatActivity {


    public static Activity TotalLog;

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;

    // private TextInputEditText TextInputEdit_id,TextInputEdit_pass; // 아이디 비밀번호
    //private TextView TextView_signup; // 회원가입
    //private LinearLayout LinearLayout_Button1; // 로그인 장식임(사용 설명서)

    private LoginButton loginButton; // 페이스북에서 제공하는 로그인 버트

    private DatabaseReference myRef, getUsers, isToken;
    // 여기서 getUsers 는 주요 로그인 외의 uid 를 저장하는 수를 가져오기 위해 사용됨
    // 세번째 isToken 은 토큰이 존재하는지 확인하고 존재하지 않는다면 추가하는 기능

    private FirebaseDatabase database;

    private UserCount userCount;
    private String tokens;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "로그로그로그로그로그";
    private FirebaseUser user;
    private GoogleSignInOptions gso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        TotalLog = TotalLoginActivity.this;

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mCallbackManager = CallbackManager.Factory.create();

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("users");
        isToken = database.getReference("Tokens"); // 토큰들의 집합

        tokens = FirebaseInstanceId.getInstance().getToken();

        //inputToken();

        // 현재 기기의 토큰을 가져옴

        // 토큰은 회원가입 뿐만아니라 로그인 이나 기타 상태에서도
        // 새로 받아질 수 있다고 가정하고, tokenCollecte 에 갱신하도록 한다.
        // 단, 이 부분도 룸이 생길 경우에 삭제해야한다.

        // 토큰이 존재하는지 확인하고 없다면 추가한다.

        if (user != null) { // 로그인이 되있는 상태라면 바로 메시지 창으로 넘아감
            final String uid = user.getUid(); // 접속했었던 유아이를 가져옴
            if (uid != null) {
                myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userdata data = dataSnapshot.getValue(Userdata.class);
                        if (data != null) {
                            data.setToken(tokens); // 새로 받은 토큰을 갱신해주고
                            Intent intent = new Intent(TotalLoginActivity.this, chatActivity.class);
                            intent.putExtra("data", data);
                            myRef.child(uid).setValue(data);  // 갱신한 토큰을 디비에 올려줌
                            startActivity(intent);
                            finish();
                        } else {
                            LayoutStart();
                            Intent intent = new Intent(TotalLoginActivity.this, getNickName.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("token", tokens);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else LayoutStart();
    }

    private void LayoutStart(){
        setContentView(R.layout.activity_total_login);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // 구글 로그인 준비

        goolgleStart();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("설마여기임?", "1");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("설마여기임?", "2");
            }
        });
    }

    private void goolgleStart() {
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        findViewById(R.id.signOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        findViewById(R.id.disconnectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Toast.makeText(getApplicationContext(), "Google sign in success", Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Google sign in failed", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Toast.makeText(getApplicationContext(), "signInWithCredential:success", Toast.LENGTH_LONG).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            final String uid = user.getUid();

                            myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Userdata data = dataSnapshot.getValue(Userdata.class);
                                    if (data == null) { // 새로운 uid 계정을 만들어야함
                                        Intent intent = new Intent(TotalLoginActivity.this, getNickName.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("token", tokens);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(TotalLoginActivity.this, chatActivity.class);
                                        intent.putExtra("data", data);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Toast.makeText(getApplicationContext(), "signInWithCredential:failure", Toast.LENGTH_LONG).show();

                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.signInButton).setVisibility(View.GONE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Toast.makeText(getApplicationContext(),"로그인성공",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            final String uid = user.getUid();

                            //myRef.child(uid).setValue("1");
                            //Toast.makeText(getApplicationContext(),myRef.child("users/" + uid).getKey(),Toast.LENGTH_LONG).show();

                            myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Userdata data = dataSnapshot.getValue(Userdata.class);
                                    if (data == null) { // 새로운 uid 계정을 만들어야함
                                        Intent intent = new Intent(TotalLoginActivity.this, getNickName.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("token", tokens);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(TotalLoginActivity.this, chatActivity.class);
                                        intent.putExtra("data", data);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {

                        }
                    }
                });
    }
}
