package com.javaProject.checkcheck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCodeActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_code);

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText editCode = (EditText)findViewById(R.id.GroupCode);
        Button save_btn = findViewById(R.id.codeSave);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editCode.getText().toString())){

                    db.collection("Group").whereEqualTo("code",editCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                //들어가는 코드가 있을경우
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //해당 쿼리를 모두 보여줌, 여기서는 한개
                                    List<String> mem = new ArrayList<>();
                                    mem = (List<String>) document.getData().get("member");
                                    //그룹의 멤버 리스트에 내가 없다면
                                    if(!mem.contains(current_uid)) {
                                        if (mem.size() >= 8) {
                                            //그룹의 멤버 리스트 길이가 8 이상일 경우
                                            Toast.makeText(AddCodeActivity.this, "그룹 인원이 모두 찼습니다", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //아닐경우 멤버 업데이트
                                            db.collection("Group").document(document.getId()).update("member", FieldValue.arrayUnion(current_uid));
                                            db.collection("User").document(current_uid).update("current", FieldValue.arrayUnion(document.getId()));
                                            //todo data 만들어주는 메서드
                                            makeTodo(document.getId(), current_uid);
                                        }
                                    }else {
                                        Toast.makeText(AddCodeActivity.this,"이미 가입한 그룹입니다",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }else{
                                Toast.makeText(AddCodeActivity.this,"잘못된 코드입니다",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    
                }else{
                    Toast.makeText(AddCodeActivity.this,"코드를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void makeTodo(String groupId, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("group", groupId);
        userMap.put("user", userId);
        userMap.put("todo", a);
        userMap.put("finish", b);

        db.collection("Todo").document().set(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                Toast.makeText(AddCodeActivity.this, "그룹에 가입했습니다", Toast.LENGTH_SHORT).show();
                return;
            }else{
                String error = task.getException().getMessage();
                Toast.makeText(AddCodeActivity.this,"데이터 저장 실패 : "+error,Toast.LENGTH_SHORT).show();
            }
        });
    }
}