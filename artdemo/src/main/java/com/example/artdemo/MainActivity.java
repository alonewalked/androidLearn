package com.example.artdemo;

import com.example.artdemo.binder.view.BookFragment;
import com.example.artdemo.binder.view.MessengerFragment;
import com.example.artdemo.binder.view.SocketClientFragment;
import com.example.artdemo.viewdemo.view.ScrollerFragment;
import com.example.artdemo.viewdemo.view.ZoomFragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Fragment[] pages = new Fragment[]{
            new MessengerFragment(),
            new BookFragment(),
            new SocketClientFragment(),
            new ZoomFragment(),
            new ScrollerFragment()
    };

    private int currentPage = 0;

    private FragmentManager fragmentManager;
    private Button mButton;
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            initView();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        int commit = fragmentManager.beginTransaction().add(R.id.container, pages[currentPage]).commit();
        mButton = (Button) findViewById(R.id.btnNext);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (++currentPage >= pages.length) {
                    currentPage = 0;
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, pages[currentPage])
                        .addToBackStack(null)
                        .commit();
            }
        });

        mBtn2 = (Button) findViewById(R.id.btn_scroll);
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScrollerActivity.class);
                startActivity(intent);
            }
        });
    }
}
