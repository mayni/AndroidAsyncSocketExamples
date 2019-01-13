package com.github.reneweb.androidasyncsocketexamples.tcp;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.net.InetSocketAddress;

public class Client {

    private clientMessageRecListener listener;
    private String host ,message ,mes;
    private int port;




    public interface clientMessageRecListener{
        void recMessage(String mes);
        void checkConnection(Exception e);
    }
    public void setListener(clientMessageRecListener listener) {
        this.listener = listener;
    }

    public Client(String host, int port,String message) {
        this.host = host;
        this.port = port;
        this.message = message;
        setup();

    }


    private void setup() {
        System.out.println("Client setup " );
        System.out.println("Client setup " + host);
        System.out.println("Client setup " + port);
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if(ex != null){
                    System.out.println("[Fail]" + ex.toString());
                    listener.checkConnection(ex);

                }else{
                    handleConnectCompleted(ex,socket);
                    listener.checkConnection(ex);
                }

//                handleConnectCompleted(ex,socket);
            }

        });

    }
    public void handleConnectCompleted(Exception ex, final AsyncSocket socket) {
        System.out.println("Client handleConnectCompleted " + socket.toString());

        if(ex != null) {
            ///////////////////////////////////////////////////////
            System.out.println("[Client] Faillllllllllllllllll" );
            throw new RuntimeException(ex);
        }

        Util.writeAll(socket, message.getBytes(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully wrote message");
            }
        });

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                mes = new String(bb.getAllByteArray());
                System.out.println("[Client] Received Message " + mes);
                listener.recMessage(mes);
                socket.close();
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if(ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if(ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully end connection");
            }
        });




    }


}