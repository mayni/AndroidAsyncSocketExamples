package com.github.reneweb.androidasyncsocketexamples.tcp;


import com.github.reneweb.androidasyncsocketexamples.ConnectivityReceiver;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;

public class Client implements ConnectivityReceiver.ConnectivityReceiverListener {

    private clientMessageRecListener listener;
    private String host ,message ,mes;
    private int port;

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            System.out.println("[Main] Closed Wifi");
        }
    }



    public interface clientMessageRecListener {
        void recMessage(String mes);
        void checkConnection(Exception e);
        void checkWifi(Exception e);
    }


    public void setListener(clientMessageRecListener listener) {
        this.listener = listener;
    }


    public Client(String host, int port, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
        setup();
    }


    private void setup() {
        System.out.println("Client setup " + host);
        System.out.println("Client setup " + port);
        System.out.println("Client setup " + message);
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if(ex != null){
//                    System.out.println("[Fail]" + ex.toString());
                    System.out.println("[Fail]" + ex.toString());
                    listener.checkWifi(ex);
                    listener.checkConnection(ex);

                }else{
                    handleConnectCompleted(ex,socket);
//                    listener.checkConnection(ex);
//                    listener.checkWifi(ex);
                    listener.checkConnection(ex);
                }

//                handleConnectCompleted(ex,socket);
            }

        });

    }

    public void handleConnectCompleted(Exception ex, final AsyncSocket socket) {
//        System.out.println("Client handleConnectCompleted " + socket.toString());



        if(ex != null) {
            ///////////////////////////////////////////////////////
            System.out.println("[Client] Faillllllllllllllllll" );
            socket.close();
//            throw new RuntimeException(ex);
        }

        Util.writeAll(socket, message.getBytes(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
//                if (ex != null){
//                    listener.recMessage("disconnect");
//                }
//                {throw new RuntimeException(ex);}
                System.out.println("[Client] Successfully wrote message");
            }
        });

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                mes = new String(bb.getAllByteArray());
                System.out.println("[Client] Received Message " + mes);
//                if (mes.isEmpty()){
//                    listener.recMessage("disconnect");
//                }else {
//                    listener.recMessage(mes);
//                }
                listener.recMessage(mes);

                socket.close();
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
//                if(ex != null) throw new RuntimeException(ex);
//                listener.recMessage("disconnect");
                listener.checkWifi(ex);
                System.out.println("[Client] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
//                if(ex != null) throw new RuntimeException(ex);
//                listener.recMessage("disconnect");
                System.out.println("[Client] Successfully end connection");
            }
        });




    }


}