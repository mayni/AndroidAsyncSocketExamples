package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.widget.Button;

import com.koushikdutta.async.*;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.net.InetSocketAddress;

public class Client {

    private String host ,message;
    private int port;



    public Client(String host, int port,String message) {
        this.host = host;
        this.port = port;
        this.message = message;
        setup();
    }
    private void setup() {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                new SendMessage(ex,socket,message);

            }
        });
    }


}