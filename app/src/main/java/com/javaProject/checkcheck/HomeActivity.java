package com.javaProject.checkcheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private String user_id = null;
    private View drawerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }


    @Override
    protected void onResume() {
        super.onResume();


        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);


        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();

        View[] groups = {findViewById(R.id.group1),findViewById(R.id.group2),findViewById(R.id.group3),findViewById(R.id.group4),findViewById(R.id.group5)};
        Button[] buttons = {findViewById(R.id.btn_group1),findViewById(R.id.btn_group2),findViewById(R.id.btn_group3),findViewById(R.id.btn_group4),findViewById(R.id.btn_group5)};
        TextView[] persents = {findViewById(R.id.tx_persent1),findViewById(R.id.tx_persent2),findViewById(R.id.tx_persent3),findViewById(R.id.tx_persent4),findViewById(R.id.tx_persent5)};

        for(int i = 0; i<groups.length; i++){
            groups[i].setVisibility(View.INVISIBLE);
        }


        db.collection("User").document(user_id).get().addOnCompleteListener( task -> {
            if(task.isSuccessful() && task.getResult() != null){
                List<String> a = new ArrayList<>();
                a = (List<String>) task.getResult().get("current");
                for(int i = 0; i<a.size(); i++){
                    groups[i].setVisibility(View.VISIBLE);
                    int finalI = i;

                    db.collection("Group").document(a.get(i)).get().addOnCompleteListener(task2 -> {
                        if(task2.isSuccessful() && task2.getResult() != null){
                            String groupName = task2.getResult().getString("name");
                            buttons[finalI].setText(groupName);

                            db.collection("Todo").whereEqualTo("user",user_id).whereEqualTo("group",task2.getResult().getId()).get().addOnCompleteListener( task3 ->{
                                for (QueryDocumentSnapshot document : task3.getResult()) {
                                    List<String> todo = (List<String>) document.get("todo");
                                    List<String> fin = (List<String>) document.get("finish");
                                    if(fin.size() != 0){
                                        double persen = fin.size()/ (double)(todo.size()+fin.size())*100;
                                        persents[finalI].setText((int)persen+"%");
                                    }else{
                                        persents[finalI].setText("0%");
                                    }
                                }
                            });
                        }
                    });

                }
                String name = task.getResult().getString("name");
                TextView namearea = (TextView)findViewById(R.id.realName);
                namearea.setText(name);
                ImageView userImgArea = findViewById(R.id.imageView);
                String userImg = task.getResult().getString("userImg");
                switch (userImg){
                    case "0":
                        userImgArea.setImageResource(R.drawable.man1);
                        break;
                    case "1":
                        userImgArea.setImageResource(R.drawable.man2);
                        break;
                    case "2":
                        userImgArea.setImageResource(R.drawable.man3);
                        break;
                    case "3":
                        userImgArea.setImageResource(R.drawable.man4);
                        break;
                    case "4":
                        userImgArea.setImageResource(R.drawable.man5);
                        break;
                    case "5":
                        userImgArea.setImageResource(R.drawable.man6);
                        break;
                    case "6":
                        userImgArea.setImageResource(R.drawable.woman1);
                        break;
                    case "7":
                        userImgArea.setImageResource(R.drawable.woman2);
                        break;
                    case "8":
                        userImgArea.setImageResource(R.drawable.woman3);
                        break;
                    case "9":
                        userImgArea.setImageResource(R.drawable.woman4);
                        break;
                    case "10":
                        userImgArea.setImageResource(R.drawable.woman5);
                        break;
                    case "11":
                        userImgArea.setImageResource(R.drawable.woman6);
                        break;

                }
            }
        });

        for(int i = 0; i<buttons.length; i++){
            int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("User").document(user_id).get().addOnCompleteListener( task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<String> a = new ArrayList<>();
                            a = (List<String>) task.getResult().get("current");
                            db.collection("User").document(user_id).update("group", a.get(finalI));
                            startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                        }
                    });
                }
            });
        }

        ImageView btn_open = (ImageView)findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인액티비티에서 메뉴열기버튼을 누르면 메뉴가 열림
                drawerLayout.openDrawer(drawerView);
            }
        });
        //기존 그룹 가입
        ImageView joinGroup = (ImageView)findViewById(R.id.imageView7);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("User").document(user_id).get().addOnCompleteListener( task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        List<String> a = new ArrayList<>();
                        a = (List<String>) task.getResult().get("current");
                        if(a.size()>=5) {
                            Toast.makeText(HomeActivity.this, "그룹은 최대 5개까지 추가 가능합니다", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(getApplicationContext(), AddCodeActivity.class));
                        }
                    }
                });
            }
        });
        //그룹 생성
        ImageView createGroup = (ImageView)findViewById(R.id.imageView6);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("User").document(user_id).get().addOnCompleteListener( task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> a = new ArrayList<>();
                        a = (List<String>) task.getResult().get("current");
                        if (a.size() >= 5) {
                            Toast.makeText(HomeActivity.this, "그룹은 최대 5개까지 추가 가능합니다", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(getApplicationContext(), AddActivity.class));
                        }
                    }
                });
            }
        });




        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        TextView logout = (TextView)findViewById(R.id.nav_logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                auth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        TextView toCal = findViewById(R.id.nav_cal);
        toCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CalenderActivity.class));
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}