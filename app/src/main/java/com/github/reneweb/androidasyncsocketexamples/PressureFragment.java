package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.reneweb.androidasyncsocketexamples.tcp.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PressureFragment extends Fragment implements View.OnClickListener {
    Thread thread;
    private RecyclerView pressureRecycle;
    private PressureAdapter pressureAdapter;
    private List<Pressure> pressList;

    public BackToTestListener listener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PressureFragment() {

    }
    public interface BackToTestListener{
        void PressBackButton(boolean bool);
    }
    public void setListener(BackToTestListener listener) {
        this.listener = listener;
    }

    public static PressureFragment newInstance(String param1, String param2) {
        PressureFragment fragment = new PressureFragment();
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
        View view = inflater.inflate(R.layout.fragment_pressure,container,false);
        pressureRecycle = view.findViewById(R.id.pressureRecycle);
        pressList = new ArrayList<>();

        pressureRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pressureAdapter = new PressureAdapter();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Read Pressure");
        toolbar.setNavigationOnClickListener(this);

        getPressure();

        return view;
    }
    public void getPressure(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1200);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncTask<Void,Void,Void>(){


                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        Client client = new Client("10.80.67.104",12345,"01");
                                        client.setListener(new Client.clientMessageRecListener() {
                                            @Override
                                            public void recMessage(final String mes) {
                                                System.out.println("[Main] time" + mes);
                                                new AsyncTask<Void,Void,String>(){

                                                    @Override
                                                    protected String doInBackground(Void... voids) {
                                                        return mes.trim();
                                                    }

                                                    @Override
                                                    protected void onPostExecute(String message) {
                                                        super.onPostExecute(message);
                                                        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                                        Date date = new Date();
                                                        pressList.add(0,new Pressure(message,dateFormat.format(date)));
                                                        pressureAdapter.setPressures(pressList);
                                                        pressureRecycle.setAdapter(pressureAdapter);
                                                    }
                                                }.execute();


                                            }

                                            @Override
                                            public void checkConnection(Exception e) {


                                            }
                                        });
                                        return null;
                                    }

                                }.execute();


                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        listener.PressBackButton(true);
        thread.interrupt();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
