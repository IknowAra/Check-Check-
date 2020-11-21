package com.javaProject.checkcheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();

        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());

        ad.setTitle("TODO");       // 제목 설정
        ad.setMessage("해야할 일을 입력해주세요");   // 내용 설정
        final EditText et = new EditText(getActivity());
        ad.setView(et);

        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = et.getText().toString();
//                db.collection("Todo").ad
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