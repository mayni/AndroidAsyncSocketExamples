package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PressureFragment extends Fragment implements View.OnClickListener {
    Thread thread;
    private RecyclerView pressureRecycle;
    private PressureAdapter pressureAdapter;
    private List<Pressure> pressList;

    public BackToTestListener listener;


    public PressureFragment() {

    }
    public interface BackToTestListener{
        void PressBackButton(boolean bool);
    }
    public void setListener(BackToTestListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pressure,container,false);
        pressureRecycle = view.findViewById(R.id.pressureRecycle);
        pressList = new ArrayList<>();

        pressureRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pressureAdapter = new PressureAdapter();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Read Pressure");
        toolbar.setNavigationOnClickListener(this);

        getPressure();

        return view;
    }
    public void getPressure(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1200);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncTask<Void,Void,Void>(){


                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        Client client = new Client("10.80.67.104",12345,"01");
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
        listener.PressBackButton(true);
        thread.interrupt();

    }

}
