package com.abc.sih.Reports;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionsPageAdapter extends FragmentPagerAdapter {
    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                StartReport requestsFragment = new StartReport();
                return requestsFragment;

            case 1:
                HotFragment chatsFragment = new HotFragment();
                return chatsFragment;



            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "NEW";
            case 1:
                return "SERIOUS";

            default:
                return null;
        }
    }
}
