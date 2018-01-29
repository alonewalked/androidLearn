package com.example.artdemo.binder.view;

import com.example.artdemo.R;
import com.example.artdemo.binder.messenger.MessengerClient;
import com.example.artdemo.binder.messenger.MessengerService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by tj on 2018/1/5.
 */

public class MessengerFragment extends Fragment {

    Button btnSendMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messenger, container,
                false);
        this.initView(rootView);
        return rootView;

    }

    private void initView(View rootView) {
        btnSendMsg = (Button) rootView.findViewById(R.id.btn_send_messenger);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessengerClient.getInstance().sendMessage(new Date().getTime()+"");
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定服务
        Intent intent = new Intent(getActivity(), MessengerService.class);
        getActivity().bindService(intent, MessengerClient.getInstance().getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(MessengerClient.getInstance().getServiceConnection());
    }
}
