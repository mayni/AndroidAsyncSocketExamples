package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

public class CalibrateActivity extends AppCompatActivity implements View.OnClickListener {
    SensorManager sensorManager;
    TextView xSensor , ySensor , zSensor;
    Sensor sensor;
    Button startCalibrate;
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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        startCalibrate.setOnClickListener(this);



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
                if(sensorEvent.values[1] > -32 && sensorEvent.values[1] < -30){
                    startCalibrate.setEnabled(true);
                    getPressure();
                    isCalibrate = false;
                }
                if(sensorEvent.values[1] > 30 && sensorEvent.values[1] < 32){
                    startCalibrate.setEnabled(true);
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
        this.isCalibrate = true;
        v.setEnabled(false);
    }
    public void getPressure(){
        final String[] pressure = {""};
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Client client = new Client("10.0.0.177",12345,"01");
                client.setListener(new Client.clientMessageRecListener() {
                    @Override
                    public void recMessage(String mes) {
                        System.out.println("Calibrate" + mes) ;
                        pressure[0] =mes;
                        writeTofile(pressure[0]);

                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                System.out.println("[Calibrate] END");


            }
        }.execute();
        Toast.makeText(this,"Completed saved pressure",Toast.LENGTH_LONG).show();


    }

    private void writeTofile(String data) {
        try {
            OutputStreamWriter out=
                    new OutputStreamWriter(openFileOutput(NOTES, 0));

            out.write(data);
            out.close();


        }
        catch (Throwable t) {
            Toast
                    .makeText(this, "Exception: "+t.toString(), Toast.LENGTH_SHORT)
                    .show();
        }

    }

}
