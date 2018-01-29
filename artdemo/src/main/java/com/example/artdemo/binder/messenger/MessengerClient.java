package com.example.artdemo.binder.messenger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by tj on 2018/1/4.
 */

public class MessengerClient {

    // 单例
    private static volatile MessengerClient instance = null;
    private static final String TAG = "MessengerClient";

    private MessengerClient(){
    }

    public static MessengerClient getInstance() {
        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (MessengerClient.class) {
                if (instance == null) {
                    instance = new MessengerClient();
                }
            }
        }
        return instance;
    }

    private Messenger messenger;


    // bind service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 绑定后使用返回的inder对象创建Messenger；
            messenger = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * get ServiceConnection
     * @return
     */
    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }

    /**
     * sendMessage
     */
    public void sendMessage(String text) {
        //创建完成Messenger后就可以通过Messenger 来发送 Message 了
        Message message = Message.obtain();
        message.what = MessageConstant.MSG_SEV;
        Bundle bundle = new Bundle();
        bundle.putString("msg", "this is message from client: " + text + "...");
        message.setData(bundle);
        // 设置replyTo参数
        message.replyTo = replyMessenger;
        try {
            //通过信使发送信息
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现双向通信，客户端需要实现Messenger
     */
    private final static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageConstant.MSG_CLI:
                    Log.i(TAG, "messenger___客户端收到消息："+ msg.getData().getString("reply"));
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Messenger replyMessenger = new Messenger(new MessengerHandler());

}
