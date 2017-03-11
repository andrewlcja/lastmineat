package app.lastmineat.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.lastmineat.fragments.ViewDeals;
import app.lastmineat.fragments.ViewVendors;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public HomeViewPagerAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ViewDeals viewDeals = new ViewDeals();
                return viewDeals;
            case 1:
                ViewVendors viewVendors = new ViewVendors();
                return viewVendors;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
