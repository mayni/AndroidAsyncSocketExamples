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

import java.util.Locale;

public class

Main2Activity extends AppCompatActivity {

    private Button mButtonTerminate;
    private Button mButtonStart,backto;
    private EditText mEditTextInput,mEditTextInputHour;
    private Button mButtonSet;
    private TextView tvCountdown;

    private CountDownTimer mCountDownTimer;

    private long mStartTimeInMillis;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        mEditTextInput = findViewById(R.id.edit_text_input);
//        mEditTextInputHour = findViewById(R.id.edit_text_input_hour);
        mButtonSet = findViewById(R.id.set_btn);
        tvCountdown = findViewById(R.id.countdown_time);
        mButtonStart = findViewById(R.id.countdown_btn);
        mButtonTerminate = findViewById(R.id.emergency_btn);
        backto = findViewById(R.id.back_btn);
//        getIncomingIntent();
        initView();

        System.out.println("5555555555----"+ getIntent().getStringExtra("time"));

        String input = getIntent().getStringExtra("time");
//                if (input.length() == 0) {
//                    Toast.makeText(Main2Activity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }
        tvCountdown.setText(input);
        long millisInput = Long.parseLong(input) * 60000;
//                if (millisInput == 0) {
//                    Toast.makeText(Main2Activity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
//                    return;
//                }

        setTime(millisInput);

        updateCountDownText();

    }

    private void initView() {
//        mButtonSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String input = getIntent().getStringExtra("time");
////                if (input.length() == 0) {
////                    Toast.makeText(Main2Activity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
////                    return;
////                }
//
//                long millisInput = Long.parseLong(input) * 60000;
////                if (millisInput == 0) {
////                    Toast.makeText(Main2Activity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
////                    return;
////                }
//
//                setTime(millisInput);
//                mEditTextInput.setText("");
//            }
//        });
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
//        closeKeyboard();
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



            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonTerminate.setVisibility(View.VISIBLE);
            }
// else {
////                mButtonTerminate.setVisibility(View.INVISIBLE);
//            }
        }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        System.out.println("555555555555555555555");
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume(){
        super.onResume();
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//
//        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
//        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
//        mTimerRunning = prefs.getBoolean("timerRunning", false);
//
//        updateCountDownText();
//        updateWatchInterface();
//
//        if (mTimerRunning) {
//            mEndTime = prefs.getLong("endTime", 0);
//            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
//
//            if (mTimeLeftInMillis < 0) {
//                mTimeLeftInMillis = 0;
//                mTimerRunning = false;
//                updateCountDownText();
//                updateWatchInterface();
//            } else {
//                startTimer();
//            }
//        }
    }




}
