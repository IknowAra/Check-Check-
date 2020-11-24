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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class AddActivity extends AppCompatActivity {
    private FirebaseUser currentUser ;
    private FirebaseAuth auth;
    public String Data = "aAbBcCdDEefFgGhHiIJKLMNOPQRSTUVWXYZjklmnopqrstuvwxyz0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Button saved = findViewById(R.id.buttonSave);

        EditText editTitle = (EditText)findViewById(R.id.GroupName);
        EditText editInfo = (EditText)findViewById(R.id.GroupDescription);

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> a = new ArrayList<>();
                a.add(current_uid);

                if(!TextUtils.isEmpty(editInfo.getText().toString()) && !TextUtils.isEmpty(editTitle.getText().toString())){
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name",editTitle.getText().toString());
                    userMap.put("info",editInfo.getText().toString());
                    userMap.put("member",a);
                    userMap.put("code",ranCode().toString());

                    db.collection("Group").document().set(userMap).addOnCompleteListener( task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(AddActivity.this,"그룹이 추가되었습니다",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddActivity.this,"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(AddActivity.this,"모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected StringBuilder ranCode(){
        Random random = new Random();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append( Data.charAt(random.nextInt(Data.length())));
        }

        db.collection("Group").whereEqualTo("code",sb.toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    ranCode();
                }
            }
        });

        return sb;
    }
}
