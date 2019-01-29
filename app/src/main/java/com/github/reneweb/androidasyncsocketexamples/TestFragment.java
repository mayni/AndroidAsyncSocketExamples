package com.github.reneweb.androidasyncsocketexamples;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileOutputStream;
import java.util.Set;

import static android.content.Context.MODE_APPEND;

public class TestFragment extends Fragment implements View.OnClickListener {

    FragmentManager manager;
    public String value;
    TestLogsFragment fragmentTest = new TestLogsFragment();
    TestDirectFragment fragmentDirect = new TestDirectFragment();
    PressureFragment fragmentPressure = new PressureFragment();
    CalibrateFragment fragmentCalibrate = new CalibrateFragment();
    SettingFragment settingFragment = new SettingFragment();



    Bundle arguments = new Bundle();
    Bundle arguments2 = new Bundle();


    public String passdata(String s, String s1){
//        fragmentTest.passdata(s,s1);
        System.out.println("[ttt]"+s);
        return s;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager = getChildFragmentManager();

        manager.beginTransaction().add(R.id.FRAGMENT_PLACEHOLDER,fragmentTest,"fragmentTest").commit();

        arguments.putString("PAGE","TEST");
        fragmentCalibrate.setArguments(arguments);


       System.out.println("[kkk]" + value);



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

            @Override
            public void Reload(boolean bool) {
                if(bool == true){
                    manager.beginTransaction().detach(fragmentDirect).attach(fragmentDirect).commit();
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


    @Override
    public void onClick(View v) {

    }


}
