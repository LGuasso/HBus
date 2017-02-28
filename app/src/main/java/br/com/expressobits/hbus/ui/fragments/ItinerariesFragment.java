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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.firebase.FirebaseManager;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemItineraryAdapter;
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

    private List<Itinerary> listItineraries = new ArrayList<>();
    public static final String TAG = "ItinerariesFragment";
    private RecyclerView recyclerViewItineraries;
    private ProgressBar progressBar;
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
            progressBar.setVisibility(View.INVISIBLE);
            refreshRecyclerView();
        }
        country = FirebaseUtils.getCountry(cityId);
        city = FirebaseUtils.getCityName(cityId);
        company = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(cityId,"SIMSM");
        refresh(country,city,company);
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerViewItineraries = (RecyclerView) view.findViewById(R.id.recyclerViewItineraries);
        recyclerViewItineraries.setHasFixedSize(true);
    }



    private void addItinerary(Itinerary itinerary){
        if(itinerary.getId()!=null){
            listItineraries.add(itinerary);
        }
        if(listItineraries.size()>0){
            progressBar.setVisibility(View.INVISIBLE);
            recyclerViewItineraries.setVisibility(View.VISIBLE);
        }
        refreshRecyclerView();

    }

    private void refresh(String country,String city,String company){
        listItineraries.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewItineraries.setVisibility(View.INVISIBLE);
        loadCompaniesFromFirebase(country,city);
    }

    private void loadCompaniesFromFirebase(final String country,final String city){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference itinerariesTableRef = database.getReference(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryRef = itinerariesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Company company = dataSnapshot1.getValue(Company.class);
                    if(ItinerariesFragment.this.isVisible()){
                        loadItinerariesFromFirebase(country, city, company.getName());
                        loadCodesFromFirebase(country, city, company.getName());
                    }

                    Log.d(TAG, company.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        cityRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private void loadItinerariesFromFirebase(final String country, final String city, final String company) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference itinerariesTableRef = database.getReference(FirebaseUtils.ITINERARY_TABLE);
        DatabaseReference countryRef = itinerariesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        DatabaseReference companyRef = cityRef.child(company);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotItinerary : dataSnapshot.getChildren()) {
                    if(ItinerariesFragment.this.isVisible()){
                        Itinerary itinerary = dataSnapshotItinerary.getValue(Itinerary.class);
                        itinerary.setId(FirebaseUtils.getIdItinerary(country, city, company, itinerary.getName()));
                        addItinerary(itinerary);
                    }else {
                        Log.i(TAG,"Cancel load itinerary (FRAGMENT) no visible");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        companyRef.addListenerForSingleValueEvent(valueEventListener);

    }

    private void loadCodesFromFirebase(final String country, final String city, final String company) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference codesTableRef = database.getReference(FirebaseUtils.CODE_TABLE);
        DatabaseReference countryRef = codesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        DatabaseReference companyRef = cityRef.child(company);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotCode : dataSnapshot.getChildren()) {
                    if(ItinerariesFragment.this.isVisible()){
                        Code code = dataSnapshotCode.getValue(Code.class);
                        code.setId(FirebaseUtils.getIdCode(country, city, company, code.getName()));
                        Log.d("CODE LOAD FORM FIREBASE", "Code " + code.getName());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        companyRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onClickListener(View view, int position) {

        Itinerary itinerary = listItineraries.get(position);
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
                    dao.insert(listItineraries.get(position));
                    Log.d(TAG,"insert favorite "+itinerary.getId());
                    dao.close();

                    FirebaseManager.loadBusesForItinerary(itinerary);


                    lastPositionItinerary = position;

                    Snackbar.make(
                            view,
                            getResources().getString(R.string.added_bookmark_itinerary_with_sucess),
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

        Collections.sort(listItineraries);

        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getContext(), listItineraries);
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.fragment_itineraries, menu);
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
            this.refresh(country,city,company);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
