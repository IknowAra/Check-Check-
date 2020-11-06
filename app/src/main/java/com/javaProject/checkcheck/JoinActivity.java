package com.javaProject.checkcheck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Multimap;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String user_id = null;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;

    private String userName;
    private String userEmail;
    private String userPass;
    private String confirmPass;
    private String userGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        auth = FirebaseAuth.getInstance();
        editTextEmail = (EditText)findViewById(R.id.user_id);
        editTextPassword = (EditText)findViewById(R.id.user_pass);
        editTextName = (EditText)findViewById(R.id.user_name);
        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);

        Button btn_join = (Button)findViewById(R.id.btn_register);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = editTextEmail.getText().toString();
                userPass = editTextPassword.getText().toString();
                userName = editTextName.getText().toString();
                confirmPass = ((EditText)findViewById(R.id.confirm_pass)).getText().toString();
                int checkbtn = rg.getCheckedRadioButtonId();

                if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass) && !TextUtils.isEmpty(confirmPass) && !TextUtils.isEmpty(userName) && checkbtn!=-1){
                    RadioButton rb = (RadioButton) findViewById(checkbtn);
                    userGender = rb.getText().toString();

                    if (userPass.equals(confirmPass)) {
                        auth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(JoinActivity.this,"잠시만 기다려주세요",Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = auth.getCurrentUser();
                                    user_id = user.getUid();
                                    adduser();
                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(JoinActivity.this,"error :"+error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(JoinActivity.this,"비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(JoinActivity.this,"모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void adduser(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("email",userEmail);
        userMap.put("name",userName);
        userMap.put("gender",userGender);
        db.collection("User").document(user_id.toString()).set(userMap).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(JoinActivity.this,"유저정보 등록",Toast.LENGTH_SHORT).show();
                    Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goLogin);
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(JoinActivity.this,"error :"+error,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}