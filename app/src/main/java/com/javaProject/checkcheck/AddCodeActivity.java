package com.javaProject.checkcheck;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
                                    if(mem.size() >= 8){
                                        //그룹의 멤버 리스트 길이가 8 이상일 경우
                                        Toast.makeText(AddCodeActivity.this,"그룹 인원이 모두 찼습니다",Toast.LENGTH_SHORT).show();
                                    }else{
                                        //아닐경우 멤버 업데이트
                                        db.collection("Group").document(document.getId()).update("member", FieldValue.arrayUnion(current_uid));
                                        db.collection("User").document(current_uid).update("current", FieldValue.arrayUnion(document.getId()));
                                        finish();
                                        Toast.makeText(AddCodeActivity.this,"그룹에 가입했습니다",Toast.LENGTH_SHORT).show();
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
}