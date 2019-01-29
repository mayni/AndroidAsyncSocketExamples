package com.github.reneweb.androidasyncsocketexamples;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.github.reneweb.androidasyncsocketexamples.call.CallToTCP;
import com.github.reneweb.androidasyncsocketexamples.call.TextChange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

import static android.content.Context.MODE_APPEND;


public class SettingFragment extends Fragment implements View.OnClickListener {

    String hourStop,minStop;
    Switch startTime;
    EditText leftPressSide,leftPressMain,leftOffTime,rightPressSide,rightPressMain,rightOffTime,supineOffTime;
    TextView timeStart,timer;
    EditText ipAddress,portNumber;
    String PRESSURE_RAW="PressureRaw.txt";
    Thread t;
    Boolean keep = false,changed =false;
    final String defaultTime = "08:00";
    CombineActivity combineActivity;
    private TextView timeUser;
    String keepString;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
   
   
    TestFragment testFragment ;

    public SettingFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        testFragment = new TestFragment();

        setView(view);
        timeStart.setOnClickListener(this);

        setEditText();

        setTextChange();
        return view;
    }



    private void passDataToUSerFragment(){
//        2131296536        2131296258
//        combineActivity.carryTo(timeStart.getText().toString(),startTime.isChecked());


//        userLogsFragment.sendForSetText();
        System.out.println("[Main] : 78945 "+ timeStart );
//        if (userFragment != null){
//            System.out.println("[Main] : 55555555555555555555");
//        }else {
//            System.out.println("[Main] : nullllllllllll");
//        }

    }

    private void setEditText() {
        if(fileExist(PRESSURE_RAW) == true){
            ArrayList<String> data =  readPressureFile(PRESSURE_RAW);
            setValEditText(data);

        }

    }

    private void setValEditText(ArrayList<String> data) {
        Boolean isRight = false;
        Boolean isLeft = false;
        String RightLastSide=null;
        String LeftLastSide=null;

        for(int i=(data.size()-1);i>=0;i--){
            for(String a:data.get(i).split("\\s")){
                if(a.equals("RIGHT") && isRight.equals(false)){
                    RightLastSide = data.get(i);
                    isRight = true;
                }else if(a.equals("LEFT") && isLeft.equals(false)){
                    LeftLastSide = data.get(i);
                    isLeft = true;

                }
            }
        }
        setText(isRight,isLeft,RightLastSide,LeftLastSide);

    }

    private void setText(Boolean isRight, Boolean isLeft, String rightLastSide, String leftLastSide) {
        if(isRight != false){
            ArrayList<String> RightPress = new ArrayList<>();
            for(String a: rightLastSide.split("\\s")){
                RightPress.add(a);
            }

            rightPressSide.setText(PressureConvert(RightPress.get(1)));


            rightPressMain.setText(PressureConvert(RightPress.get(2)));

        }
        if(isLeft != false){
            ArrayList<String> LeftPress = new ArrayList<>();

            for(String a: leftLastSide.split("\\s")){
                LeftPress.add(a);

            }

            leftPressSide.setText(PressureConvert(LeftPress.get(1)));
            leftPressMain.setText(PressureConvert(LeftPress.get(2)));
        }



    }


    private String PressureConvert(String text) {
        DecimalFormat df = new DecimalFormat("#.##");
        Integer ADC = Integer.parseInt(text,16);
        float result = (float) ((5-0)/(1.0*1023)*(ADC-(0.0*1023)));
        System.out.println("[SettingFragment] "+String.valueOf(df.format(result)));

        return String.valueOf(df.format(result));

//        (5-0)/(0.8*1023)*(ADC-(0.1*1023));
    }

    // side main leg
    public boolean fileExist(String fname){
        String path = getContext().getFilesDir().getAbsolutePath() + "/" + fname;

        File file = new File(path);
        return file.exists();
    }

    private ArrayList<String> readPressureFile(String file) {
        ArrayList<String> data = new ArrayList<>();
        FileInputStream fileIn= null;
        try {
            fileIn = getActivity().openFileInput(file);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader br = new BufferedReader(InputRead);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null){
                data.add(text);
                System.out.println("[READ FILE]" + text);
            }
            System.out.println("[READ FILE]" + data.size());

            InputRead.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setView(View view) {

        leftOffTime = view.findViewById(R.id.leftOffsetTime);
        leftPressMain = view.findViewById(R.id.leftPressureMain);
        leftPressSide=view.findViewById(R.id.leftPressureSide);



        rightOffTime=view.findViewById(R.id.rightOffsetTime);
        rightPressMain=view.findViewById(R.id.rightPressureMain);
        rightPressSide=view.findViewById(R.id.rightPressureSide);



        supineOffTime=view.findViewById(R.id.supineOffsetTime);

        startTime=view.findViewById(R.id.startBtn);
        startTime.setOnClickListener(this);

        timeStart=view.findViewById(R.id.timeStart);
        timeStart.setOnClickListener(this);

        ipAddress = getActivity().findViewById(R.id.ipBed);
        portNumber=getActivity().findViewById(R.id.port);

        if (!changed){
            keepString = defaultTime;
        }






    }

    private void setTextChange() {
//        TextChange leftPressMainChange = new TextChange(leftPressMain,testFragment,"leftPressMain");
        leftPressMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"leftPressMain","leftPressMain.txt");

            }
        });

//        TextChange leftPressSideChange = new TextChange(leftPressSide, testFragment,"leftPressSide");
        leftPressSide.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"leftPressSide","leftPressSide.txt");
            }
        });

//        TextChange rightPressMainChange = new TextChange(rightPressMain, testFragment,"rightPressMain");
        rightPressMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"rightPressMain","rightPressMain.txt");
            }
        });
//        TextChange rightPressSideChange = new TextChange(rightPressSide, testFragment,"rightPressSide");
        rightPressSide.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"rightPressSide","rightPressSide.txt");
            }
        });

//        TextChange leftOffTimeChange = new TextChange(leftOffTime, testFragment,"leftOffTime");
        leftOffTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"leftOffTime","leftOffTime.txt");
            }
        });
//        TextChange rightOffTimeChange = new TextChange(rightOffTime, testFragment,"rightOffTime");
        rightOffTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"rightOffTime","rightOffTime.txt");
            }
        });

//        TextChange supineOffTimeChange = new TextChange(supineOffTime,testFragment,"supineOffTime");
        supineOffTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                writeToFile(s.toString(),"supineOffTime","supineOffTime.txt");
            }
        });
    }



    @Override
    public void onClick(View v) {
        if(v.getId()== timeStart.getId()){

            Calendar mcurrentTime = Calendar.getInstance();
            final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;

            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    hourStop = String.format("%02d", selectedHour );
                    minStop =  String.format("%02d",selectedMinute);
                    timeStart.setText( hourStop + ":" + minStop);
                    keepString = hourStop + ":"+minStop;
                    changed = true;
                    System.out.println("[ttt]" + keepString +" "+startTime.isChecked());
                    writeToFile2(keepString, startTime.isChecked());
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();



        }else if(v.getId()==startTime.getId()){ // btn start
            System.out.println("[ttt]");



            if(startTime.isChecked() == false){
//                startTime.setText("START");
                System.out.println("checccccccckkkkk false");
                keep = false;

//                    isStart=false;
            }else {
//                startTime.setText("STOP");
                combineActivity.carryTo(startTime.isChecked());
                writeToFile2(timeStart.getText().toString(), true);
                System.out.println("checccccccckkkkk trueeee");
//                passDataToUSerFragment();
                t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            while (!isInterrupted()) {

                                Thread.sleep(1000);
//                                System.out.println("[Main] : irwpoerioephdf " + keepString);

                                if (changed){
                                    combineActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            timeStart.setText(keepString);

                                        }
                                    });
                                }


                                long timeInMillis = System.currentTimeMillis();
                                Calendar cal1 = Calendar.getInstance();
                                cal1.setTimeInMillis(timeInMillis);
                                SimpleDateFormat dateFormat = new SimpleDateFormat(
                                        "HH:mm");
                                String dateforrow = dateFormat.format(cal1.getTime());
                                String time =hourStop+":"+minStop;
                                if (time.equals(dateforrow)){
                                   timeforstop();
                                    combineActivity.passDataToActivity(true);
                                    combineActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startTime.setChecked(false);
                                    }
                                });
                                    break;
                                }
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                };
                t.start();
//                timeforstop();
//                isStart = true;


            }

        }
    }

    public void timeforstop(){
        String ip = ipAddress.getText().toString();
        Integer port = Integer.parseInt( portNumber.getText().toString());

        System.out.println("[Main] : hourrr + "+hourStop +" : "+minStop);

        CallToTCP callToTCP = new CallToTCP(ip,port,"0B 00 FF 00 0000 00 0000");
        callToTCP.setListener(new CallToTCP.CallToTCPListener() {
            @Override
            public void recMessageCallBack(String mes) {
                System.out.println(mes);
            }
        });



    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        combineActivity =(CombineActivity) getActivity();
        System.out.println("[Main] : Attttachhhhh");
        if (keep){
            startTime.setChecked(false);
            System.out.println("[Main] : falseeeeeeee");
        }
    }


    public void passDataToFragment(String time) {

        keep =  true;
        System.out.println("[Main] : passDataaaa "+keep);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("[Main] : stopppppppp");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("[Main] : starttttttt" + keep);}

    public void onResume() {
        super.onResume();
        setEditText();
        System.out.println("[SettingFragment] 55555555555555555555555555555'");
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }

    public void writeToFile2(String s,boolean check){
        FileOutputStream file1 = null;
        String data = s.toString()+" "+check;
        try {
            file1 = getActivity().openFileOutput("time.txt", MODE_PRIVATE);
            file1.write(data.getBytes());
            file1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//        if (isVisibleToUser) {
//            System.out.println("passdata"+isVisibleToUser);
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }
    public void writeToFile(String s,String s1,String file){
        FileOutputStream fos = null;
        String data = s.toString()+" "+s1+ "\n";
        try {
            fos = getActivity().openFileOutput(file, MODE_APPEND);
            fos.write(data.getBytes());
        }
        catch (Throwable t) {

        }
    }
    public void passdata(boolean bool){
        FragmentManager manager ;
        manager = getFragmentManager();

        if(bool){System.out.println("passdata"+bool);
            manager.beginTransaction().detach(this).attach(this).commitNow();
        }
    }


}
