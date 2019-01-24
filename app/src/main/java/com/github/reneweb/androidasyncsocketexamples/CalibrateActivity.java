package com.github.reneweb.androidasyncsocketexamples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CalibrateActivity extends AppCompatActivity implements View.OnClickListener {
    SensorManager sensorManager;
    TextView xSensor , ySensor , zSensor , angle ;
    Integer angleCalibrate = 30;
    Sensor sensor;
    Button startCalibrate,changeAngle,cancelCalibrate,calibrateRight,calibrateLeft;
    boolean isCalibrate = false;
    String NOTES = "Pressure.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        xSensor = findViewById(R.id.xPo);
        ySensor = findViewById(R.id.yPo);
        zSensor =findViewById(R.id.zPo);
        startCalibrate = findViewById(R.id.startcalibrate);
        angle = findViewById(R.id.calibrate_angle);
        changeAngle = findViewById(R.id.change_angle);
        cancelCalibrate = findViewById(R.id.cancelcalibrate);
        calibrateLeft = findViewById(R.id.calibrate_left);
        calibrateRight = findViewById(R.id.calibrate_right);


        cancelCalibrate.setEnabled(false);
        angle.setText(angleCalibrate.toString());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        startCalibrate.setOnClickListener(this);
        changeAngle.setOnClickListener(this);
        cancelCalibrate.setOnClickListener(this);
        calibrateRight.setOnClickListener(this);
        calibrateLeft.setOnClickListener(this);



    }
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListener);
    }
    public SensorEventListener sensorListener  =  new SensorEventListener (){

        @Override
        public void onSensorChanged(final SensorEvent sensorEvent) {
            DecimalFormat df = new DecimalFormat("#");

            xSensor.setText("X :     " + String.valueOf(df.format(sensorEvent.values[0])));
            ySensor.setText("Y :     " + String.valueOf(df.format(sensorEvent.values[1])));
            zSensor.setText("Z :     " + String.valueOf(df.format(sensorEvent.values[2])));

            if(isCalibrate == true){
                System.out.println("[calibrate]" + sensorEvent.values[1] + " " + sensorEvent.values[2]);
                if(sensorEvent.values[1] > -(angleCalibrate+1) && sensorEvent.values[1] < -(angleCalibrate-1)){
                    startCalibrate.setEnabled(true);
                    cancelCalibrate.setEnabled(false);
                    changeAngle.setEnabled(true);
                    getPressure();
                    isCalibrate = false;
                }
                if(sensorEvent.values[1] > (angleCalibrate+1) && sensorEvent.values[1] < (angleCalibrate-1)){
                    startCalibrate.setEnabled(true);
                    cancelCalibrate.setEnabled(false);
                    changeAngle.setEnabled(true);
                    getPressure();
                    isCalibrate = false;
                }

            }


//            side , main , leg
        }



        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId() == startCalibrate.getId()){
            System.out.println("Angle"+angleCalibrate);
            this.isCalibrate = true;
            v.setEnabled(false);
            cancelCalibrate.setEnabled(true);
            changeAngle.setEnabled(false);
        }else if(v.getId() == changeAngle.getId()){
            changeAngleDialog();
        }else if(v.getId() == cancelCalibrate.getId()){
            this.isCalibrate = false;
            startCalibrate.setEnabled(true);
            changeAngle.setEnabled(true);
            v.setEnabled(false);

        }else if(v.getId() == calibrateLeft.getId() || v.getId() == calibrateRight.getId()){
            QuickCalibrate(v.getId());

        }
    }

    private void QuickCalibrate(int id) {
        String message = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        if(id == calibrateLeft.getId()){
            builder.setMessage("02 63 FF");
            message = "02 63 FF";
        }else if(id == calibrateRight.getId()){
            builder.setMessage("02 93 FF");
            message = "02 93 FF";
        }
        final String finalMessage = message;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        System.out.println("[String]"+finalMessage);
                        Client client = new Client("10.0.0.177",12345,finalMessage);
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
                cancelCalibrate.setEnabled(true);
                changeAngle.setEnabled(false);
            }

        });
        builder.setNegativeButton("CANCEL",null);
        builder.create().show();
    }

    private void changeAngleDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void getPressure(){

        new AsyncTask<Void,Void,Void>(){
            String pressure = null;
            Exception error = null;
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Client client = new Client("10.0.0.177",12345,"01");
                    Thread.sleep(32000);
                    client.setListener( new Client.clientMessageRecListener() {

                        @Override
                        public void checkConnection(Exception e) {

                            error = e;
                            if(error != null){
                                final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                System.out.println("Calibrate checkConnection " + e) ;
                                writeTofile(dateFormat.format(dateFormat)+" Error "+e);

                            }

                        }
                        @Override
                        public void checkWifi(Exception e) {
    
                        }
                        
                        @Override
                        public void recMessage(String mes) {
                            System.out.println("Calibrate recMessage " + mes) ;
                            pressure =mes;
                            if(pressure != null){
                                writeTofile("Pressure " + pressure );
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                System.out.println("onPostExecutezz");

            }
        }.execute();

//        Toast.makeText(this,"Completed saved pressure",Toast.LENGTH_LONG).show();



    }

    private void writeTofile(String dat) {
        FileOutputStream fos = null;
        String data = dat + "\n";
        try {
            fos = openFileOutput(NOTES, MODE_APPEND);
            fos.write(data.getBytes());

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + NOTES,
                    Toast.LENGTH_LONG).show();
        }
        catch (Throwable t) {

        }
    }

}
