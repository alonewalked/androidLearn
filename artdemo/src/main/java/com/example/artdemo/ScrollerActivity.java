package com.example.artdemo;

import com.example.artdemo.viewdemo.view.DragLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScrollerActivity extends AppCompatActivity {

    DragLayout mDragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);

    }

    private void initView() {


    }
}
