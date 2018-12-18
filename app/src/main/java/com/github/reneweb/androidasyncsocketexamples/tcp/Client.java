package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.github.reneweb.androidasyncsocketexamples.ConnectivityReceiver;
import com.koushikdutta.async.*;
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



    public Client(String host, int port, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
        setup();
    }

    private void setup() {

        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if (ex == null && socket.isOpen() ){
//                    listener.recMessage("portCorrect");
                    handleConnectCompleted(ex,socket);
                }else {
                    listener.recMessage("portWrong");
                }
            }
        });
    }

    public void handleConnectCompleted(Exception ex, final AsyncSocket socket) {

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
                System.out.println("[Client]" + emitter);
                mes = new String(bb.getAllByteArray());
                System.out.println("[Client] Received Message " + mes);
                if (mes.isEmpty()){
                    listener.recMessage("disconnect");
                }else {
                    listener.recMessage(mes);
                }

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