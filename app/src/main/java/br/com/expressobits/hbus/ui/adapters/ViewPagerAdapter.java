package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.fragments.ScheduleFragment;
import br.com.expressobits.hbus.utils.StringUtils;

/**
 * @author Rafael
 * @since 27/12/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ViewPager";
    private final ScheduleFragment[] mFragmentList = new ScheduleFragment[3];
    private String country;
    private String city;
    private String company;
    private String itinerary;
    private String way;
    private Context context;

    public ViewPagerAdapter(FragmentManager manager,Context context) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "Selection fragment view pager " + position);
        if(mFragmentList[position]==null){
            ScheduleFragment scheduleFragment = new ScheduleFragment();
            Bundle args = new Bundle();
            args.putString(ScheduleFragment.ARGS_COUNTRY,country);
            args.putString(ScheduleFragment.ARGS_CITY,city);
            args.putString(ScheduleFragment.ARGS_COMPANY,company);
            args.putString(ScheduleFragment.ARGS_ITINERARY,itinerary);
            args.putString(ScheduleFragment.ARGS_WAY,way);
            args.putString(ScheduleFragment.ARGS_TYPEDAY,StringUtils.getTypeDayInt(position));
            scheduleFragment.setArguments(args);
            mFragmentList[position] = scheduleFragment;
        }
        return mFragmentList[position];
    }

    @Override
    public int getCount() {
        return TypeDay.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getResourceTypeday(position);
    }

    /**
     * Convert integer typeday in string translate from resources
     * @param typeday integer represents Type of day {@link TypeDay}
     * @return Resource typeday translate and formated
     * @see TypeDay
     */
    private String getResourceTypeday(int typeday){
        if(typeday == TypeDay.USEFUL.toInt()){
            return context.getString(R.string.business_days);
        }else if(typeday == TypeDay.SATURDAY.toInt()){
            return context.getString(R.string.saturday);
        }else if(typeday == TypeDay.SUNDAY.toInt()){
            return context.getString(R.string.sunday);
        }else {
            return "ERRO";
        }
    }

    /**
     * Update references
     * @param country Country and region
     * @param city City name
     * @param company Company name
     * @param itinerary itinerary name
     * @param way way
     */
    public void refresh(String country,String city,String company,String itinerary,String way){
        this.country = country;
        this.city = city;
        this.company = company;
        this.itinerary = itinerary;
        this.way = way;
    }

}