package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.gae.PullBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullCodesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;
import br.com.expressobits.hbus.util.NetworkUtils;
import br.com.expressobits.hbus.utils.DAOUtils;

/**
 * Fragmento que exibe todos {@link br.com.expressobits.hbus.model.Itinerary}
 *
 * <p>Tem comportamento de Callback do fragmento com Activity {@link br.com.expressobits.hbus.ui.MainActivity}</p>
 *
 * @author Rafael Correa
 * @since 16/11/15
 */
public class ItinerariesFragment extends Fragment implements RecyclerViewOnClickListenerHack,ResultListenerAsyncTask{

    private List<Itinerary> listItineraries;
    private List<Itinerary> favoriteItineraries;
    private List<Code> codes;
    public static final String TAG = "ItinerariesFragment";
    private RecyclerView recyclerViewItineraries;
    private ProgressBar progressBar;
    private String cityId;
    public int lastPositionItinerary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraries,container,false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle();
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        recyclerViewItineraries = (RecyclerView) view.findViewById(R.id.recyclerViewItineraries);
        recyclerViewItineraries.setHasFixedSize(true);

        BusDAO dao = new BusDAO(getActivity());
        listItineraries = dao.getItineraries(cityId);
        Log.d(TAG, "listItineraries size " + listItineraries.size());

        FavoriteDAO favoriteDAO = new FavoriteDAO(getActivity());
        favoriteItineraries = favoriteDAO.getItineraries(cityId);
        Log.d(TAG, "favoriteItineraries size " + listItineraries.size());

        if(listItineraries.size()>0){
            recyclerViewItineraries.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            refreshRecyclerView();
        }else {
            pullItineraries(cityId);
        }

        dao.close();

    }

    @Override
    public void onClickListener(View view, int position) {
        switch (view.getId()){
            case R.id.imageViewStar:
                FavoriteDAO dao = new FavoriteDAO(getActivity());
                Itinerary itinerary = listItineraries.get(position);
                if(dao.getItinerary(itinerary.getId())!=null){
                    dao.removeFavorite(itinerary);
                    dao.close();
                    Log.d(TAG, "remove favorite " + itinerary.getId());
                    String result = String.format(getResources().getString(R.string.delete_itinerary_with_sucess),itinerary.getName());
                    Snackbar.make(
                            view,
                            result,
                            Snackbar.LENGTH_LONG).show();




                }else {
                    dao.insert(listItineraries.get(position));
                    Log.d(TAG,"insert favorite "+itinerary.getId());
                    dao.close();

                    BusDAO db = new BusDAO(getActivity());
                    if(db.getBuses(cityId,itinerary.getId()).size()<1){
                        lastPositionItinerary = position;
                        pullBuses(itinerary);
                    }

                    Snackbar.make(
                            view,
                            getResources().getString(R.string.added_favorite_itinerary_with_sucess),
                            Snackbar.LENGTH_LONG).show();
                }

                break;
            case R.id.linearLayoutItemList:
                ((MainActivity)getActivity()).onCreateDialogChooseWay(listItineraries.get(position).getId());
                break;
        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

    public void refreshRecyclerView(){

        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getContext(),true,listItineraries,favoriteItineraries);
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);
        recyclerViewItineraries.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
    }


    public void pullItineraries(String cityId){
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewItineraries.setVisibility(View.INVISIBLE);
        Log.d(TAG, "initial push itinerary");
        if(NetworkUtils.isWifiConnected(getActivity()) || NetworkUtils.isMobileConnected(getActivity())) {
            PullItinerariesEndpointsAsyncTask pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
            pullItinerariesEndpointsAsyncTask.setContext(getActivity());
            pullItinerariesEndpointsAsyncTask.setResultListenerAsyncTask(this);
            City city = new City();
            city.setName(DAOUtils.getNameCity(cityId));
            city.setCountry(DAOUtils.getNameCountry(cityId));
            pullItinerariesEndpointsAsyncTask.execute(city);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            //imageViewNetworkError.setVisibility(View.VISIBLE);
        }
    }

    public void pullCodes(String cityId){
        Log.d(TAG,"initial push code");
        if(NetworkUtils.isWifiConnected(getActivity()) || NetworkUtils.isMobileConnected(getActivity())) {
            PullCodesEndpointsAsyncTask pullCodesEndpointsAsyncTask = new PullCodesEndpointsAsyncTask();
            pullCodesEndpointsAsyncTask.setContext(getActivity());
            pullCodesEndpointsAsyncTask.setResultListenerAsyncTask(this);
            City city = new City();
            city.setName(DAOUtils.getNameCity(cityId));
            city.setCountry(DAOUtils.getNameCountry(cityId));
            pullCodesEndpointsAsyncTask.execute(city);
        }
    }

    private void pullBuses(Itinerary itinerary){
        Log.d(TAG,"initial push buses");
        if(NetworkUtils.isWifiConnected(getActivity()) || NetworkUtils.isMobileConnected(getActivity())) {
            PullBusEndpointsAsyncTask pullBusEndpointsAsyncTask = new PullBusEndpointsAsyncTask();
            pullBusEndpointsAsyncTask.setContext(getActivity());
            pullBusEndpointsAsyncTask.setResultListenerAsyncTask(this);
            City city = new City();
            city.setName(DAOUtils.getNameCity(cityId));
            city.setCountry(DAOUtils.getNameCountry(cityId));
            Pair<City,Itinerary> pair = new Pair<>(city,itinerary);

            pullBusEndpointsAsyncTask.execute(pair);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.fragment_itineraries, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.menu_action_refresh) {
            String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
            pullItineraries(cityId);
            pullCodes(cityId);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finished(List list) {

        if(list!=null){
            if(list.size()>0){
                if(list.get(0) instanceof Itinerary){
                    listItineraries = list;
                    Log.d(TAG,list.toString());
                    BusDAO db = new BusDAO(getActivity());
                    int result = db.deleteItineraries(cityId);
                    Log.d(TAG, "result delete ititneraries " + result);
                    for (Itinerary itinerary:listItineraries){
                        Log.d(TAG,"itinerary "+itinerary.getName()+"load from datastore ways"+itinerary.getWays());
                        db.insert(itinerary);
                    }
                    Log.i(TAG, list.toString());
                    recyclerViewItineraries.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),getString(R.string.load_online_itineraries_with_sucess),Toast.LENGTH_LONG).show();
                    refreshRecyclerView();
                }else if(list.get(0) instanceof Code){
                    codes = list;
                    Log.d(TAG,codes.toString());
                    BusDAO db = new BusDAO(getActivity());
                    int result = db.deleteCodes(cityId);
                    Log.d(TAG, "result delete code " + result);
                    for (Code code:codes){
                        Log.d(TAG,"itinerary "+code.getName()+"load from datastore descr "+code.getDescrition());
                        db.insert(code);
                    }
                    Log.i(TAG, codes.toString());
                    Toast.makeText(getActivity(),getString(R.string.load_online_codes_with_sucess),Toast.LENGTH_LONG).show();
                }else if(list.get(0) instanceof Bus){
                    List<Bus> buses = list;
                    Log.d(TAG,buses.toString());
                    BusDAO db = new BusDAO(getActivity());
                    int result = db.deleteBuses(cityId, listItineraries.get(lastPositionItinerary).getId());
                    Log.d(TAG, "result delete bus " + result);
                    for (Bus bus:buses){
                        Log.d(TAG,"bus "+bus.getTime()+"load from datastore way "+bus.getWay());
                        db.insert(bus);
                    }
                    Log.i(TAG, buses.toString());
                    Toast.makeText(getActivity(),getString(R.string.load_online_buses_with_sucess),Toast.LENGTH_LONG).show();
                }
            }
        }


    }
}
