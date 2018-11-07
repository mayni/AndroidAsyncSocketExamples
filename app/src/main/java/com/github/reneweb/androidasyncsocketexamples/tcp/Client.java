package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import com.koushikdutta.async.*;
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
               handleConnectCompleted(ex,socket);
            }
        });


    }
    public void handleConnectCompleted(Exception ex, final AsyncSocket socket) {

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
                System.out.println("[Client]" + emitter);
                mes = new String(bb.getAllByteArray());
                System.out.println("[Client] Received Message " + mes);
                listener.recMessage(mes);
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