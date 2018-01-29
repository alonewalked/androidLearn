package com.example.artdemo.binder.view;

import com.example.artdemo.R;
import com.example.artdemo.binder.socket.SocketConstant;
import com.example.artdemo.binder.socket.SocketServerService;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by tj on 2018/1/9.
 */

public class SocketClientFragment extends Fragment {

    private final static String TAG = "SocketClientFragment";

    private Button btSend;
    private EditText etReceive;
    private Socket[] mClientSockets = new Socket[] {
            null,
            null
    };
    private PrintWriter[] mPrintWriter = new PrintWriter[] {
            null,
            null
    };
    private TextView tvMessage;

    private int currentTalk = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_socket_client, container, false);
        this.initView(rootView);
        this.initService();
        return rootView;
    }

    private void initView(View rootView) {
        btSend = rootView.findViewById(R.id.bt_send);
        etReceive = rootView.findViewById(R.id.et_receive);
        tvMessage = rootView.findViewById(R.id.tv_message);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (++currentTalk >= 2) {
                    currentTalk = 0;
                }
                final String msg = etReceive.getText().toString();
                //向服务器发送信息
                if(!TextUtils.isEmpty(msg) && null != mPrintWriter[currentTalk]) {
                    mPrintWriter[currentTalk].println(msg);
                    tvMessage.setText(tvMessage.getText() + "\n" + "客户端【"+currentTalk+"】：" + msg);
                    etReceive.setText("");
                }
            }
        });
    }

    private void initService() {
        Intent service = new Intent(getActivity(), SocketServerService.class);
        getActivity().startService(service);
        new Thread() {
            @Override
            public void run() {
                try {
                    connectSocketServer(0);
                    sleep(100);
                    connectSocketServer(1);

                }
                catch (InterruptedException ex) {
                    Log.e(TAG, "socket___" + ex.getMessage());
                }
            }
        }.start();
    }

    /**
     * 连接socket
     */
    private void connectSocketServer(final int id) {
        Socket socket = null;
        while (socket == null) {
            try {
                //选择和服务器相同的端口8688
                socket = new Socket("localhost", SocketConstant.SOKET_PORT);
                mClientSockets[id] = socket;
                mPrintWriter[id] =
                        new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                Log.e(TAG, "socket___" + e.getMessage());
                SystemClock.sleep(1000);
            }
        }
        // 接收
        try {
            // 接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!getActivity().isFinishing()) {
                final String msg = br.readLine();
                if (msg != null) {
                    getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tvMessage.setText(tvMessage.getText() + "\n" + "客户端【"+(id+1)+"】" + "服务端：" + msg);
                          }
                      }
                    );
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "socket___" + e.getMessage());
            e.printStackTrace();
        }
    }
}
