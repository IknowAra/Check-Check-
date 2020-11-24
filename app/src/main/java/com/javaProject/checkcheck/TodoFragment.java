package com.javaProject.checkcheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TodoFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;
    private String user_id = null;
    private String current_group = null;
    private String current_todo = null;
    public TodoFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        Button open = (Button) view.findViewById(R.id.add_btn);
        open.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());

        ad.setTitle("TODO");       // 제목 설정
        ad.setMessage("해야할 일을 입력해주세요");   // 내용 설정
        final EditText et = new EditText(getActivity());
        ad.setView(et);

        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("User").document(user_id).get().addOnCompleteListener( docu -> {
                    if(docu.isSuccessful() && docu.getResult() != null){
                        //Group 도큐먼트 값 구하기
                        current_group = docu.getResult().getString("group");
                        db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",current_group).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        //Todo 도큐먼트 값 구하기
                                        current_todo = document.getId();
                                    }
                                }
                            }
                        });
                    }
                });

                String value = et.getText().toString();
                db.collection("Todo").document(current_todo).update("todo", FieldValue.arrayUnion(value));

                dialog.dismiss();
            }
        });

        ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }
}