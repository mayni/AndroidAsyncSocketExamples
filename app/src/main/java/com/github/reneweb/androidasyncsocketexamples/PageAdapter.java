package com.github.reneweb.androidasyncsocketexamples;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        UserFragment tab1 = new UserFragment();
        SettingFragment tab3 = new SettingFragment();
        TestFragment tab2 = new TestFragment();

        switch (position) {
            case 0:

                return tab1;
            case 1:

                return tab2;
            case 2:

                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
