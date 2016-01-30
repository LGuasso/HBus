package br.com.expressobits.hbus.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.utils.TextUtils;

/**
 * @author Rafael
 * @since 27/12/15.
 */
class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<HoursFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    String cityId;
    String itineraryId;
    String way;
    Context context;

    public ViewPagerAdapter(FragmentManager manager,Context context,String cityId,String itineraryId,String way) {
        super(manager);
        this.context = context;
        this.way = way;
        this.itineraryId = itineraryId;
        this.cityId = cityId;
    }

    @Override
    public Fragment getItem(int position) {
        HoursFragment hoursFragment = new HoursFragment();
        Bundle args = new Bundle();
        args.putString(HoursFragment.ARGS_CITYID, cityId);
        args.putString(HoursFragment.ARGS_ITINERARYID, itineraryId);
        args.putString(HoursFragment.ARGS_WAY, way);
        args.putString(HoursFragment.ARGS_TYPEDAY, TextUtils.getTypeDayInt(position));
        hoursFragment.setArguments(args);
        mFragmentList.set(position,hoursFragment);
        return hoursFragment;
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

    public void refresh(String cityId,String itineraryId,String way){
        this.cityId = cityId;
        this.way = way;
        this.itineraryId = itineraryId;
    }


}