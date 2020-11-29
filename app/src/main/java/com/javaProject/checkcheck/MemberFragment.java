package com.javaProject.checkcheck;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class MemberFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;
    private String user_id = null;
    private String current_group = null;

    public MemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        ImageView remov = (ImageView) view.findViewById(R.id.remove);
        ImageView back = (ImageView) view.findViewById(R.id.back2);

        remov.setOnClickListener(this);
        back.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView groupNameSpace = view.findViewById(R.id.GroupNameArea);
        TextView groupInfoSpace = view.findViewById(R.id.GroupInfo);
        TextView groupCodeSpace = view.findViewById(R.id.GroupCodeArea);

        View[] groups = {view.findViewById(R.id.user1), view.findViewById(R.id.user2), view.findViewById(R.id.user3), view.findViewById(R.id.user4), view.findViewById(R.id.user5), view.findViewById(R.id.user6), view.findViewById(R.id.user7), view.findViewById(R.id.user8)};
        TextView[] names = {view.findViewById(R.id.user_name1), view.findViewById(R.id.user_name2), view.findViewById(R.id.user_name3), view.findViewById(R.id.user_name4), view.findViewById(R.id.user_name5), view.findViewById(R.id.user_name6), view.findViewById(R.id.user_name7), view.findViewById(R.id.user_name8)};
        TextView[] pers = {view.findViewById(R.id.user_per1), view.findViewById(R.id.user_per2), view.findViewById(R.id.user_per3), view.findViewById(R.id.user_per4), view.findViewById(R.id.user_per5), view.findViewById(R.id.user_per6), view.findViewById(R.id.user_per7), view.findViewById(R.id.user_per8)};
        for(int i = 0; i<groups.length; i++){
            groups[i].setVisibility(View.INVISIBLE);
            names[i].setVisibility(View.INVISIBLE);
            pers[i].setVisibility(View.INVISIBLE);
        }

        db.collection("User").document(user_id).get().addOnCompleteListener( docu -> {
            if (docu.isSuccessful() && docu.getResult() != null) {
                //Group 도큐먼트 값 구하기
                current_group = docu.getResult().getString("group");

                db.collection("Group").document(current_group).get().addOnCompleteListener( task ->{
                    if (task.isSuccessful() && task.getResult() != null) {
                        groupNameSpace.setText(task.getResult().getString("name"));
                        groupInfoSpace.setText(task.getResult().getString("info"));
                        groupCodeSpace.setText("그룹 코드 : "+task.getResult().getString("code"));

                        List<String> a = (List<String>) task.getResult().get("member");
                        for(int i = 0; i<a.size(); i++){
                            int finalI = i;
                            groups[i].setVisibility(View.VISIBLE);
                            names[i].setVisibility(View.VISIBLE);
                            pers[i].setVisibility(View.VISIBLE);
                            db.collection("User").document(a.get(i)).get().addOnCompleteListener(userdata ->{
                                //이름
                                names[finalI].setText(userdata.getResult().getString("name"));
                                String userImg = userdata.getResult().getString("userImg");
                                //이미지
                                switch (userImg){
                                    case "0":
                                        groups[finalI].setBackgroundResource(R.drawable.man1);
                                        break;
                                    case "1":
                                        groups[finalI].setBackgroundResource(R.drawable.man2);
                                        break;
                                    case "2":
                                        groups[finalI].setBackgroundResource(R.drawable.man3);
                                        break;
                                    case "3":
                                        groups[finalI].setBackgroundResource(R.drawable.man4);
                                        break;
                                    case "4":
                                        groups[finalI].setBackgroundResource(R.drawable.man5);
                                        break;
                                    case "5":
                                        groups[finalI].setBackgroundResource(R.drawable.man6);
                                        break;
                                    case "6":
                                        groups[finalI].setBackgroundResource(R.drawable.woman1);
                                        break;
                                    case "7":
                                        groups[finalI].setBackgroundResource(R.drawable.woman2);
                                        break;
                                    case "8":
                                        groups[finalI].setBackgroundResource(R.drawable.woman3);
                                        break;
                                    case "9":
                                        groups[finalI].setBackgroundResource(R.drawable.woman4);
                                        break;
                                    case "10":
                                        groups[finalI].setBackgroundResource(R.drawable.woman5);
                                        break;
                                    case "11":
                                        groups[finalI].setBackgroundResource(R.drawable.woman6);
                                        break;
                                }
                                //퍼센트
                                db.collection("Todo").whereEqualTo("user",a.get(finalI)).whereEqualTo("group",current_group).get().addOnCompleteListener( task3 ->{
                                    for (QueryDocumentSnapshot document : task3.getResult()) {
                                        List<String> todo = (List<String>) document.get("todo");
                                        List<String> fin = (List<String>) document.get("finish");
                                        if(fin.size() != 0){
                                            double persen = fin.size()/ (double)(todo.size()+fin.size())*100;
                                            pers[finalI].setText((int)persen+"%");
                                        }else{
                                            pers[finalI].setText("0%");
                                        }
                                    }
                                });
                            });

                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.remove){
            //탈퇴 기능

        }else if(v.getId() == R.id.back2){
            getActivity().onBackPressed();
        }
    }
}