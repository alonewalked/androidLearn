package com.example.artdemo.binder.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService extends Service {

    private static final String TAG = "SocketServerService";
    private boolean isServiceDestroyed = false;

    public SocketServerService() {

    }

    public boolean isServiceDestroyed() {
        return isServiceDestroyed;
    }

    @Override
    public void onCreate() {
        // 创建线程并创建TCP连接
        //new Thread(new TcpServer(this)).start();
        Log.i(TAG, "socket___service oncreate___" );
        super.onCreate();
    }

    // 服务启动
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "socket___service onStartCommand___" );
        // 创建线程并创建TCP连接
        new Thread(new TcpServer(this)).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        isServiceDestroyed = true;
        super.onDestroy();
    }

    /**
     * response client
     */
    public void responseClient(Socket client) throws IOException {
        // 接受客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端输出消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("您好，我是服务端");
        while (!isServiceDestroyed) {
            String str = in.readLine();
            Log.i(TAG, "socket___收到客户端发来的信息: " + str);
            if (TextUtils.isEmpty(str)) {
                //客户端断开了连接
                Log.i(TAG, "socket___客户端断开连接");
                break;
            }
            String message = "收到了客户端的信息为：" + str;
            // 从客户端收到的消息加工再发送给客户端
            out.println(message);
        }
        out.close();
        in.close();
        client.close();
    }

    /**
     * TCP server 端
     */
    private static class TcpServer implements Runnable {
        WeakReference<SocketServerService> wService;

        public TcpServer(SocketServerService service) {
            wService = new WeakReference<SocketServerService>(service);
        }

        @Override
        public void run(){
            try {
                this.create();
            }
            catch (IOException e) {
                Log.e(TAG, "socket___" + e.getMessage());
            }
        }
        private void create() throws IOException{
            ServerSocket socket = null;
            //监听端口
            try {
                socket = new ServerSocket(SocketConstant.SOKET_PORT);
                while (null != wService.get() &&
                        !wService.get().isServiceDestroyed()) {
                    try {
                        // 接受客户端请求，并且阻塞直到接收到消息
                        final Socket client = socket.accept();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    wService.get().responseClient(client);
                                } catch (IOException e) {
                                    Log.e(TAG, "socket___" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } catch (IOException e) {
                        Log.e(TAG, "socket___" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }finally {

            }
        }
    }
}
