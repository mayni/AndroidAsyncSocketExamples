package com.github.reneweb.androidasyncsocketexamples;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    private Button mButtonTerminate;
    private Button mButtonStart,backto;

    private Button mButtonSet;
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

        mButtonSet = findViewById(R.id.set_btn);
        tvCountdown = findViewById(R.id.countdown_time);
        mButtonStart = findViewById(R.id.countdown_btn);
        mButtonTerminate = findViewById(R.id.emergency_btn);
        backto = findViewById(R.id.back_btn);
        initView();
        updateCountDownText();
    }

    private void initView() {
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().hasExtra("time")){
                    String input = getIntent().getStringExtra("time");
                    long millisInput = Long.parseLong(input) * 60000;
                    setTime(millisInput);
                }

            }
        });
        mButtonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }


        });

        mButtonTerminate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pauseTimer();
                resetTimer();
            }
        });

        backto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                intent.putExtra("closed","1");
                startActivity(intent);
            }
        });

    }

//    private void getIncomingIntent() {
//        if(getIntent().hasExtra("name") && getIntent().hasExtra("ip_Address") ){
//            tv_name.setText(getIntent().getStringExtra("name"));
//            tv_ip.setText(getIntent().getStringExtra("ip_Address"));
//        }
//        if (getIntent().hasExtra("hour") && getIntent().hasExtra("minute")
//                ){
//            System.out.println("Time receive : "+ getIntent().getStringExtra("hour")+":"
//                    +getIntent().getStringExtra("minute")+" repeat : "+
//                    getIntent().getStringExtra("repeat"));
//        }
//
//    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();

    }


    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        System.out.println("hour  " + hours);

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        tvCountdown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonStart.setText("Pause");
        } else {
            mButtonStart.setText("Start");
            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonTerminate.setVisibility(View.VISIBLE);
            }
        }
        }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }




}
