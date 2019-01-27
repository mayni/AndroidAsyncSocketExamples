package com.github.reneweb.androidasyncsocketexamples;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import java.time.LocalTime;
import java.util.Calendar;
import java.util.zip.Inflater;


public class SettingFragment extends Fragment implements View.OnClickListener {
    Button startTime;
    EditText leftPressSide,leftPressMain,leftOffTime,rightPressSide,rightPressMain,rightOffTime,supineOffTime;
    TextView timeStart;
    EditText ipAddress,portNumber;
    Boolean isStart = false;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setView(view);
        return view;
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
