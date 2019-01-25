package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.FileOutputStream;
import java.util.function.BinaryOperator;

import static android.content.Context.MODE_APPEND;


public class TestDirectFragment extends Fragment implements  View.OnClickListener {

    public BackToTestListener listener;
    Button mainPump,sidePump,leftsideValve,leftmainValve,rightmainValve,rightsideValve;
    Toolbar toolbar;

    String PROCESS = "Process.txt";
    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public TestDirectFragment() {

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
        View view = inflater.inflate(R.layout.fragment_test_direct, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setView(view);
        setOnclick();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Direct Control");
        toolbar.setNavigationOnClickListener(this);

        return view;
    }

    private void setSending(final String msg) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client("10.80.66.207",12345,msg);
                client.setListener(new Client.clientMessageRecListener() {
                    @Override
                    public void recMessage(String mes) {

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

        }.execute();
    }

    private void setOnclick() {
        mainPump.setOnClickListener(this);
        sidePump.setOnClickListener(this);
        leftsideValve.setOnClickListener(this);
        leftmainValve.setOnClickListener(this);
        rightsideValve.setOnClickListener(this);
        rightmainValve.setOnClickListener(this);
    }

    private void setView(View view) {
        mainPump=view.findViewById(R.id.main_pump);
        sidePump=view.findViewById(R.id.side_pump);
        leftmainValve=view.findViewById(R.id.left_mainValve);
        leftsideValve=view.findViewById(R.id.left_sideValve);
        rightmainValve=view.findViewById(R.id.right_mainValve);
        rightsideValve=view.findViewById(R.id.right_sideValve);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mainPump.getId()){
            setMessage((byte) 0b00000001,0);
        }else if(v.getId() == sidePump.getId()){
            setMessage((byte) 0b00000010,0);
        }else if(v.getId() == leftmainValve.getId()){
            setMessage((byte) 0b00010000,1);
        }else if(v.getId() == leftsideValve.getId()){
            setMessage((byte) 0b01000000,1);
        }else if(v.getId() == rightsideValve.getId()){
            setMessage((byte) 128,1);
        }else if(v.getId() == rightmainValve.getId()){
            setMessage((byte) 0b00100000,1);
        }else{
            listener.PressBackButton(true);
        }
    }
    byte command = (byte) 0b00000000;

    private void setMessage(byte mes, int sec) {
        command = (byte) (command | mes);
        System.out.println(command);


    }
    public static String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }


    private void writeTofile(String dat) {
        FileOutputStream fos = null;
        String data = dat + "\n";
        try {
            fos = getActivity().openFileOutput(PROCESS, MODE_APPEND);
            fos.write(data.getBytes());
//
//            fos = getActivity().openFileOutput(PROCESS, Context.MODE_PRIVATE);
//            fos.write("".getBytes());
        }
        catch (Throwable t) {

        }
    }


}
