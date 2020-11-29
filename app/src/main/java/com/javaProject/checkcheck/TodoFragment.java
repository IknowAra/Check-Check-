package com.javaProject.checkcheck;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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


public class TodoFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;
    private String user_id = null;
    private String current_group = null;
    public TodoFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        ImageView open = (ImageView) view.findViewById(R.id.add_btn);
        ImageView back = (ImageView) view.findViewById(R.id.back1);
        open.setOnClickListener(this);
        back.setOnClickListener(this);

        ArrayList<TextView> remain = new ArrayList<>();
        ArrayList<TextView> fini = new ArrayList<>();
        LinearLayout lin1 = view.findViewById(R.id.ll1);
        LinearLayout lin2 = view.findViewById(R.id.ll2);

        db.collection("User").document(user_id).get().addOnCompleteListener( docu -> {
            if (docu.isSuccessful() && docu.getResult() != null) {
                //Group 도큐먼트 값 구하기
                current_group = docu.getResult().getString("group");
                db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",current_group).get().addOnCompleteListener( task2 -> {
                    for (QueryDocumentSnapshot document : task2.getResult()) {
                        List<String> todo = (List<String>) document.get("todo");
                        List<String> fin = (List<String>) document.get("finish");
                        for(int i = 0; i<todo.size(); i++){
                            TextView tv = new TextView(getContext());
                            tv.setText(todo.get(i));
                            tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                            tv.setTextSize(30);
                            tv.setBackgroundColor(R.color.colorPrimary);
                            lin1.addView(tv);
                            remain.add(tv);
                        }
                        for(int i = 0; i<fin.size(); i++){
                            TextView tv = new TextView(getContext());
                            tv.setText(fin.get(i));
                            tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                            tv.setTextSize(30);
                            tv.setBackgroundColor(R.color.colorPrimary);
                            lin2.addView(tv);
                            fini.add(tv);
//                            tv.setOnClickListener();
                        }
                    }
                });
            }
        });
        remain.get(0).setText("엥 이게 뭐야");
        Log.d("",remain.toString());

        return view;
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.add_btn){
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
                                            String value = et.getText().toString();
                                            db.collection("Todo").document(document.getId()).update("todo", FieldValue.arrayUnion(value));
                                        }
                                    }
                                }
                            });
                        }
                    });

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
        }else if(v.getId() == R.id.back1){
            getActivity().onBackPressed();
        }



    }
}