package com.example.artdemo.viewdemo.view;

import com.example.artdemo.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tj on 2018/1/17.
 */

public class ScrollerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollerLayout rootView = (ScrollerLayout) inflater.inflate(R.layout.fragment_scroller, container, false);
        return rootView;
    }

}
