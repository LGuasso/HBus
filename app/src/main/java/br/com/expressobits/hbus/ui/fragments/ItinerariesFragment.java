package br.com.expressobits.hbus.ui.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.provider.RecentItineraries;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.ui.model.Header;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Fragmento que exibe todos {@link Itinerary}
 *
 * <p>Tem comportamento de Callback do fragmento com Activity {@link br.com.expressobits.hbus.ui.MainActivity}</p>
 *
 * @author Rafael Correa
 * @since 16/11/15
 */
public class ItinerariesFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    private List<Object> listItineraries = new ArrayList<>();
    public static final String TAG = "ItinerariesFragment";
    private FastScrollRecyclerView recyclerViewItineraries;
    //private ProgressBar progressBar;
    public int lastPositionItinerary;
    private String city;
    private String country;
    private String company;

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
        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        if(listItineraries.size()>0){
            recyclerViewItineraries.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.INVISIBLE);
            refreshRecyclerView();
        }
        country = FirebaseUtils.getCountry(cityId);
        city = FirebaseUtils.getCityName(cityId);
        refresh(country,city);
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerViewItineraries = (FastScrollRecyclerView) view.findViewById(R.id.recyclerViewItineraries);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        recyclerViewItineraries.setLayoutManager(llmUseful);
        recyclerViewItineraries.setHasFixedSize(true);
    }

    private void addItinerary(Itinerary itinerary){
        if(itinerary.getId()!=null){
            listItineraries.add(itinerary);
        }
        if(listItineraries.size()>0){
            //progressBar.setVisibility(View.INVISIBLE);
            recyclerViewItineraries.setVisibility(View.VISIBLE);
        }
        refreshRecyclerView();

    }

    private void refresh(String country,String city){
        listItineraries.clear();

        //progressBar.setVisibility(View.VISIBLE);
        recyclerViewItineraries.setVisibility(View.INVISIBLE);

        loadRecentItinerariesFromPreference();

        listItineraries.add(new Header(getString(R.string.all_itineraries)));
        loadItinerariesFromDatabase(country,city);

    }

    private void loadItinerariesFromDatabase(String country,String city){
        ScheduleDAO scheduleDAO = new ScheduleDAO(getContext(),country,city);
        List<Itinerary> itineraries = scheduleDAO.getItineraries();
        Collections.sort(itineraries);
        for (Itinerary itinerary:itineraries) {
            addItinerary(itinerary);
        }
        scheduleDAO.close();
    }

    private void loadRecentItinerariesFromPreference(){
        List<String> ids = RecentItineraries.getListRecentIdItineraries(getContext(),SQLConstants.getIdCityDefault(country,city));
        if(ids.size()>0){
            listItineraries.add(0,new Header("Itinerários Recentes"));
            ScheduleDAO scheduleDAO = new ScheduleDAO(getContext(),country,city);
            for (int i = 0; i < ids.size(); i++) {
                addItinerary(scheduleDAO.getItineraryForId(ids.get(i)));
            }
            scheduleDAO.close();
        }

    }

    @Override
    public void onClickListener(View view, int position) {
        if(listItineraries.get(position) instanceof Itinerary){
            Itinerary itinerary = (Itinerary)listItineraries.get(position);
            switch (view.getId()){
                case R.id.icon:
                    BookmarkItineraryDAO dao = new BookmarkItineraryDAO(getActivity());
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
                        dao.insert(itinerary);
                        Log.d(TAG,"insert favorite "+itinerary.getId());
                        dao.close();

                        lastPositionItinerary = position;

                        Snackbar.make(
                                view,
                                getResources().getString(R.string.added_bookmark_itinerary_with_sucess),
                                Snackbar.LENGTH_LONG).show();
                    }
                    recyclerViewItineraries.getAdapter().notifyDataSetChanged();
                    break;
                case R.id.linearLayoutItemList:
                    ((MainActivity)getActivity()).onCreateDialogChooseWay(itinerary);
                    break;
            }
        }


    }



    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

    public void refreshRecyclerView(){

        //
        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getContext(), listItineraries);
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        recyclerViewItineraries.setLayoutManager(llmUseful);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_itineraries_fragment, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo( searchManager.getSearchableInfo( getActivity().getComponentName() ) );
        searchView.setQueryHint( getResources().getString(R.string.itinerary_search_hint) );
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.menu_action_refresh) {
            this.refresh(country,city);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
