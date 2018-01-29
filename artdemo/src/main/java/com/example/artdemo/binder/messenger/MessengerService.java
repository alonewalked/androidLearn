package com.example.artdemo.binder.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by tj on 2018/1/4.
 */

public class MessengerService extends Service {

    private static final String TAG = "MessageHandler";

    /**
     * 通过MessengerHandler创建Messenger对象
     */
    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageConstant.MSG_SEV:
                    Log.i(TAG, "messenger___服务端收到消息："+ msg.getData().getString("msg"));

                    // 接受replyTo参数
                    Messenger replyMessenger = msg.replyTo;
                    if (null != replyMessenger) {
                        Message replyMessage = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("reply", "服务端已经收到消息");
                        replyMessage.what = MessageConstant.MSG_CLI;
                        replyMessage.setData(bundle);
                        try {
                            replyMessenger.send(replyMessage);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    //此Messenger将客户端发送的消息传递给 MessengerHandler
    private Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 使用messenger返回底层Binder
        return messenger.getBinder();
    }
}
