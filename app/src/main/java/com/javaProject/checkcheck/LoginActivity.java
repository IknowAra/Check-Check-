package com.javaProject.checkcheck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser = null;
    private FirebaseAuth auth = null;
    private String user_email = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            user_email = currentUser.getEmail();
        }

        Button loginbtn = (Button)findViewById(R.id.btn_login);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText id_edit = (EditText)findViewById(R.id.et_id);
                EditText pass_edit = (EditText)findViewById(R.id.et_pass);
                String loginEmail = id_edit.getText().toString();
                String loginPass = pass_edit.getText().toString();
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) {
                    auth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                                Intent goHome = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(goHome);
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"error :"+error,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this,"이메일과 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //회원가입
        Button gojoin = (Button)findViewById(R.id.btn_gojoin);
        gojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });



    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        auth = FirebaseAuth.getInstance();
//        inputEmail = (EditText)findViewById(R.id.et_id);
//        inputPass = (EditText)findViewById(R.id.et_pass);
//        Button btn_login = (Button)findViewById(R.id.btn_login);
//
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                String email = inputEmail.getText().toString().trim();
//                String pwd = inputPass.getText().toString().trim();
//
//                auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(intent);
//                        }else{
//
//                        }
//                    }
//                });
//            }
//        });
//        Button imageButton = (Button) findViewById(R.id.btn_gojoin);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//    }

}