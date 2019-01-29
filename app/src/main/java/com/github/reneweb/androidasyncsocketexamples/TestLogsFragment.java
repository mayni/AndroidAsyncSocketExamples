package com.github.reneweb.androidasyncsocketexamples;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.reneweb.androidasyncsocketexamples.call.TextChange;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.SYSTEM_HEALTH_SERVICE;

public class TestLogsFragment extends Fragment implements View.OnClickListener {
    final List<String> lists = new ArrayList<String>();
    private EditText message;
    String text,selectIp;
    private static MainActivity instance;
    Button directcontrol, calibratework, rightwork, leftwork, bothwork ,checkmodework , clearwork ,readpressurework ,emergencywork , detail,
            wifibtn,onwork,send;
    public String LEFT_PRESSURE_SIDE ,LEFT_PRESSURE_MAIN ;
    String RIGHT_PRESSURE_SIDE = "0000",RIGHT_PRESSURE_MAIN = "0000";
    Integer OFFSET_LEFT = 0;
    Integer OFFSET_RIGHT = 0;
    Integer OFFSET_SUPINR = 0;

    TextView wifiStatus,bedStatus,status;


    EditText leftPressSide,rightPressSide,leftPressMain,rightPressMain,leftOffset,rightOffset,supineOffset;
    private RecyclerView recyclerView, recyclerViewRec ;
    private MainAdapter adapter, adapterRec ;
    private List<BaseItem> itemList, itemListRec ;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int hour, min;
    Calendar calendar;


    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    String [] BITS = {"main pump","side pump","Valve main left","Valve main right","Valve side left", "Valve side right"};
    String[] ipAddr;
    boolean[] Checkbit = new boolean[]{false, false, false, false, false, false};
    WifiManager wifi;
    String mSelected;
    Integer time = 0;

    private List<ScanResult> wifiList;


    ///// ------------------------------------ NETWORK CREDENTIALS
    String networkSSID = "TP-Link_1316";
    String networkPass = "60177094";

    WifiManager wifiManager;

    Integer hourTime=0,minuteTime =0;

    EditText iptest,porttest;
    EditText ipAddress,portNumber;

    String valRightMain,valRightSide,valLeftMain,valLeftSide,valOffLeft,valOffRight,valOffSupine;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"StaticFieldLeak", "WrongViewCast"})

    private String mParam1;
    private String mParam2;

    private DirectControllistener listener;
    FragmentManager manager;

    public TestLogsFragment() {


    }




    public interface DirectControllistener{
        void DirectControlOnclick(boolean bool,String string);

    }
    public void setListener(DirectControllistener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_logs, container, false);
        View view1 = inflater.inflate(R.layout.activity_combine,container,false);

        setView(view,view1);

        ipAddress = getActivity().findViewById(R.id.ipBed);
        portNumber = getActivity().findViewById(R.id.port);

        setOnClick();
        setItemlist();
        setWiFi();

        TestFragment testFragment = new TestFragment();


        return view;
    }


    public void displayReceivedData(String message)
    {
        System.out.println(message);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    private void setWiFi(){
//        wifi =(WifiManager)
//                getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (getConnection()){
//            wifibtn.setText("disconnect");
//            wifiStatus.setText("Connected");
//            bedStatus.setText("Connected");
//            bedStatus.setTextColor(Color.GREEN);
//            findViewById(R.id.sending).setEnabled(true);
//            getipAddress();
//        }else {
//            wifibtn.setText("connect");
//            findViewById(R.id.sending).setEnabled(false);
//            wifiStatus.setText("Disconnect");
//            bedStatus.setText("Disconnect");
//            bedStatus.setTextColor(Color.RED);
//        }
    }


    private void setItemlist() {
        itemList = new ArrayList<>();
        itemListRec = new ArrayList<>();
    }

    private void setView(View view,View view1){
        rightwork = view.findViewById(R.id.rightwork);
        leftwork =  view.findViewById(R.id.leftwork);
        bothwork =  view.findViewById(R.id.bothwork);
        directcontrol = view.findViewById(R.id.directcontrol);
        checkmodework = view.findViewById(R.id.checkmode);
        clearwork = view.findViewById(R.id.clear);
        detail = view.findViewById(R.id.detail);
        readpressurework = view.findViewById(R.id.readpressure);
        emergencywork = view.findViewById(R.id.emergency);
        calibratework = view.findViewById(R.id.calibrate);

        onwork = view.findViewById(R.id.onwork);
        emergencywork = view.findViewById(R.id.emergency);

        wifibtn = view.findViewById(R.id.wifiBtn);
        wifiStatus = view.findViewById(R.id.wifiSatus);
        bedStatus = view.findViewById(R.id.status);

        status = view1.findViewById(R.id.statusBed);
        send = view.findViewById(R.id.sending);

//        leftPressMain=view2.findViewById(R.id.leftPressureMain);
//        leftPressSide=view2.findViewById(R.id.leftPressureSide);
//        rightPressMain=view2.findViewById(R.id.rightPressureMain);
//        rightPressSide=view2.findViewById(R.id.rightPressureSide);
//        leftOffset=view2.findViewById(R.id.leftOffsetTime);
//        rightOffset=view2.findViewById(R.id.rightOffsetTime);


        message =  view.findViewById(R.id.message);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new MainAdapter();

        recyclerViewRec = view.findViewById(R.id.recyclerView1);
        recyclerViewRec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapterRec = new MainAdapter();








    }

    private void setOnClick(){
        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);
        checkmodework.setOnClickListener(this);
        clearwork.setOnClickListener(this);
        readpressurework.setOnClickListener(this);
        emergencywork.setOnClickListener(this);
        calibratework.setOnClickListener(this);
        directcontrol.setOnClickListener(this);

        onwork.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Integer id = view.getId();
        if(view.getId() == rightwork.getId() || view.getId() == leftwork.getId() || view.getId() == bothwork.getId()){
//            readFile();

            setTime(id);

        }else {
            if(view.getId() == checkmodework.getId()){
                String checkmode = "0A FF FF";
                message.setText(checkmode);
            }else if(view.getId() == clearwork.getId()){
                String clear = "0A 00 00";
                message.setText(clear);
            }else if(view.getId() == readpressurework.getId()){
                listener.DirectControlOnclick(true,"pressure");
            }else if(view.getId() == emergencywork.getId()){
                String emerg = "EE";
                    sendONOFF(emerg);
            }else if(view.getId() == calibratework.getId()){
//                checkConnection();
                    listener.DirectControlOnclick(true,"calibrate");
            }else if(view.getId() ==  directcontrol.getId()){
                setDirectControl();
            }else if(view.getId() == send.getId()){
                ClickToConnect();
            }else if(view.getId() == onwork.getId()){
                String on = "0B 01 FF 00 0000 00 0000";
                sendONOFF(on);
            }
        }

//        if (view.getId() == detail.getId() ){
//            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
//            System.out.println("---------------------");
//            String min = Integer.toString(time/60);
//            intent.putExtra("time",min);
//            startActivity(intent);
//        }
//        if(view.getId() == onwork.getId()){
//            String on = "0B 01 FF 00 0000 00 0000";
//            message.setText(on);
//        }
        if (view.getId() == wifibtn.getId()){

//            if (!getConnection()){
//                System.out.println("Clicked connect");
//                findViewById(R.id.sending).setEnabled(true);
////                findViewById(R.id.findIp).setEnabled(true);
//                connectWifi();
////                checkConnection();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                checkConnection();
////                    if (getConnection()){
//                getipAddress();
////                    }
////                    checkConnection();
//
//
//            }
//            else {
//                wifi.setWifiEnabled(false);
//                findViewById(R.id.sending).setEnabled(false);
//                findViewById(R.id.findIp).setEnabled(false);
//                checkConnection();
////                ipAddr = new String[0];
//
//            }
        }
//        if (view.getId() == findViewById(R.id.findIp).getId()){
//            System.out.println("[Main] : click detail");
////            getipAddress();
//
//            dialogIp();
//            System.out.println("[Main] : " + lists);
////            ipaddress.setText(lists.get(0));

    }
    private void sendONOFF(final String text){
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        final String ip = ipAddress.getText().toString();
        final int p = Integer.parseInt(portNumber.getText().toString());



        final String dateSend = dateFormat.format(date);

        itemList.add(0,new CardViewItem()
                .setText(text,dateFormat.format(date),"",""));
        adapter.setItemList(itemList);
        recyclerView.setAdapter(adapter);

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client(ip,  p, text);
                client.setListener(new Client.clientMessageRecListener() {
                    @Override
                    public void recMessage(final String mes) {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {

                                System.out.println("[Main] rec" + mes.trim());
                                return String.valueOf(mes.trim());
                            }

                            @Override
                            protected void onPostExecute(String mes) {
                                final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();
                                super.onPostExecute(mes);


                                if (mes.equals("disconnect")) {
                                    ////                                    bedStatus.setText("Disconnect");
                                    ////                                    bedStatus.setTextColor(Color.RED);
                                    ////                                    getActivity().findViewById(R.id.sending).setEnabled(false);
                                } else {
                                    //                                    bedStatus.setText("Connected");
                                    //                                    getActivity().findViewById(R.id.sending).setEnabled(true);
                                    //                                    bedStatus.setTextColor(getActivity().getColor(R.color.lightGreen));
                                    itemList.set(0, new CardViewItem().setText(text, dateSend, mes, dateFormat.format(date)));

                                    adapter.setItemList(itemList);
                                    recyclerView.setAdapter(adapter);
                                    itemListRec.add(0, new CardViewItem().setText(text, dateSend, dateFormat.format(date), mes));
                                    adapterRec.setItemList(itemListRec);
                                    recyclerViewRec.setAdapter(adapterRec);
                                }
                            }
                        }.execute();
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

    public void ClickToConnect() {
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        final String m = message.getText().toString();
        final String ip = ipAddress.getText().toString();
        final int port = Integer.parseInt(portNumber.getText().toString());

        final String dateSend = dateFormat.format(date);

        itemList.add(0,new CardViewItem()
                .setText(m,dateFormat.format(date),"",""));
        adapter.setItemList(itemList);
        recyclerView.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client(ip ,port, m);
                client.setListener(new Client.clientMessageRecListener() {
                    @Override
                    public void recMessage(final String mes) {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {

                            System.out.println("[Main] rec" + mes.trim());
                            return String.valueOf(mes.trim());
                        }

                            @Override
                            protected void onPostExecute(String mes) {
                                final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();
                                super.onPostExecute(mes);


                                if (mes.equals("disconnect")) {
    ////                                    bedStatus.setText("Disconnect");
    ////                                    bedStatus.setTextColor(Color.RED);
    ////                                    getActivity().findViewById(R.id.sending).setEnabled(false);
                                } else {
    //                                    bedStatus.setText("Connected");
    //                                    getActivity().findViewById(R.id.sending).setEnabled(true);
    //                                    bedStatus.setTextColor(getActivity().getColor(R.color.lightGreen));
                                    itemList.set(0, new CardViewItem().setText(m, dateSend, mes, dateFormat.format(date)));

                                    adapter.setItemList(itemList);
                                    recyclerView.setAdapter(adapter);
                                    itemListRec.add(0, new CardViewItem().setText(m, dateSend, dateFormat.format(date), mes));
                                    adapterRec.setItemList(itemListRec);
                                    recyclerViewRec.setAdapter(adapterRec);
                                }
                            }
                        }.execute();

                    }

                    @Override
                    public void checkConnection(Exception e) {

                    }

                    @Override
                    public void checkWifi(Exception e) {
                        final Exception err = e;
                        final String[] mes = {null};
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                if (err != null) {
                                    mes[0] = "Disconnect";

                                } else {
                                    mes[0] = "Connected";
                                }
                                return mes[0];
                            }

                            @Override
                            protected void onPostExecute(String mes) {
                                super.onPostExecute(mes);
                                status.setText(mes);
                                status.setTextColor((mes == "Connected" ? getActivity().getColor(R.color.lightGreen) : getActivity().getColor(R.color.red)));
                            }
                        }.execute();
                    }

                });
                return null;
            }

        }.execute();

        message.setText("");

    }


    private void setTime(final Integer id) {
        readFile();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View theView = inflater.inflate(R.layout.time_picker, null);

        NumberPicker time = theView.findViewById(R.id.time1);
        NumberPicker time1 = theView.findViewById(R.id.time2);

        time.setMaxValue(2);
        time.setMinValue(0);


        time1.setMaxValue(59);
        time1.setMinValue(0);

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
        builder.setTitle("Set time");
        builder.setView(theView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            int timeTime = (hourTime*60*60)+(minuteTime*60);
            @Override
            public void onClick(DialogInterface dialog, int which) {



                if(id == rightwork.getId()){
                    String right = "0A 04 FF "
                            +"82 012C 03FF 03FF"
                            +" 93 "+decToHex(timeTime-OFFSET_RIGHT).substring(4)+" "+RIGHT_PRESSURE_SIDE+" "+RIGHT_PRESSURE_MAIN
                            +" 80 0258 0000 0000 "
                            +"00 "+decToHex(timeTime-OFFSET_SUPINR).substring(4)+" 0000 0000";
                    message.setText(right);
                }else if(id == leftwork.getId()){
                    String left = "0A 04 FF "
                            +"42 012C 03FF 03FF"
                            +" 63 "+decToHex(timeTime-OFFSET_LEFT).substring(4)+" "+LEFT_PRESSURE_SIDE+" "+LEFT_PRESSURE_MAIN
                            +" 40 0258 0000 0000"
                            +" 00 "+decToHex(timeTime-OFFSET_SUPINR).substring(4)+" 0000 0000";
                    message.setText(left);
                }else if(id == bothwork.getId()){
                    String both = "0A 08 FF "
                            +"42 012C 03FF 03FF"
                            +" 63 "+decToHex(timeTime-OFFSET_LEFT).substring(4)+" "+LEFT_PRESSURE_SIDE+" "+LEFT_PRESSURE_MAIN
                            +" 40 0258 0000 0000"
                            +" 00 "+decToHex(timeTime-OFFSET_SUPINR)+" 0000 0000"
                            +" 82 012C 03FF 03FF "
                            +"93 "+decToHex(timeTime-OFFSET_RIGHT).substring(4)+" "+RIGHT_PRESSURE_SIDE+" "+RIGHT_PRESSURE_MAIN
                            +" 80 0258 0000 0000"
                            +" 00 "+decToHex(timeTime-OFFSET_RIGHT)+" 0000 0000";
                    message.setText(both);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL",null);
        builder.create();
        builder.show();
    }
    private static String decToHex(int dec) {
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
    private void setDirectControl() {
        listener.DirectControlOnclick(true,"direct");
    }
    public void readFile(){
        String[] filename = {"leftOffTime.txt","leftPressMain.txt","leftPressSide.txt",
                "rightOffTime.txt","rightPressMain.txt","rightPressSide.txt","supineOffTime.txt"};
        for(int i=0;i<filename.length;i++){
            try {
                FileInputStream fileIn=getActivity().openFileInput(filename[i]);
                InputStreamReader InputRead= new InputStreamReader(fileIn);
                BufferedReader br = new BufferedReader(InputRead);
                String lastline=null;
                while ((text = br.readLine()) != null){
//                    System.out.println("[Pressure]" + text);
                    lastline = text;
                }
                InputRead.close();
                ArrayList<String> press = new ArrayList<>();
                for(String str : lastline.split("\\s") ){
//                    System.out.println(str);
                    press.add(str.trim());
                }
                if(press.size() != 1){
                    if(press.get(1).equals("supineOffTime") || press.get(1).equals("leftOffTime") || press.get(1).equals("rightOffTime")){
                        if(press.get(1).equals("supineOffTime")){
                            OFFSET_SUPINR = Integer.parseInt(press.get(0))*60;
                        }else if(press.get(1).equals("leftOffTime")){
                            OFFSET_LEFT = Integer.parseInt(press.get(0))*60;
                        }else if(press.get(1).equals("rightOffTime")){
                            OFFSET_RIGHT = Integer.parseInt(press.get(0))*60;
                        }
                    }else{
                        float x = Float.parseFloat(press.get(0));
                        Integer a =  Math.round(1023*x/5);
                        String hex = decToHex(a);
                        System.out.println(x+" "+a+" "+hex.substring(4));
                        if(press.get(1).equals("leftPressMain")){
                            LEFT_PRESSURE_MAIN = hex.substring(4);
                        }else if(press.get(1).equals("leftPressSide")){
                            LEFT_PRESSURE_SIDE = hex.substring(4);
                        }else if(press.get(1).equals("rightPressMain")){
                            RIGHT_PRESSURE_MAIN=hex.substring(4);
                        }else if(press.get(1).equals("rightPressSide")){
                            RIGHT_PRESSURE_SIDE=hex.substring(4);
                        }

                    }
                }


//                if(press.size() != 1){
//                    if(!filename[i].equals(filename[0]) || !filename[i].equals(filename[3]) || !filename[i].equals(filename[6])){
//
//                    }
//
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}