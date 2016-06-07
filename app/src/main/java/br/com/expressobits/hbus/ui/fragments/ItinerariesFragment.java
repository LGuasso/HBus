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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.dao.SaveCodesAsyncTask;
import br.com.expressobits.hbus.dao.SaveItinerariesAsyncTask;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.DAOUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Fragmento que exibe todos {@link br.com.expressobits.hbus.model.Itinerary}
 *
 * <p>Tem comportamento de Callback do fragmento com Activity {@link br.com.expressobits.hbus.ui.MainActivity}</p>
 *
 * @author Rafael Correa
 * @since 16/11/15
 */
public class ItinerariesFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    private List<Itinerary> listItineraries = new ArrayList<>();
    private List<Itinerary> favoriteItineraries;
    private List<Code> codes;
    public static final String TAG = "ItinerariesFragment";
    private RecyclerView recyclerViewItineraries;
    private ProgressBar progressBar;
    public int lastPositionItinerary;
    private SaveItinerariesAsyncTask saveItinerariesAsyncTask;
    private SaveCodesAsyncTask saveCodesAsyncTask;
    int totalCodes=0;
    int totalItineraries=0;
    int progressCodes =0;
    int progressItineraries =0;

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
        ((MainActivity)getActivity()).setSelectItemNavigation(TAG);
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerViewItineraries = (RecyclerView) view.findViewById(R.id.recyclerViewItineraries);
        recyclerViewItineraries.setHasFixedSize(true);

        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);

        FavoriteDAO favoriteDAO = new FavoriteDAO(getActivity());

        favoriteItineraries = favoriteDAO.getItineraries(cityId);

        if(listItineraries.size()>0){
            recyclerViewItineraries.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            refreshRecyclerView();
        }else {
            String country = FirebaseUtils.getCountry(cityId);
            String city = FirebaseUtils.getCityName(cityId);
            String company = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(cityId,"SIMSM");
            refresh(country,city,company);
        }

    }



    private void addItinerary(Itinerary itinerary){
        if(listItineraries.size()>0){
            progressBar.setVisibility(View.INVISIBLE);
            recyclerViewItineraries.setVisibility(View.VISIBLE);
        }
        if(itinerary.getId()!=null){
            listItineraries.add(itinerary);
        }
        refreshRecyclerView();

    }

    public void refresh(final String country, final String city,final String company){
        listItineraries.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewItineraries.setVisibility(View.INVISIBLE);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference itinerariesTableRef = database.getReference(FirebaseUtils.ITINERARY_TABLE);
        DatabaseReference countryRef = itinerariesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        DatabaseReference companyRef = cityRef.child(company);
        companyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Itinerary itinerary = dataSnapshot.getValue(Itinerary.class);
                itinerary.setId(FirebaseUtils.getIdItinerary(country,city,company,itinerary.getName()));
                addItinerary(itinerary);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClickListener(View view, int position) {

        Itinerary itinerary = listItineraries.get(position);
        switch (view.getId()){
            case R.id.imageViewStar:
                FavoriteDAO dao = new FavoriteDAO(getActivity());
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

                    /**BusDAO db = new BusDAO(getActivity());
                    if(db.getBuses(cityId,itinerary.getId()).size()<1){
                        //pullBuses(itinerary);
                    }*/

                    lastPositionItinerary = position;

                    Snackbar.make(
                            view,
                            getResources().getString(R.string.added_favorite_itinerary_with_sucess),
                            Snackbar.LENGTH_LONG).show();
                }

                break;
            case R.id.linearLayoutItemList:
                ((MainActivity)getActivity()).onCreateDialogChooseWay(itinerary);
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
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
    }


    /**
     * TODO remove CLoud
     * public void pullItineraries(String cityId){
        linearLayoutProgress.setVisibility(View.VISIBLE);
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
            linearLayoutProgress.setVisibility(View.INVISIBLE);
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
    }*/

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
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CITY_TABLE);
            DatabaseReference countryRef = citiesTableRef.child(DAOUtils.getNameCountry(cityId));
            DatabaseReference cityRef = countryRef.child(DAOUtils.getNameCity(cityId));
            cityRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    City city = dataSnapshot.getValue(City.class);
                    refresh(city.getCountry(),city.getName(),city.getCompanyDefault());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /**BusDAO dao = new BusDAO(getActivity());
            City city = dao.getCityName(cityId);

            //TODO reformular o DB para usar o company default field
            city.setCompanyDefault("SIMSM");
            refresh(city.getCountry(),city.getName(),city.getCompanyDefault());*/

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** TODO remove cloud
    @Override
    public void finished(List list) {

        if(list!=null){
            if(list.size()>0){
                if(list.get(0) instanceof Itinerary){
                    listItineraries = list;
                    Log.d(TAG,list.toString());
                    saveItinerariesAsyncTask = new SaveItinerariesAsyncTask();
                    saveItinerariesAsyncTask.setContext(getActivity());
                    saveItinerariesAsyncTask.setFinishSaveDAO(this);
                    saveItinerariesAsyncTask.execute(new Pair<String, List<Itinerary>>(cityId,list));
                    totalItineraries = list.size();
                    progressItineraries =0;

                }else if(list.get(0) instanceof Code){
                    codes = list;
                    Log.d(TAG,codes.toString());
                    saveCodesAsyncTask = new SaveCodesAsyncTask();
                    saveCodesAsyncTask.setContext(getActivity());
                    saveCodesAsyncTask.setFinishSaveDAO(this);
                    saveCodesAsyncTask.execute(new Pair<String, List<Code>>(cityId,list));
                    totalCodes = list.size();
                    progressCodes =0;

                }else if(list.get(0) instanceof Bus){
                    List<Bus> buses = list;
                    Log.d(TAG,buses.toString());
                    BusDAO db = new BusDAO(getActivity());
                    int result = db.deleteBuses(cityId, listItineraries.get(lastPositionItinerary).getId());
                    Log.d(TAG, "result delete bus " + result);
                    for (Bus bus:buses){
                        Log.d(TAG,"bus "+bus.getTime()+" load from datastore way "+bus.getWay());
                        db.insert(bus);
                    }
                    Log.i(TAG, buses.toString());
                    if(BuildConfig.DEBUG) {
                        Toast.makeText(getActivity(), getString(R.string.load_online_buses_with_sucess), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }*/

    /**@Override
    public void finish() {
            if(saveItinerariesAsyncTask.isFinished() && saveCodesAsyncTask.isFinished()){
                recyclerViewItineraries.setVisibility(View.VISIBLE);
                linearLayoutProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),getString(R.string.load_online_itineraries_with_sucess),Toast.LENGTH_LONG).show();
                refreshRecyclerView();

        }

    }

    @Override
    public void progressUpdate(Object type, int progress, int total) {



        if(type instanceof Code){
            progressCodes++;


        }else if(type instanceof Itinerary){
            progressItineraries++;
        }
        float number = ((float)(progressCodes+progressItineraries)/ (float)(totalCodes+totalItineraries));
        textViewProgress.setText(((int)(number*100))+"%");
        progressBar.setMax(totalItineraries+totalCodes);
        progressBar.setProgress(progressItineraries+progressCodes);

    }*/
}
