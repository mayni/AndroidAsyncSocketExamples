package com.github.reneweb.androidasyncsocketexamples;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.github.reneweb.androidasyncsocketexamples.call.CallToTCP;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.SENSOR_SERVICE;


public class CalibrateFragment extends Fragment implements View.OnClickListener {
    SensorManager sensorManager;
    TextView ySensor , angle ;
    Integer angleCalibrate = 30;
    Sensor sensor;
    Button startCalibrate,changeAngle, stopCalibrate,calibrateRight,calibrateLeft;
    ProgressDialog dialogLoading;
    boolean isCalibrate = false;
    Boolean isGetPressure = false;
    String NOTES = "Pressure.txt";
    String NOTES_RAW = "PressureRaw.txt";
    String valuePage;

    String[] senserValue = new String[3];
    EditText ipAddress,portNumber;
    TextView status;


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
        View view1 =inflater.inflate(R.layout.activity_combine,container,false);

        toolbar =  view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Calibrate");
        toolbar.setNavigationOnClickListener(this);

        ySensor = view.findViewById(R.id.yPo);
        startCalibrate = view.findViewById(R.id.startcalibrate);
        angle = view.findViewById(R.id.calibrate_angle);
        changeAngle = view.findViewById(R.id.change_angle);
        stopCalibrate = view.findViewById(R.id.cancelcalibrate);

        calibrateLeft = view.findViewById(R.id.calibrate_left);
        calibrateRight = view.findViewById(R.id.calibrate_right);

        ipAddress = getActivity().findViewById(R.id.ipBed);
        portNumber = getActivity().findViewById(R.id.port);

        status = view1.findViewById(R.id.statusBed);

        if(valuePage == "USER"){
            System.out.println("USERRRRRRRRRRRRRRRRRRRR");
            calibrateLeft.setVisibility(view.VISIBLE);
            calibrateRight.setVisibility(view.VISIBLE);

        }
        stopCalibrate.setEnabled(false);
        angle.setText(angleCalibrate.toString());
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        startCalibrate.setOnClickListener(this);
        changeAngle.setOnClickListener(this);
        stopCalibrate.setOnClickListener(this);
        calibrateLeft.setOnClickListener(this);
        calibrateRight.setOnClickListener(this);

        return view;
    }

    private void QuickCalibrate(int id) {
        String message = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        if(id == calibrateLeft.getId()){
            builder.setMessage("Calibrate Left");

            message = "02 63 FF";
        }else if(id == calibrateRight.getId()){
            builder.setMessage("Calibrate Right");
            message = "02 93 FF";
        }
        final String finalMessage = message;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String ip = ipAddress.getText().toString();
                final Integer port = Integer.parseInt(portNumber.getText().toString());
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        System.out.println("[String]"+finalMessage);
                        Client client = new Client(ip,port,finalMessage);
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
                isCalibrate = true;
                startCalibrate.setEnabled(false);
                stopCalibrate.setEnabled(true);
                changeAngle.setEnabled(false);
            }

        });
        builder.setNegativeButton("CANCEL",null);
        builder.create().show();
    }

    public SensorEventListener sensorListener  =  new SensorEventListener (){

        @Override
        public void onSensorChanged(final SensorEvent sensorEvent) {
            DecimalFormat df = new DecimalFormat("#");
            ySensor.setText(String.valueOf(df.format(sensorEvent.values[1])));
            senserValue[0] = String.valueOf(sensorEvent.values[0]);
            senserValue[1] = String.valueOf(sensorEvent.values[1]);
            senserValue[2] = String.valueOf(sensorEvent.values[2]);


            if(isCalibrate == true){
                System.out.println("[calibrate]" +sensorEvent.values[0]+" "  + sensorEvent.values[1] + " " + sensorEvent.values[2]);
                if(sensorEvent.values[1] > -(angleCalibrate+1) && sensorEvent.values[1] < -(angleCalibrate-1)){
                    startCalibrate.setEnabled(true);
                    stopCalibrate.setEnabled(false);
                    changeAngle.setEnabled(true);
                    PleaseWaitDialog();
                    checkSide("START");

//                    getPressure("START");
                    isCalibrate = false;
                }
                if(sensorEvent.values[1] < (angleCalibrate+1) && sensorEvent.values[1] > (angleCalibrate-1)){
                    System.out.println("eeeeeeeeee");
                    startCalibrate.setEnabled(true);
                    stopCalibrate.setEnabled(false);
                    changeAngle.setEnabled(true);
                    PleaseWaitDialog();
                    checkSide("START");
//                    getPressure("START");
                    isCalibrate = false;
                }
            }

        }



        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private void checkSide(final String from){
        final String ip = ipAddress.getText().toString();
        final Integer port = Integer.parseInt(portNumber.getText().toString());
        CallToTCP callToTCP=new CallToTCP(ip,port,"02 0C 0E");
        callToTCP.setListener(new CallToTCP.CallToTCPListener() {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            @Override
            public void recMessageCallBack(String mes) {
                if(mes.charAt(0) != '['){
                    ArrayList<String> data = new ArrayList<>();
                    for(String a:mes.split("\\s")){
                        data.add(a);
                    }

                    if(data.get(1).equals("60") || data.get(1).equals("63")){
                        System.out.println("[CalibrateFragment] 60" );
                        getPressure(from,"LEFT");

                    }else if(data.get(1).equals("90") || data.get(1).equals("93")){
                        System.out.println("[CalibrateFragment] 90");
                        getPressure(from,"RIGHT");
                    }else{
                        AlertForTryAgain("Please check your pump or valve before calibrate");
                    }
                    System.out.println("[CalibrateFragment]"+ data.get(1) );
                }else{
                    writeTofile(formatter.format(date).toString()+" Error "+mes,NOTES);
                    String tryagain = "Can't get pressure please try again";
                    AlertForTryAgain(tryagain);
                }

            }
        });
    }
    public void getPressure(final String from, final String side){

        final String ip = ipAddress.getText().toString();
        final Integer port = Integer.parseInt(portNumber.getText().toString());
        CallToTCP callToTCP = new CallToTCP(ip,port,"01");
        callToTCP.setListener(new CallToTCP.CallToTCPListener() {
            @Override
            public void recMessageCallBack(final String mes) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                if(mes.charAt(0) != '['){
                    if(mes != null){
                        if(from=="START"){
                            for(String a: mes.split("\\s")){
                                System.out.println(a);
                            }
                            writeTofile(mes +" "+angle.getText().toString()+" "+side,NOTES_RAW);
                            writeTofile(formatter.format(date).toString()+" Pressure " + mes +" "+angle.getText().toString()+" "+side,NOTES);
                        }else if(from=="STOP"){
                            String senserVal = senserValue[1];
                            if(senserVal.charAt(0) == '-'){
                                senserVal = senserVal.substring(1);
                            }
                            writeTofile(mes +" "+senserVal+" "+side,NOTES_RAW);
                            writeTofile(formatter.format(date).toString()+" Pressure " + mes +" "+senserVal+" "+side,NOTES);
                        }

                    }
                    String complete = "Completed";
                    AlertForTryAgain(complete);
                }else{
                    writeTofile(formatter.format(date).toString()+" Error "+mes,NOTES);
                    String tryagain = "Can't get pressure please try again";
                    AlertForTryAgain(tryagain);
                }

            }
        });
//        new AsyncTask<Void,Void,Void>(){
//            String pressure = null;
//            Exception error = null;
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                try {
//                    Client client = new Client(ip,port,"01");
////                    Thread.sleep(32000);
//                    client.setListener( new Client.clientMessageRecListener() {
//
//                        @Override
//                        public void checkConnection(Exception e) {
//                            error = e;
//                            if(error != null){
//                                final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//                                System.out.println("Calibrate checkConnection " + e) ;
//
//                            }
//                        }
//
//                        @Override
//                        public void checkWifi(Exception e) {
//
//                        }
//
//                        @Override
//                        public void recMessage(String mes) {
//                            System.out.println("Calibrate recMessage " + mes) ;
//                            pressure =mes;
//                            if(pressure != null){
//                                writeTofile("Pressure " + pressure );
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                System.out.println("onPostExecutezz");
//
//            }
//        }.execute();

//        Toast.makeText(this,"Completed saved pressure",Toast.LENGTH_LONG).show();



    }
    private void PleaseWaitDialog(){

        dialogLoading = new ProgressDialog(getActivity());
        dialogLoading.setMessage("Please Wait");
        dialogLoading.setCancelable(true);
        dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogLoading.show();
    }
    private void AlertForTryAgain(final String mes){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogLoading.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mes);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
    }
    private void writeTofile(String dat,String file) {
        FileOutputStream fos = null;
        String data = dat + "\n";
        try {
            fos = getActivity().openFileOutput(file, MODE_APPEND);
            fos.write(data.getBytes());

            Toast.makeText(getActivity(), "Saved to " + getContext().getFilesDir().getAbsolutePath() + "/" + NOTES, Toast.LENGTH_LONG).show();
        }
        catch (Throwable t) {

        }
    }

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
            stopCalibrate.setEnabled(true);
            changeAngle.setEnabled(false);

        }else if(v.getId() == stopCalibrate.getId()){
            this.isCalibrate = false;
            startCalibrate.setEnabled(true);
            changeAngle.setEnabled(true);
            v.setEnabled(false);
            PleaseWaitDialog();
            checkSide("STOP");

        }else if(v.getId() == changeAngle.getId()) {
            changeAngleDialog();
        }else if(v.getId() == calibrateRight.getId() || v.getId() == calibrateLeft.getId()){
            QuickCalibrate(v.getId());
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
