package com.github.reneweb.androidasyncsocketexamples;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.R;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserLogsFragment extends Fragment implements View.OnClickListener {

    TextView status,modeShow;
    TextView timer;
    EditText ipAddress,portNumber;
    CombineActivity combineActivity;
    String keepString = " ";

    ArrayList<String> arrayList = new ArrayList<>();

    Thread t;

    Boolean checked = false;

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

    public void sendTosetText(String time, Boolean changed, Intent intent) {
        keepString = time;
        this.checked = changed;
        System.out.println("[Main] : im hereeeeeeeee " + keepString + checked);

//        theardForTime();
    }

    public void setActivity(FragmentActivity activity) {
        combineActivity = (CombineActivity) activity;
    }



    public interface CalibratePressOnListener{
        void isCalibratePressOn(boolean bool);
    }

    public void setListener(CalibratePressOnListener listener) {
        this.listener = listener;
    }

    private void readTimeFile(){

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = getActivity().openFileInput("time.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lastLine = null;
            String text;
            while ((text = bufferedReader.readLine()) != null){
                lastLine = text;
            }
            for (String m:lastLine.split("\\s")){
                arrayList.add(m.trim());
            }
            inputStreamReader.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile2(){
        FileOutputStream file1 = null;
        String data = "08:00"+" "+"false";
        try {
            file1 = getActivity().openFileOutput("time.txt", MODE_PRIVATE);
            file1.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void theardForTime(){
//        timer.setText("55555555555555555555555");
//        if (checked){

//    }
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
//                        System.out.println("[Main] : thearddddd starttttt" +  getTargetFragment());
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {

                                        readTimeFile();
                                        System.out.println("[hhhh] : ssssssss111" + arrayList.get(0)+ arrayList.get(1)) ;

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

//                                        if (arrayList.get(1) == "true"){
                                            timer.setText(arrayList.get(0));
//                                        }

                                    }
                                }.execute();
                            }
                        });

//                            new AsyncTask<Void, Void, String>() {
//                                @Override
//                                protected String doInBackground(Void... voids) {
//                                    String mes = keepString;
//                                    return mes;
//                                }
//
//                                @Override
//                                protected void onProgressUpdate(Void... values) {
////                                    if (check) {
//                                        super.onProgressUpdate(values);
////                                    }
//                                }
//
//                                @Override
//                                protected void onPostExecute(String s) {
//                                    super.onPostExecute(s);
//                                    timer.setTextColor(Color.RED);
//                                    timer.setText(s);
//
//                                }
//                            }.execute();


                    }
                } catch (InterruptedException e) {

                }
            }
        };
        t.start();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_logs,container,false);

//        Bundle bundle = this.getArguments();
//
//        if(bundle != null){
////           combineActivity = bundle.getString("key");
//        }

//        System.out.println("[Main] : timeeeeeeeeeee " + timerStop.getText().toString());
        setView(view);
        setOnClick();
        writeToFile2();
//        timeUser.setOnFocusChangeListener(this);
        theardForTime();
//        setSending();

        return view;
    }



    private void setSending(final String msg) {
        final String ip = ipAddress.getText().toString();
        final Integer port = Integer.parseInt(portNumber.getText().toString());
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client(ip,port,msg);
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

    private void setView(View view) {
        rightwork = view.findViewById(R.id.rightwork);
        leftwork = view.findViewById(R.id.leftwork);
        bothwork = view.findViewById(R.id.bothwork);
        calibratework = view.findViewById(R.id.calibrate_angle);
        emergencywork = view.findViewById(R.id.emergency);

        status = getActivity().findViewById(R.id.statusBed);
        ipAddress = getActivity().findViewById(R.id.ipBed);
        portNumber = getActivity().findViewById(R.id.port);
        timer = view.findViewById(R.id.timer);
//        timer.setText(keepString);
        modeShow = view.findViewById(R.id.modeShow);

    }




    @Override
    public void onClick(View v) {

        if(v.getId() == rightwork.getId() || v.getId() == leftwork.getId() || v.getId() == bothwork.getId() ){
            dialogSetting(v.getId());
        }else if(v.getId() == calibratework.getId()){
            listener.isCalibratePressOn(true);
        }else if(v.getId() == emergencywork.getId()){
            setSending("0B 00 FF 00 0000 00 0000");
            modeShow.setText(" - ");
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
                    modeShow.setText("Right");
                }else if(id == leftwork.getId()){
                    String left = "0A 04 FF 42 012C 03FF 03FF 63 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600).substring(4)+" 0000 0000";
                    setSending(left);
                    setSending(onVal);
                    modeShow.setText("Left");
                }else if(id == bothwork.getId()){
                    String both = "0A 08 FF 42 012C 03FF 03FF 63 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600)+" 0000 0000"+" 82 012C 03FF 03FF 93 "+decToHex(timeTime-300).substring(4)+" "+PRESSURE_SIDE+" "+PRESSURE_MAIN+" 40 0258 0000 0000 00 "+decToHex(timeTime-600)+" 0000 0000";
                    setSending(both);
                    setSending(onVal);
                    modeShow.setText("Both");
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


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
//        combineActivity =(CombineActivity) activity;
        System.out.println("[Main] : Attttachhhhh");

    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){ // fragment is visible
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//
//        }
//    }




}
