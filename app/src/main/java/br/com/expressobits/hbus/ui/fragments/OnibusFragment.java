package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.GregorianCalendar;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horarios
 */
public class OnibusFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String TAG = "OnibusFragment";
    public static final String ARGS_CITYID = "cityId";
    public static final String ARGS_ITINERARYID = "itineraryId";
    public static final String ARGS_WAY = "Way";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container,false);
        initViews(view);

        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getString(ARGS_CITYID)!=null &&arguments.getString(ARGS_ITINERARYID)!=null && arguments.getString(ARGS_WAY)!=null){
            refresh(arguments.getString(ARGS_CITYID),arguments.getString(ARGS_ITINERARYID),arguments.getString(ARGS_WAY));
        }
        return view;

    }

    private void initViews(View view) {
        initTabLayout(view);
    }



    private void initTabLayout(View view){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        Bundle arguments = getArguments();
        setupViewPager(viewPager,arguments.getString(ARGS_CITYID),arguments.getString(ARGS_ITINERARYID), arguments.getString(ARGS_WAY));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager,String cityId,String itineraryId,String way) {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(),
                cityId,
                itineraryId,
                way);
        Bundle args = new Bundle();
        args.putString(HoursFragment.ARGS_CITYID, cityId);
        args.putString(HoursFragment.ARGS_ITINERARYID,itineraryId);
        args.putString(HoursFragment.ARGS_WAY, way);


        HoursFragment hoursFragmentUseful = new HoursFragment();
        hoursFragmentUseful.setArguments(args);
        hoursFragmentUseful.setTypeday(TypeDay.USEFUL);

        HoursFragment hoursFragmentSaturday = new HoursFragment();
        hoursFragmentSaturday.setArguments(args);
        hoursFragmentSaturday.setTypeday(TypeDay.SATURDAY);

        HoursFragment hoursFragmentSunday = new HoursFragment();
        hoursFragmentSunday.setArguments(args);
        hoursFragmentSunday.setTypeday(TypeDay.SUNDAY);

        viewPagerAdapter.addFragment(hoursFragmentUseful,
                getString(R.string.useful));
        viewPagerAdapter.addFragment(hoursFragmentSaturday,
                getString(R.string.saturday));
        viewPagerAdapter.addFragment(hoursFragmentSunday,
                getString(R.string.sunday));
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * @param itineraryId
     * @param way
     */
    public void refresh(String cityId,String itineraryId,String way){
        viewPagerAdapter.refresh(cityId,itineraryId, way);
        viewPager.setAdapter(viewPagerAdapter);

        //TODO implement sunday days in holiday
        int typeday  = HoursUtils.getTipoDeDia(GregorianCalendar.getInstance());
        Log.d(TAG, "Typday of now " + typeday);

        viewPager.setCurrentItem(typeday);
    }

    @Override
    public void onClickListener(View view, int position) {
        //TODO para imaplementar clickes em horários
        //ItemBusAdapter adapter = (ItemBusAdapter) recyclerViewUsefulDays.getAdapter();
        //adapter.removeListItem(position);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

}
