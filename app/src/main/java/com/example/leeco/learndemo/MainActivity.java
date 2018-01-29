package com.example.leeco.learndemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.uikit.ScaleProcessView;
import com.example.uikit.banner.RecyclerViewBannerBase;
import com.example.uikit.banner.RecyclerViewBannerNormal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerViewBannerNormal banner, banner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initProcessor();
        this.initBanner();
    }

    private void initProcessor() {
        final ScaleProcessView scaleProgressView = (ScaleProcessView) findViewById(R.id.spv);
        scaleProgressView.setOverText("过头了");
        scaleProgressView.setNearOverText("快过头了");

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                scaleProgressView.setTotalAndCurrentCount(100,i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initBanner() {
        banner = (RecyclerViewBannerNormal) findViewById(R.id.banner);
        banner2 = (RecyclerViewBannerNormal) findViewById(R.id.banner2);
        List<String> list = new ArrayList<>();
        list.add("http://oo6pz0u05.bkt.clouddn.com/17-12-13/69427561.jpg");
        list.add("http://oo6pz0u05.bkt.clouddn.com/17-12-13/23738150.jpg");
        list.add("http://oo6pz0u05.bkt.clouddn.com/17-12-13/30127126.jpg");
        list.add("http://oo6pz0u05.bkt.clouddn.com/17-12-13/36125492.jpg");
        banner.initBannerImageView(list, new RecyclerViewBannerBase.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "clicked:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        banner2.initBannerImageView(list, new RecyclerViewBannerBase.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "clicked:" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
