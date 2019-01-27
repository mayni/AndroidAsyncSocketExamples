package com.github.reneweb.androidasyncsocketexamples.call;

import android.os.AsyncTask;
import android.renderscript.Sampler;

import com.github.reneweb.androidasyncsocketexamples.Pressure;
import com.github.reneweb.androidasyncsocketexamples.ViewType;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallToTCP {
    private String ip ,message;
    private int port;
    public CallToTCPListener listener;
    private Task task;
    private class Task extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            Client client = new Client(ip,port,message);
                client.setListener(new Client.clientMessageRecListener() {

                    @Override
                    public void recMessage(final String mes) {
                        processValue(mes);
                        onCancelled();
                    }

                    @Override
                    public void checkConnection(Exception e) {

                    }

                    @Override
                    public void checkWifi(Exception e) {

                    }
                });
                return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            task.cancel(true);
        }
    }
    public CallToTCP (String ip , Integer port , String message){
        this.ip = ip;
        this.port = port;
        this.message = message;
        task= (Task) new Task().execute();
    }
    public interface CallToTCPListener{
        void recMessageCallBack(String mes);
    }

    public void setListener(CallToTCPListener listener) {
        this.listener = listener;
    }

    public  void processValue(String string) {
        listener.recMessageCallBack(string);
        task.cancel(true);
    }

}
