package com.javaProject.checkcheck;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {
    PieChart chart1;
    BarChart chart2;

    private FirebaseAuth auth;
    private String user_id = null;
    private String current_group = null;

    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String[] colors = new String[]{ "#70A5DB", "#DB4E44","#324F80", "#448DDB", "#55718F","#8546DB","#ADDB8C","#DB543B"};
        //색 지정

        chart1 = (PieChart) view.findViewById(R.id.tab1_chart_1);
        chart2 = (BarChart)view.findViewById(R.id.tab1_chart_2);
        chart1.clearChart();
        chart2.clearChart();



        db.collection("User").document(user_id).get().addOnCompleteListener( docu -> {
            if (docu.isSuccessful() && docu.getResult() != null) {
                //Group 도큐먼트 값 구하기
                current_group = docu.getResult().getString("group");
                String userName = docu.getResult().getString("name");

                db.collection("Group").document(current_group).get().addOnCompleteListener(task -> { //
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> a = (List<String>) task.getResult().get("member");

                        int sum = 0;
                        for(int i = 0; i<a.size(); i++){
                            int finalI = i;
                            db.collection("User").document(a.get(i)).get().addOnCompleteListener(userdata ->{
                                String name = userdata.getResult().getString("name");
                                db.collection("Todo").whereEqualTo("user",a.get(finalI)).whereEqualTo("group",current_group).get().addOnCompleteListener(task3 ->{
                                    for (QueryDocumentSnapshot document : task3.getResult()) {
                                        List<String> todo = (List<String>) document.get("todo");
                                        List<String> fin = (List<String>) document.get("finish");
                                        if(fin.size() != 0){
                                            double persen = fin.size()/ (double)(todo.size()+fin.size())*100;
                                            chart1.addPieSlice(new PieModel(name, (int)persen, Color.parseColor(colors[finalI])));
                                        }else{
                                            chart1.addPieSlice(new PieModel(name, 0, Color.parseColor(colors[finalI])));
                                        }

                                    }
                                });

                            });
                        } // 원형 그래프 //

                        db.collection("Todo").whereEqualTo("group",current_group).get().addOnCompleteListener(task2 ->{//
                            int finSum = 0;
                            int toSum = 0;
                            for (QueryDocumentSnapshot docus : task2.getResult()) {
                                List<String> l1 = (List<String>) docus.get("todo");
                                List<String> l2 = (List<String>) docus.get("finish");
                                toSum += l1.size();
                                finSum += l2.size();

                                if(docus.get("user").equals(user_id)){
                                    if(l2.size() != 0){
                                        double persen = l2.size()/ (double)(l1.size()+l2.size())*100;
                                        chart2.addBar(new BarModel(userName, (int)persen, 0xFFFA5901));
                                    }else{
                                        chart2.addBar(new BarModel(userName, 0, 0xFFFA5901));
                                    }
                                }
                            }
                            if(finSum != 0){
                                double groupPer = finSum/ (double)(finSum+toSum)*100;
                                chart2.addBar(new BarModel("그룹 평균", (int)groupPer, 0xFFFA5901));
                            }else{
                                chart2.addBar(new BarModel("그룹 평균",0, 0xFFFA5901));
                            }
                        });
                    }
                });
            }
        });
        chart1.startAnimation();
        chart2.startAnimation();
        return view;
    } // 막대그래프//

    // 막대 차트 설정
//    private void setBarChart(List<itemModel> itemList2) {
//
//        chart2.clearChart();
//
//        chart2.addBar(new BarModel("12", 10f, 0xFF56B7F1));
//        chart2.addBar(new BarModel("13", 10f, 0xFF56B7F1));
//        chart2.addBar(new BarModel("14", 10f, 0xFF56B7F1));
//        chart2.addBar(new BarModel("15", 20f, 0xFF56B7F1));
//        chart2.addBar(new BarModel("16", 10f, 0xFF56B7F1));
//        chart2.addBar(new BarModel("17", 10f, 0xFF56B7F1));
//
//        chart2.startAnimation();
//
//    }
}