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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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


public class TodoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private FirebaseAuth auth;
    private String user_id = null;
    private String current_group = null;

    private EditText itemET;
    private Button btn;
    private ImageView clear;
    private ListView itemList;

    private ArrayList <String> items;
    private ArrayAdapter<String> adapter;
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

        itemET = view.findViewById(R.id.item_edit_text);
        btn = view.findViewById(R.id.add_btn);
        itemList = view.findViewById(R.id.item_list);
        clear = view.findViewById(R.id.clear_btn);

        db.collection("User").document(user_id).get().addOnCompleteListener( docu -> {
            if (docu.isSuccessful() && docu.getResult() != null) {
                current_group = docu.getResult().getString("group");
                db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",current_group).get().addOnCompleteListener( task2 -> {
                    for (QueryDocumentSnapshot document : task2.getResult()) {
                        items = (ArrayList<String>) document.get("todo");
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
                        itemList.setAdapter(adapter);

                        clear.setOnClickListener(this);
                        btn.setOnClickListener(this);
                        itemList.setOnItemClickListener(this);
                    }
                });
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(v.getId() == R.id.add_btn){
            String itemEntered = itemET.getText().toString();
            if(itemEntered.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "할일을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }else{
                adapter.add(itemEntered);
                itemET.setText("");

                db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",current_group).get().addOnCompleteListener( tasks ->{
                    for(QueryDocumentSnapshot document : tasks.getResult()){
                        db.collection("Todo").document(document.getId()).update("todo", FieldValue.arrayUnion(itemEntered));
                    }
                });
            }
        }else if(v.getId() == R.id.clear_btn){
            items.clear();
            adapter.notifyDataSetChanged();

            db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",current_group).get().addOnCompleteListener( tasks ->{
                for(QueryDocumentSnapshot document : tasks.getResult()){
                    db.collection("Todo").document(document.getId()).update("todo", FieldValue.arrayRemove());
                }
            });

        }else if(v.getId() == R.id.back1){
            getActivity().onBackPressed();

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Todo");
        builder.setMessage("완료하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",current_group).get().addOnCompleteListener( tasks ->{
                    for(QueryDocumentSnapshot document : tasks.getResult()){
                        db.collection("Todo").document(document.getId()).update("todo", FieldValue.arrayRemove(items.get(position)));
                        db.collection("Todo").document(document.getId()).update("finish", FieldValue.arrayUnion(items.get(position)));
                        Toast.makeText(getActivity(), " *완료* "+items.get(position) , Toast.LENGTH_SHORT).show();
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}