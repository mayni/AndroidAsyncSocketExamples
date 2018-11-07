package com.github.reneweb.androidasyncsocketexamples;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private Button mButtonStart;

    private Button mButtonTerminate;
    private TextView tvCountdown;

    private CountDownTimer mCountDownTimer;
    private long mStartTimeInMillis;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mButtonStart = findViewById(R.id.countdown_btn);
        tvCountdown = findViewById(R.id.countdown_time);
        mButtonTerminate = findViewById(R.id.emergency_btn);

        initView();
        updateCountDownText();


    }

    private void updateCountDownText() {

    }

    private void initView() {

    }




}
