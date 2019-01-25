package com.github.reneweb.androidasyncsocketexamples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.reneweb.androidasyncsocketexamples.R;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UserLogsFragment extends Fragment implements View.OnClickListener {

    TextView status;
    EditText ip,port;

    Button rightwork,leftwork,bothwork,emergencywork,calibratework;

    String PRESSURE_SIDE = "0000",PRESSURE_MAIN = "0000";
    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public CalibratePressOnListener listener;
    Integer hourTime=0,minuteTime =0;


    public UserLogsFragment() {

    }
    public interface CalibratePressOnListener{
        void isCalibratePressOn(boolean bool);
    }

    public void setListener(CalibratePressOnListener listener) {
        this.listener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_logs,container,false);
        View view1 = inflater.inflate(R.layout.activity_combine,container,false);
        setView(view,view1);
        setOnClick();
//        setSending();

        return view;
    }

    private void setSending(final String msg) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                String portStr = port.getText().toString();
                int portNumber = Integer.parseInt(portStr);
                Client client = new Client(ip.getText().toString(),portNumber,msg);
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

    private void setOnClick() {
        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);
        calibratework.setOnClickListener(this);
        emergencywork.setOnClickListener(this);
    }

    private void setView(View view,View view1) {
        rightwork = view.findViewById(R.id.rightwork);
        leftwork = view.findViewById(R.id.leftwork);
        bothwork = view.findViewById(R.id.bothwork);
        calibratework = view.findViewById(R.id.calibrate_angle);
        emergencywork = view.findViewById(R.id.emergency);

        status = view1.findViewById(R.id.statusBed);
        ip = view1.findViewById(R.id.ipBed);
        port = view1.findViewById(R.id.port);
    }




    @Override
    public void onClick(View v) {

        if(v.getId() == rightwork.getId() || v.getId() == leftwork.getId() || v.getId() == bothwork.getId() ){
            dialogSetting(v.getId());
        }else if(v.getId() == calibratework.getId()){
            listener.isCalibratePressOn(true);
        }else if(v.getId() == emergencywork.getId()){
            setSending("EE");
        }
    }

    private void dialogSetting(final int id) {
        final String onVal = "0B 01 FF 00 0000 00 0000";
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View theView = inflater.inflate(R.layout.time_picker, null);


        NumberPicker repeat = theView.findViewById(R.id.repeat);
        NumberPicker time = theView.findViewById(R.id.time1);
        NumberPicker time1 = theView.findViewById(R.id.time2);

        time.setMaxValue(2);
        time.setMinValue(0);


        time1.setMaxValue(59);
        time1.setMinValue(0);

        repeat.setMaxValue(20);
        repeat.setMinValue(1);



        time.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
                hourTime = newVal;
            }
        });
        time1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
                minuteTime = newVal;
            }
        });
//        builder.setTitle("Set time");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int timeTime = (hourTime*60*60)+(minuteTime*60);
                if(id == rightwork.getId()){
                    String right = "0A 04 FF 82 012C 03FF 03FF 93 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600).substring(4)+" 0000 0000";
                    setSending(right);
                    setSending(onVal);
                }else if(id == leftwork.getId()){
                    String left = "0A 04 FF 42 012C 03FF 03FF 63 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600).substring(4)+" 0000 0000";
                    setSending(left);
                    setSending(onVal);
                }else if(id == bothwork.getId()){
                    String both = "0A 08 FF 42 012C 03FF 03FF 63 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600)+" 0000 0000"+" 82 012C 03FF 03FF 93 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600)+" 0000 0000";
                    setSending(both);
                    setSending(onVal);
                }
//                setSending(onVal);
            }
        });
        builder.setView(theView);


        builder.setNegativeButton("CANCEL",null);
        builder.create();
        builder.show();

    }
//    0x02 0x0C 0x0E
    public void readFile(){
        try {
            FileInputStream fileIn=getActivity().openFileInput("Pressure.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(InputRead);
            StringBuilder sb = new StringBuilder();
            String text;
            String ab = null;
            while ((text = br.readLine()) != null){
                System.out.println("[Pressure]" + text);
                ab = text;
            }
            InputRead.close();
            ArrayList<String> press = new ArrayList<>();
            for(String str : ab.split("\\s") ){
                press.add(str);
            }
            PRESSURE_SIDE = press.get(2);
            PRESSURE_MAIN = press.get(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
