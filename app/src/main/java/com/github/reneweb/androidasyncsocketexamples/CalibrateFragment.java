package com.github.reneweb.androidasyncsocketexamples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DecimalFormat;

import static android.content.Context.SENSOR_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;


public class CalibrateFragment extends Fragment implements View.OnClickListener {
    SensorManager sensorManager;
    TextView ySensor , angle ;
    Integer angleCalibrate = 30;
    Sensor sensor;
    Button startCalibrate,changeAngle,cancelCalibrate,calibrateRight,calibrateLeft;
    boolean isCalibrate = false;
    String NOTES = "Pressure.txt";
    String valuePage;

    private String mParam1;
    private String mParam2;

    Toolbar toolbar;



    public  BackToTestListener listener;

    public CalibrateFragment() {
        // Required empty public constructor
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             valuePage = bundle.getString("PAGE", "0");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate,container,false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Calibrate");
        toolbar.setNavigationOnClickListener(this);

        ySensor = view.findViewById(R.id.yPo);
        startCalibrate = view.findViewById(R.id.startcalibrate);
        angle = view.findViewById(R.id.calibrate_angle);
        changeAngle = view.findViewById(R.id.change_angle);
        cancelCalibrate = view.findViewById(R.id.cancelcalibrate);

        calibrateLeft = view.findViewById(R.id.calibrate_left);
        calibrateRight = view.findViewById(R.id.calibrate_right);

        if(valuePage == "USER"){
            System.out.println("USERRRRRRRRRRRRRRRRRRRR");
            calibrateLeft.setVisibility(view.VISIBLE);
            calibrateRight.setVisibility(view.VISIBLE);

        }
        cancelCalibrate.setEnabled(false);
        angle.setText(angleCalibrate.toString());
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        startCalibrate.setOnClickListener(this);
        changeAngle.setOnClickListener(this);
        cancelCalibrate.setOnClickListener(this);

        return view;
    }


    public SensorEventListener sensorListener  =  new SensorEventListener (){

        @Override
        public void onSensorChanged(final SensorEvent sensorEvent) {
            DecimalFormat df = new DecimalFormat("#");
            ySensor.setText(String.valueOf(df.format(sensorEvent.values[1])));

            if(isCalibrate == true){
                System.out.println("[calibrate]" + sensorEvent.values[1] + " " + sensorEvent.values[2]);
                if(sensorEvent.values[1] > -(angleCalibrate+1) && sensorEvent.values[1] < -(angleCalibrate-1)){
                    startCalibrate.setEnabled(true);
                    cancelCalibrate.setEnabled(false);
                    changeAngle.setEnabled(true);
//                    getPressure();
                    isCalibrate = false;
                }
                if(sensorEvent.values[1] > (angleCalibrate+1) && sensorEvent.values[1] < (angleCalibrate-1)){
                    startCalibrate.setEnabled(true);
                    cancelCalibrate.setEnabled(false);
                    changeAngle.setEnabled(true);
//                    getPressure();
                    isCalibrate = false;
                }
            }

        }



        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListener);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() ==startCalibrate.getId()){
            System.out.println("Angle"+angleCalibrate);
            this.isCalibrate = true;
            v.setEnabled(false);
            cancelCalibrate.setEnabled(true);
            changeAngle.setEnabled(false);

        }else if(v.getId() == cancelCalibrate.getId()){
            this.isCalibrate = false;
            startCalibrate.setEnabled(true);
            changeAngle.setEnabled(true);
            v.setEnabled(false);

        }else if(v.getId() == changeAngle.getId()){
            changeAngleDialog();
        }else {
            listener.PressBackButton(true);
        }
    }

    private void changeAngleDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View theView = inflater.inflate(R.layout.angle_picker, null);
        NumberPicker numberPicker = theView.findViewById(R.id.angle_picker);

        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(0);
        numberPicker.setValue(angleCalibrate);

        final Integer[] newAngle = {angleCalibrate};

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newAngle[0] = newVal;
            }
        });


        builder.setTitle("Change Angle");
        builder.setView(theView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                angleCalibrate = newAngle[0];
                angle.setText(angleCalibrate.toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL",null);
        builder.create();
        builder.show();
    }
}
