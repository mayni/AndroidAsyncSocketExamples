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
import com.github.reneweb.androidasyncsocketexamples.call.CallToTCP;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static android.content.Context.MODE_PRIVATE;



public class UserLogsFragment extends Fragment implements View.OnClickListener {

    TextView status,getStatus,modeShow;
    TextView timer;
    CombineActivity combineActivity;
    String keepString = " ";

    ArrayList<String> arrayList = new ArrayList<>();

    Thread t;

    Boolean checked = false;

    EditText ipAddress,portNumber;
    Thread thread1,thread2;
    Button rightwork,leftwork,bothwork,emergencywork,calibratework;
    Button firstB,secB,thirdB,forthB;
    public String LEFT_PRESSURE_SIDE ,LEFT_PRESSURE_MAIN ;
    String RIGHT_PRESSURE_SIDE = "0000",RIGHT_PRESSURE_MAIN = "0000";
    Integer OFFSET_LEFT = 0;
    Integer OFFSET_RIGHT = 0;
    Integer OFFSET_SUPINR = 0;
    String text;
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
//        keepString = time;
        this.checked = changed;
        System.out.println("[Main] : im hereeeeeeeee " + keepString + checked);
        if(changed==true){
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }

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
//                                        System.out.println("[hhhh] : ssssssss111" + arrayList.get(0)+ arrayList.get(1)) ;

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

//                                        if (arrayList.get(1) == "true"){
                                            timer.setText(arrayList.get(0));
//                                        }else {
//                                            timer.setText(" ");
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
        setBag();
//        setSending();





        return view;
    }
    public void setBag(){
        final String ip = ipAddress.getText().toString();
        final Integer port = Integer.parseInt(portNumber.getText().toString());
        thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(10000);





                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncTask<Void,Void,Void>(){


                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        Client client = new Client(ip,port,"02 0C 0E");
                                        client.setListener(new Client.clientMessageRecListener() {
                                            @Override
                                            public void recMessage(final String mes) {


                                                new AsyncTask<Void,Void,String>(){

                                                    @Override
                                                    protected String doInBackground(Void... voids) {
                                                       return mes.trim();
                                                    }

                                                    @Override
                                                    protected void onPostExecute(String str) {
                                                        super.onPostExecute(str);

                                                        String message = str.trim();
                                                        ArrayList<String> data = new ArrayList<>();
                                                        for(String m:message.split("\\s")){
                                                            data.add(m.trim());
                                                        }
                                                        System.out.println("iiiiiiiiiiiiiiii"+data.get(1));
                                                        Integer buttonNumber = Integer.parseInt(Integer.toBinaryString(Integer.parseInt(data.get(1),16)));
                                                        String no = String.format("%08d", buttonNumber);

                                                        if(no.charAt(0) == '0'){
                                                            System.out.println("recMessageCallBack" + no.charAt(0));
                                                            forthB.setBackgroundResource(R.drawable.button);
                                                        }else{
                                                            System.out.println("recMessageCallBack" + no.charAt(0));
                                                            forthB.setBackgroundResource(R.drawable.button_on);
                                                        }
                                                        if(no.charAt(1) == '0'){
                                                            System.out.println("recMessageCallBack" + no.charAt(1));
                                                            firstB.setBackgroundResource(R.drawable.button);
                                                        }else{
                                                            System.out.println("recMessageCallBack" + no.charAt(1));
                                                            firstB.setBackgroundResource(R.drawable.button_on);
                                                        }
                                                        if(no.charAt(2) == '0'){
                                                            System.out.println("recMessageCallBack" + no.charAt(2));
                                                            thirdB.setBackgroundResource(R.drawable.button);
                                                        }else{
                                                            System.out.println("recMessageCallBack" + no.charAt(2));
                                                            thirdB.setBackgroundResource(R.drawable.button_on);
                                                        }
                                                        if(no.charAt(3) == '0'){
                                                            System.out.println("recMessageCallBack" + no.charAt(3));
                                                            secB.setBackgroundResource(R.drawable.button);
                                                        }else{
                                                            System.out.println("recMessageCallBack" + no.charAt(3));
                                                            secB.setBackgroundResource(R.drawable.button_on);
                                                        }
                                                        if(data.get(1).trim().equals("82")||data.get(1).trim().equals("93")||data.get(1).trim().equals("90")){
                                                            getStatus.setText("Right");
                                                        }
                                                        if(data.get(1).trim().equals("42")||data.get(1).trim().equals("63")||data.get(1).trim().equals("60")){
                                                            getStatus.setText("Left");
                                                        }
                                                        if(data.get(1).trim().equals("80")||data.get(1).trim().equals("00")||data.get(1).trim().equals("40")){
                                                            getStatus.setText("Supine");
                                                        }
                                                        if(data.get(1).trim().equals("03") || data.get(1).trim().equals("83")){
                                                            getStatus.setText("Supine");
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
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread1.start();
    }

    private void setStatusBag() {
        String ip = ipAddress.getText().toString();
        Integer port =Integer.parseInt(portNumber.getText().toString()) ;
        final String[] data = new String[1];
        CallToTCP callToTCP = new CallToTCP(ip,port,"02 0C 0E");
        callToTCP.setListener(new CallToTCP.CallToTCPListener() {
            @Override
            public void recMessageCallBack(String mes) {
                String message = mes.trim();
                ArrayList<String> data = new ArrayList<>();
                for(String m:message.split("\\s")){
                    data.add(m.trim());
                }
                System.out.println("recMessageCallBack" + Integer.toBinaryString(Integer.parseInt(data.get(1),16)));


            }
        });

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

        firstB=view.findViewById(R.id.firstBag);
        secB=view.findViewById(R.id.secondBag);
        thirdB=view.findViewById(R.id.thirdBag);
        forthB=view.findViewById(R.id.forthBag);

        getStatus=view.findViewById(R.id.statusBag);


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
        readFile();
        final String onVal = "0B 01 FF 00 0000 00 0000";
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int timeTime = (hourTime*60*60)+(minuteTime*60);
                if(id == rightwork.getId()){
                    String right = "0A 04 FF "
                            +"82 012C 03FF 03FF"
                            +" 93 "+decToHex(timeTime-OFFSET_RIGHT).substring(4)+" "+RIGHT_PRESSURE_SIDE+" "+RIGHT_PRESSURE_MAIN
                            +" 80 0258 0000 0000 "
                            +"00 "+decToHex(timeTime-OFFSET_SUPINR).substring(4)+" 0000 0000";
                    setSending(right);
                    setSending(onVal);
                    modeShow.setText("Right");
                }else if(id == leftwork.getId()){
                    String left = "0A 04 FF "
                            +"42 012C 03FF 03FF"
                            +" 63 "+decToHex(timeTime-OFFSET_LEFT).substring(4)+" "+LEFT_PRESSURE_SIDE+" "+LEFT_PRESSURE_MAIN
                            +" 40 0258 0000 0000"
                            +" 00 "+decToHex(timeTime-OFFSET_SUPINR).substring(4)+" 0000 0000";
                    setSending(left);
                    setSending(onVal);
                    modeShow.setText("Left");
                }else if(id == bothwork.getId()){
                    String both = "0A 08 FF "
                            +"42 012C 03FF 03FF"
                            +" 63 "+decToHex(timeTime-OFFSET_LEFT).substring(4)+" "+LEFT_PRESSURE_SIDE+" "+LEFT_PRESSURE_MAIN
                            +" 40 0258 0000 0000"
                            +" 00 "+decToHex(timeTime-OFFSET_SUPINR).substring(4)+" 0000 0000"
                            +" 82 012C 03FF 03FF "
                            +"93 "+decToHex(timeTime-OFFSET_RIGHT).substring(4)+" "+RIGHT_PRESSURE_SIDE+" "+RIGHT_PRESSURE_MAIN
                            +" 80 0258 0000 0000"
                            +" 00 "+decToHex(timeTime-OFFSET_RIGHT).substring(4)+" 0000 0000";
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

        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            thread1.interrupt();

        }else {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


}
