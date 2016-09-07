package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horarios
 */
public class OnibusFragment extends Fragment{

    public static final String TAG = "OnibusFragment";

    public static final String ARGS_COUNTRY = "country";
    public static final String ARGS_CITY = "city";
    public static final String ARGS_COMPANY = "company";
    public static final String ARGS_ITINERARY = "itinerary";
    public static final String ARGS_WAY = "way";

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container,false);
        initViews(view);

        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getString(ARGS_COUNTRY)!=null &&
                arguments.getString(ARGS_CITY)!=null && arguments.getString(ARGS_COMPANY)!=null &&
                arguments.getString(ARGS_ITINERARY)!=null && arguments.getString(ARGS_WAY)!=null){
            String country = arguments.getString(ARGS_COUNTRY);
            String city = arguments.getString(ARGS_CITY);
            String company = arguments.getString(ARGS_COMPANY);
            String itinerary = arguments.getString(ARGS_ITINERARY);
            String way = arguments.getString(ARGS_WAY);
            refresh(country, city, company, itinerary, way);
        }
        setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View view) {
        initTabLayout(view);
        //ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }



    private void initTabLayout(View view){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        Bundle arguments = getArguments();
        setupViewPager(viewPager,arguments.getString(ARGS_COUNTRY),arguments.getString(ARGS_CITY),
                arguments.getString(ARGS_COMPANY),arguments.getString(ARGS_ITINERARY),arguments.getString(ARGS_WAY));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager,String country,String city,String company,String itinerary,String way) {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(),country,city,company,itinerary,way);
        Bundle argsUseful = new Bundle();
        argsUseful.putString(HoursFragment.ARGS_COUNTRY,country);
        argsUseful.putString(HoursFragment.ARGS_CITY,city);
        argsUseful.putString(HoursFragment.ARGS_COMPANY,company);
        argsUseful.putString(HoursFragment.ARGS_ITINERARY,itinerary);
        argsUseful.putString(HoursFragment.ARGS_WAY, way);
        argsUseful.putString(HoursFragment.ARGS_TYPEDAY, TypeDay.USEFUL.toString());
        HoursFragment hoursFragmentUseful = new HoursFragment();
        hoursFragmentUseful.setArguments(argsUseful);

        Bundle argsSaturday = new Bundle();
        argsSaturday.putString(HoursFragment.ARGS_COUNTRY,country);
        argsSaturday.putString(HoursFragment.ARGS_CITY,city);
        argsSaturday.putString(HoursFragment.ARGS_COMPANY,company);
        argsSaturday.putString(HoursFragment.ARGS_ITINERARY,itinerary);
        argsSaturday.putString(HoursFragment.ARGS_WAY, way);
        argsSaturday.putString(HoursFragment.ARGS_TYPEDAY, TypeDay.SATURDAY.toString());
        HoursFragment hoursFragmentSaturday = new HoursFragment();
        hoursFragmentSaturday.setArguments(argsSaturday);

        Bundle argsSunday = new Bundle();
        argsSunday.putString(HoursFragment.ARGS_COUNTRY,country);
        argsSunday.putString(HoursFragment.ARGS_CITY,city);
        argsSunday.putString(HoursFragment.ARGS_COMPANY,company);
        argsSunday.putString(HoursFragment.ARGS_ITINERARY,itinerary);
        argsSunday.putString(HoursFragment.ARGS_WAY, way);
        argsSunday.putString(HoursFragment.ARGS_TYPEDAY, TypeDay.SUNDAY.toString());
        HoursFragment hoursFragmentSunday = new HoursFragment();
        hoursFragmentSunday.setArguments(argsSunday);

        viewPagerAdapter.addFragment(hoursFragmentUseful,
                getString(R.string.business_days));
        viewPagerAdapter.addFragment(hoursFragmentSaturday,
                getString(R.string.saturday));
        viewPagerAdapter.addFragment(hoursFragmentSunday,
                getString(R.string.sunday));
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void refresh(String country,String city,String company,String itinerary,String way){
        viewPagerAdapter.refresh(country,city,company,itinerary,way);
        viewPager.setAdapter(viewPagerAdapter);
        //TODO implement sunday days in holiday
        int typeday  = HoursUtils.getTipoDeDia(GregorianCalendar.getInstance());
        viewPager.setCurrentItem(typeday);
    }

    /**@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.fragment_bus, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.menu_action_refresh) {
           // pullBuses();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onDetach() {
        Log.d(TAG,"onDetach");
        super.onDetach();
    }
}
