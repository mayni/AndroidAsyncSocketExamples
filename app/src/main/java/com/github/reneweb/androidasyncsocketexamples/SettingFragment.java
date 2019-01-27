package com.github.reneweb.androidasyncsocketexamples;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.reneweb.androidasyncsocketexamples.call.CallToTCP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;


public class SettingFragment extends Fragment implements View.OnClickListener {
    Button startTime;
    EditText leftPressSide,leftPressMain,leftOffTime,rightPressSide,rightPressMain,rightOffTime,supineOffTime;
    TextView timeStart;
    EditText ipAddress,portNumber;
    String PRESSURE_RAW="PressureRaw.txt";
    Boolean isStart = false;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setView(view);
        setEditText();
        return view;
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
        System.out.println("[setValEditText] "+ RightLastSide);
        System.out.println("[setValEditText] "+ LeftLastSide);
        ArrayList<String> RightPress = new ArrayList<>();
        ArrayList<String> LeftPress = new ArrayList<>();
        for(String a: RightLastSide.split("\\s")){
            RightPress.add(a);
        }
        for(String a: LeftLastSide.split("\\s")){
            LeftPress.add(a);
        }
        leftPressSide.setText(LeftPress.get(1));
        leftPressMain.setText(LeftPress.get(2));

        rightPressSide.setText(RightPress.get(1));
        rightPressMain.setText(RightPress.get(2));

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


    private void setView(View view) {
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

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==timeStart.getId()){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;

            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    timeStart.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }else if(v.getId()==startTime.getId()){
            String ip = ipAddress.getText().toString();
            Integer port = Integer.parseInt( portNumber.getText().toString());

            if(isStart == true){

                CallToTCP callToTCP = new CallToTCP(ip,port,"EE");
                callToTCP.setListener(new CallToTCP.CallToTCPListener() {
                    @Override
                    public void recMessageCallBack(String mes) {
                        System.out.println(mes);
                    }
                });
                startTime.setText("START");
                isStart=false;

            }else if(isStart == false){
                startTime.setText("STOP");
                isStart = true;

            }

        }

    }
}
