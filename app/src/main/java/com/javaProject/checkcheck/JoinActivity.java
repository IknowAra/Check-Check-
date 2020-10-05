package com.javaProject.checkcheck;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.IgnoreExtraProperties;

public class JoinActivity extends AppCompatActivity {
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


    }
}

