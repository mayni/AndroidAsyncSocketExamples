package com.github.reneweb.androidasyncsocketexamples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.github.reneweb.androidasyncsocketexamples.R;
import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserLogsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserLogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLogsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button rightwork,leftwork,bothwork,emergencywork,calibratework;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserLogsFragment() {
        // Required empty public constructor
    }

    public static UserLogsFragment newInstance(String param1, String param2) {
        UserLogsFragment fragment = new UserLogsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_logs,container,false);
        setView(view);
        setOnClick();
        setSending();
        return view;
    }

    private void setSending() {

    }

    private void setOnClick() {
        rightwork.setOnClickListener(this);
        leftwork.setOnClickListener(this);
        bothwork.setOnClickListener(this);
        calibratework.setOnClickListener(this);
        emergencywork.setOnClickListener(this);
    }

    private void setView(View view) {
        rightwork = view.findViewById(R.id.rightwork);
        leftwork = view.findViewById(R.id.leftwork);
        bothwork = view.findViewById(R.id.bothwork);
        calibratework = view.findViewById(R.id.calibrate_angle);
        emergencywork = view.findViewById(R.id.emergency);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == rightwork.getId() || v.getId() == leftwork.getId() || v.getId() == bothwork.getId() ){
            dialogSetting();
        }else if(v.getId() == calibratework.getId()){

        }else if(v.getId() == emergencywork.getId()){

        }
    }

    private void dialogSetting() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View theView = inflater.inflate(R.layout.time_picker, null);


        NumberPicker repeat = theView.findViewById(R.id.repeat);
        NumberPicker time = theView.findViewById(R.id.time1);
        NumberPicker time1 = theView.findViewById(R.id.time2);

        time.setMaxValue(2);
        time.setMinValue(0);


        time1.setMaxValue(59);
        time1.setMinValue(0);

        repeat.setMaxValue(20);
        repeat.setMinValue(1);



        time.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
//                hourTime = newVal;
            }
        });
        time1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
//                minuteTime = newVal;
            }
        });
//        builder.setTitle("Set time");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setView(theView);


        builder.setNegativeButton("CANCEL",null);
        builder.create();
        builder.show();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
