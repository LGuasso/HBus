package br.com.expressobits.hbus.ui.fragments;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
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
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.gae.PullBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.util.NetworkUtils;
import br.com.expressobits.hbus.utils.DAOUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horarios
 */
public class OnibusFragment extends Fragment implements RecyclerViewOnClickListenerHack,ResultListenerAsyncTask<Bus>{

    public static final String TAG = "OnibusFragment";
    public static final String ARGS_CITYID = "cityId";
    public static final String ARGS_ITINERARYID = "itineraryId";
    public static final String ARGS_WAY = "Way";

    private String cityId;
    private String itineraryId;
    private String way;

    private ProgressBar progressBar;
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


            this.cityId = arguments.getString(ARGS_CITYID);
            this.itineraryId = arguments.getString(ARGS_ITINERARYID);
            this.way = arguments.getString(ARGS_WAY);
            refresh(cityId,itineraryId,way);

        }

        setHasOptionsMenu(true);
        return view;

    }

    private void initViews(View view) {
        initTabLayout(view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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

        viewPagerAdapter.refresh(cityId, itineraryId, way);
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

    @Override
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
            pullBuses();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pullBuses(){
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        Log.d(TAG,"initial push buses");
        if(NetworkUtils.isWifiConnected(getActivity()) || NetworkUtils.isMobileConnected(getActivity())) {
            PullBusEndpointsAsyncTask pullBusEndpointsAsyncTask = new PullBusEndpointsAsyncTask();
            pullBusEndpointsAsyncTask.setContext(getActivity());
            pullBusEndpointsAsyncTask.setResultListenerAsyncTask(this);
            City city = new City();
            city.setName(DAOUtils.getNameCity(cityId));
            city.setCountry(DAOUtils.getNameCountry(cityId));

            BusDAO dao = new BusDAO(getActivity());

            Itinerary itinerary = dao.getItinerary(itineraryId);

            Pair<City,Itinerary> pair = new Pair<>(city,itinerary);

            pullBusEndpointsAsyncTask.execute(pair);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            //imageViewNetworkError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finished(List<Bus> buses) {
        if(buses!=null){
            //listItineraries = itineraries;
            BusDAO db = new BusDAO(getActivity());
            int result = db.deleteBuses(cityId,itineraryId);
            for (Bus bus:buses){
                Log.d(TAG,"bus "+bus.getTime()+"load from datastore way"+bus.getWay());
                db.insert(bus);
            }

            viewPager.setVisibility(View.VISIBLE);
        }else{
            //imageViewNetworkError.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
        refresh(cityId,itineraryId,way);
    }
}
