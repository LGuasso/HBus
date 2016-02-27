package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.gae.PullItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;
import br.com.expressobits.hbus.util.NetworkUtils;

/**
 * Fragmento que exibe todos {@link br.com.expressobits.hbus.model.Itinerary}
 *
 * <p>Tem comportamento de Callback do fragmento com Activity {@link br.com.expressobits.hbus.ui.MainActivity}</p>
 *
 * @author Rafael Correa
 * @since 16/11/15
 */
public class ItinerariesFragment extends Fragment implements RecyclerViewOnClickListenerHack,ResultListenerAsyncTask<Itinerary> {

    private List<Itinerary> listItineraries;
    private List<Itinerary> favoriteItineraries;
    public static final String TAG = "ItinerariesFragment";
    private RecyclerView recyclerViewItineraries;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraries,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.all_lines), null);
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
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
                    Log.d(TAG,"remove favorite "+itinerary.getId());
                    String result = String.format(getResources().getString(R.string.delete_itinerary_with_sucess),itinerary.getName());
                    Snackbar.make(
                            view,
                            result,
                            Snackbar.LENGTH_LONG).show();
                }else {
                    dao.insert(listItineraries.get(position));
                    Log.d(TAG,"insert favorite "+itinerary.getId());
                    dao.close();

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

    @Override
    public void finished(List<Itinerary> itineraries) {

        if(itineraries!=null){
            listItineraries = itineraries;
            BusDAO db = new BusDAO(getActivity());
            for (Itinerary itinerary:listItineraries){
                db.insert(itinerary);
            }
            Log.i(TAG, itineraries.toString());
            recyclerViewItineraries.setVisibility(View.VISIBLE);
        }else{
            //imageViewNetworkError.setVisibility(View.VISIBLE);
            recyclerViewItineraries.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
        refreshRecyclerView();
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
        Log.d(TAG,"initial push itinerary");
        if(NetworkUtils.isWifiConnected(getActivity()) || NetworkUtils.isMobileConnected(getActivity())) {
            PullItinerariesEndpointsAsyncTask pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
            pullItinerariesEndpointsAsyncTask.setContext(getActivity());
            pullItinerariesEndpointsAsyncTask.setResultListenerAsyncTask(this);
            City city = new City();
            city.setName(cityId.split("/")[0]);
            city.setCountry(cityId.split("/")[1]);
            pullItinerariesEndpointsAsyncTask.execute(city);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            //imageViewNetworkError.setVisibility(View.VISIBLE);
        }
    }
}
