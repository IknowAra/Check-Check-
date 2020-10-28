package com.javaProject.checkcheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser = null;
    FirebaseFirestore database = null;
    private FirebaseAuth auth = null;
    private EditText inputEmail;
    private EditText inputPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            String userEmail = currentUser.getEmail();
        }

        Button login_btn = (Button)findViewById(R.id.btn_login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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