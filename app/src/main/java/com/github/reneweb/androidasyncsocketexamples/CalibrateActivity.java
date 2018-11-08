package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CalibrateActivity extends AppCompatActivity {
    SensorManager sensorManager;
    TextView xSensor , ySensor , zSensor;
    Sensor sensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        xSensor = findViewById(R.id.xPo);
        ySensor = findViewById(R.id.yPo);
        zSensor =findViewById(R.id.zPo);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
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
        public void onSensorChanged(SensorEvent sensorEvent) {
            DecimalFormat df = new DecimalFormat("#.##");

            xSensor.setText("X : " + String.valueOf(df.format(sensorEvent.values[0])));
            ySensor.setText("Y :" + String.valueOf(df.format(sensorEvent.values[1])));
            zSensor.setText("Z :" + String.valueOf(df.format(sensorEvent.values[2])));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

}
