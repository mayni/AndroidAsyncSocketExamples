package com.github.reneweb.androidasyncsocketexamples;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserFragment extends Fragment {
    FragmentManager manager;
    UserLogsFragment fragmentUserLogs = new UserLogsFragment();
    CalibrateFragment fragmentCalibrate = new CalibrateFragment();


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager = getChildFragmentManager();
        manager.beginTransaction().add(R.id.FRAGMENT_PLACEHOLDER, fragmentUserLogs,"fragmentUserLogs").commit();

        Bundle arguments = new Bundle();
        arguments.putString("PAGE","USER");
        fragmentCalibrate.setArguments(arguments);

        fragmentUserLogs.setListener(new UserLogsFragment.CalibratePressOnListener() {
            @Override
            public void isCalibratePressOn(boolean bool) {
                if(bool == true){
                    manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER,fragmentCalibrate,"fragmentCalibrate").addToBackStack("fragmentUserLogs").commit();
                }
            }
        });
        fragmentCalibrate.setListener(new CalibrateFragment.BackToTestListener() {
            @Override
            public void PressBackButton(boolean bool) {
                if(bool == true){
                    manager.beginTransaction().replace(R.id.FRAGMENT_PLACEHOLDER,fragmentUserLogs,"fragmentUserLogs").addToBackStack("fragmentCalibrate").commit();
                }
            }
        });

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

}
