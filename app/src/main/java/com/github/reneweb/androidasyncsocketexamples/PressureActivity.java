package com.github.reneweb.androidasyncsocketexamples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PressureActivity extends AppCompatActivity implements View.OnClickListener {
    Thread thread;
    private RecyclerView pressureRecycle;
    private PressureAdapter pressureAdapter;
    private List<Pressure> pressList;
    private ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);
        pressureRecycle = findViewById(R.id.pressureRecycle);
        pressList = new ArrayList<>();
        backbtn = findViewById(R.id.back_main);
        backbtn.setOnClickListener(this);

        pressureRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pressureAdapter = new PressureAdapter();


        getPressure();
    }
    protected void onStop(){
        super.onStop();
        thread.interrupt();
    }
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }
    public void getPressure(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1200);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncTask<Void,Void,Void>(){


                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        Client client = new Client("10.0.0.177",12345,"01");
                                        client.setListener(new Client.clientMessageRecListener() {
                                            @Override
                                            public void recMessage(final String mes) {
                                                System.out.println("[Main] time" + mes);
                                                new AsyncTask<Void,Void,String>(){

                                                    @Override
                                                    protected String doInBackground(Void... voids) {
                                                        return mes.trim();
                                                    }

                                                    @Override
                                                    protected void onPostExecute(String message) {
                                                        super.onPostExecute(message);
                                                        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                                        Date date = new Date();
                                                        pressList.add(0,new Pressure(message,dateFormat.format(date)));
                                                        pressureAdapter.setPressures(pressList);
                                                        pressureRecycle.setAdapter(pressureAdapter);
                                                    }
                                                }.execute();


                                            }

                                            @Override
                                            public void checkConnection(Exception e) {

                                            }
                                        });
                                        return null;
                                    }

                                }.execute();


                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == backbtn.getId()){
            Intent intent = new Intent(PressureActivity.this,MainActivity.class);
            startActivity(intent);
            thread.interrupt();
        }
    }
}
