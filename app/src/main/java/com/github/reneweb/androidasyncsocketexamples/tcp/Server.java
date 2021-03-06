package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.app.Activity;

import com.github.reneweb.androidasyncsocketexamples.MainActivity;
import com.koushikdutta.async.*;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.ListenCallback;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

    private InetAddress host;
    private int port;

    public Server(String host, int port) throws UnknownHostException {

        try {
            this.host = InetAddress.getByName(host);
            System.out.println("[Server]" + host);

        } catch (UnknownHostException e) {
            System.out.println("[Server] Not Connected" );
            throw new RuntimeException(e);
        }

        this.port = port;

        setup();

    }

    private void setup() {

        AsyncServer.getDefault().listen(host, port, new ListenCallback() {

            @Override
            public void onAccepted(final AsyncSocket socket) {
                handleAccept(socket);
            }
            @Override
            public void onListening(AsyncServerSocket socket) {
                System.out.println("[Server] Server started listening for connections");
            }

            @Override
            public void onCompleted(Exception ex) {
                if(ex != null) {
                    System.out.println("[Server] Successfully shutdown server");
//                    throw new RuntimeException(ex);
                }else {

                }

            }
        });
    }

    private void handleAccept(final AsyncSocket socket) {
        System.out.println("[Server] New Connection " + socket.toString());

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                System.out.println("[Server] Received Message " + new String(bb.getAllByteArray()));

                Util.writeAll(socket, "Hello Client".getBytes(), new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        if (ex != null) throw new RuntimeException(ex);
                        System.out.println("[Server] Successfully wrote message");

                    }
                });
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Server] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Server] Successfully end connection");
            }
        });
    }
}