package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TestFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Boolean isDirectControlOnClick = false;
    FragmentManager manager;

    TestLogsFragment fragmentTest = new TestLogsFragment ();
    TestDirectFragment fragmentDirect = new TestDirectFragment();
    PressureFragment fragmentPressure = new PressureFragment();
    CalibrateFragment fragmentCalibrate = new CalibrateFragment();

    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
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
        manager = getChildFragmentManager();
        manager.beginTransaction().add(R.id.FRAGMENT_PLACEHOLDER, fragmentTest,"fragmentTest").commit();

        fragmentTest.setListener(new TestLogsFragment.DirectControllistener() {
            @Override
            public void DirectControlOnclick(boolean bool,String string) {
                if(bool == true){
                    if(string == "direct"){
                        manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER,fragmentDirect,"fragmentDirect").addToBackStack("fragmentTest").commit();
                    }else if(string == "pressure"){
                        manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER,fragmentPressure,"fragmentPressure").addToBackStack("fragmentTest").commit();
                    }else if(string == "calibrate"){
                        manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER,fragmentCalibrate,"fragmentCalibrate").addToBackStack("fragmentTest").commit();
                    }

                }
            }
        });
        fragmentDirect.setListener(new TestDirectFragment.BackToTestListener() {
            @Override
            public void PressBackButton(boolean bool) {
                if(bool == true){
                    manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER, fragmentTest,"fragmentTest").addToBackStack("fragmentDirect").commit();
                }
            }
        });
        fragmentPressure.setListener(new PressureFragment.BackToTestListener() {
            @Override
            public void PressBackButton(boolean bool) {
                if(bool == true){
                    manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER, fragmentTest,"fragmentTest").addToBackStack("fragmentPressure").commit();
                }

            }
        });
        fragmentCalibrate.setListener(new CalibrateFragment.BackToTestListener() {
            @Override
            public void PressBackButton(boolean bool) {
                if(bool == true){
                    manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER, fragmentTest,"fragmentTest").addToBackStack("fragmentCalibrate").commit();
                }
            }
        });




        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == fragmentTest.directcontrol.getId()){
            System.out.println("onClickkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        }
    }

}
