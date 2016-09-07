package br.com.expressobits.hbus.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.utils.HoursUtils;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * @author Rafael
 * @since 27/12/15.
 */
class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ViewPager";
    private final List<HoursFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    String country;
    String city;
    String company;
    String itinerary;
    String way;

    Context context;

    public ViewPagerAdapter(FragmentManager manager,Context context,String country,String city,
                            String company,String itinerary,String way) {
        super(manager);
        this.context = context;
        this.country = country;
        this.city = city;
        this.company = company;
        this.itinerary = itinerary;
        this.way = way;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "Selection fragment view pager " + position);
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(HoursFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }




    public void refresh(String country,String city,String company,String itinerary,String way){
        this.country = country;
        this.city = city;
        this.company = company;
        this.itinerary = itinerary;
        this.way = way;
    }



}