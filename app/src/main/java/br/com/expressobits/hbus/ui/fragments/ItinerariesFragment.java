package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public static final String TAG = "ItinerariesFragment";
    private RecyclerView recyclerViewItineraries;

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
        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        recyclerViewItineraries = (RecyclerView) view.findViewById(R.id.recyclerViewItineraries);
        recyclerViewItineraries.setHasFixedSize(true);
        FavoriteDAO favoriteDAO = new FavoriteDAO(getActivity());
        BusDAO dao = new BusDAO(getActivity());
        //listItineraries = dao.getItineraries(cityId);
        //List<Itinerary> favoriteItineraries = favoriteDAO.getItineraries(cityId);
        //ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getContext(),true,listItineraries,favoriteItineraries);
        //dao.close();
        PullItinerariesEndpointsAsyncTask pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
        pullItinerariesEndpointsAsyncTask.setContext(getActivity());
        pullItinerariesEndpointsAsyncTask.setResultListenerAsyncTask(this);
        City city = new City();
        city.setName(cityId.split("/")[0]);
        city.setCountry(cityId.split("/")[1]);
        pullItinerariesEndpointsAsyncTask.execute(city);
    }

    @Override
    public void onClickListener(View view, int position) {
        ((MainActivity)getActivity()).onCreateDialogChooseWay(listItineraries.get(position).getId());
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

    @Override
    public void finished(List<Itinerary> itineraries) {
        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getContext(),true,listItineraries);
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
    }
}
