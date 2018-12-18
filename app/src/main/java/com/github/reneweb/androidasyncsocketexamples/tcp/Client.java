package com.github.reneweb.androidasyncsocketexamples.tcp;

<<<<<<< HEAD
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.github.reneweb.androidasyncsocketexamples.ConnectivityReceiver;
import com.koushikdutta.async.*;
=======
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
>>>>>>> 793462a52a02f3e3d6ea25fa8742e0bda82dc7b9
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements ConnectivityReceiver.ConnectivityReceiverListener {

    private clientMessageRecListener listener;
    private String host ,message ,mes;
    private int port;

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            AsyncServer.getCurrentThreadServer().stop();
//            listener.recMessage("disconnect");
        }
    }



    public interface clientMessageRecListener{
        void recMessage(String mes);
    }


    public void setListener(clientMessageRecListener listener) {
        this.listener = listener;
    }

<<<<<<< HEAD


    public Client(String host, int port, String message) {
=======
    public Client(String host, int port,String message) {
>>>>>>> 793462a52a02f3e3d6ea25fa8742e0bda82dc7b9
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
<<<<<<< HEAD
                if (ex == null && socket.isOpen() ){
//                    listener.recMessage("portCorrect");
                    handleConnectCompleted(ex,socket);
                }else {
                    listener.recMessage("portWrong");
                }
=======
                System.out.println("Client onConnectCompleted " + socket.toString());
                    handleConnectCompleted(ex,socket);

>>>>>>> 793462a52a02f3e3d6ea25fa8742e0bda82dc7b9
            }
        });
    }

    public void handleConnectCompleted(Exception ex, final AsyncSocket socket) {
        System.out.println("Client handleConnectCompleted " + socket.toString());

        if(ex != null) {
            ///////////////////////////////////////////////////////
            System.out.println("[Client] Faillllllllllllllllll" );
            socket.close();
//            throw new RuntimeException(ex);
        }

        Util.writeAll(socket, message.getBytes(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null){
                    listener.recMessage("disconnect");
                }
//                {throw new RuntimeException(ex);}
                System.out.println("[Client] Successfully wrote message");
            }
        });

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                mes = new String(bb.getAllByteArray());
                System.out.println("[Client] Received Message " + mes);
<<<<<<< HEAD
                if (mes.isEmpty()){
                    listener.recMessage("disconnect");
                }else {
                    listener.recMessage(mes);
                }

=======
                listener.recMessage(mes);
                socket.close();
>>>>>>> 793462a52a02f3e3d6ea25fa8742e0bda82dc7b9
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
//                if(ex != null) throw new RuntimeException(ex);
                listener.recMessage("disconnect");
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