package br.com.expressobits.hbus.ui.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.FragmentManagerListener;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemHomeAdapter;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.model.Header;
import br.com.expressobits.hbus.ui.news.NewsDetailsActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * A simple {@link Fragment} subclass.
 * @author Rafael Correa
 * @since 06/07/2015
 */
public class HomeFragment extends Fragment implements RecyclerViewOnClickListenerHack,
        ChooseWayDialogListener,View.OnClickListener{

    public static final String TAG = "HomeFragment";
    public String selectedItem;
    private RecyclerView recyclerView;
    private List<Object> items = new ArrayList<>();
    FragmentManagerListener mCallback;
    LinearLayout linearLayoutEmptyList;
    LinearLayoutManager llmUseful;
    private String cityId;
    View view;
    private String country;
    private String cityName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentManagerListener) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " precisa implementar OnSettingsListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pullDataItens();
    }

    private void updateEmptyListView() {
        if(items.size()<1){
            recyclerView.setVisibility(View.GONE);
            linearLayoutEmptyList.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            linearLayoutEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        country = FirebaseUtils.getCountry(cityId);
        cityName = FirebaseUtils.getCityName(cityId);
        view = inflater.inflate(R.layout.fragment_favorite_itinerary, container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initListViews(view);
        initEmptyList(view);
        setHasOptionsMenu(true);
    }

    private void initEmptyList(View view) {
        linearLayoutEmptyList = (LinearLayout)view.findViewById(R.id.list_empty);
        linearLayoutEmptyList.setOnClickListener(this);

    }

    private void initListViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list_lines);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);
        ItemHomeAdapter adapter = new ItemHomeAdapter(this.getActivity(), items);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);

    }

    private void getBookmarkedItineraries(){
        BookmarkItineraryDAO dao  = new BookmarkItineraryDAO(getActivity());
        List<Itinerary> itineraries = dao.getItineraries(cityId);
        dao.close();
        items.add(0,new Header(getResources().getString(R.string.bookmarks)));
        items.addAll(itineraries);
        updateListViews();
    }

    private void getLastNews(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newsTableRef = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference cityTableRef = newsTableRef.child(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = cityTableRef.child(country);
        cityName = FirebaseUtils.getCityName(cityId);
        DatabaseReference cityRef = countryRef.child(cityName);
        Query recentNewsQuery = cityRef.limitToLast(1);
        recentNewsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                News news = dataSnapshot.getValue(News.class);
                country = FirebaseUtils.getCountry(cityId);
                news.setId(FirebaseUtils.getIdNewsCity(String.valueOf(news.getTime()), country, cityName));

                if(news.isActived() &&
                        !(PreferenceManager.getDefaultSharedPreferences(getContext()).
                                getBoolean(NewsDetailsActivity.READ_PREFERENCE+"/"+news.getId(), false))){
                    items.add(0,new Header(getResources().getString(R.string.unread_news)));
                    items.add(1,news);
                    updateListViews();
                }
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

    private void pullDataItens(){
        items.clear();
        getBookmarkedItineraries();
        getLastNews();
    }

    private void updateListViews() {
        recyclerView.getAdapter().notifyDataSetChanged();
        updateEmptyListView();
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
    public void onClickListener(final View view, int position) {

        if (items.get(position) instanceof Itinerary) {
            Itinerary itinerary = (Itinerary)items.get(position);
            switch (view.getId()){
                case R.id.buttonLookTime:
                    ((MainActivity)getActivity()).onCreateDialogChooseWay(itinerary);
                    break;
                case R.id.buttonRemove:
                    selectedItem = itinerary.getId();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.dialog_alert_title_confirm_remove);
                    builder.setNegativeButton(android.R.string.no, null);
                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        BookmarkItineraryDAO dao = new BookmarkItineraryDAO(getActivity());
                        Itinerary itinerary1 = dao.getItinerary(selectedItem);
                        dao.removeFavorite(itinerary1);
                        HomeFragment.this.initListViews(HomeFragment.this.view);
                        items.remove(itinerary);
                        HomeFragment.this.
                        updateEmptyListView();
                        String result = getActivity().getResources().getString(R.string.delete_itinerary_with_sucess, itinerary1.getName());
                        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(MainActivity.DEBUG, false)) {
                            Toast.makeText(getContext(),result, Toast.LENGTH_LONG).show();
                        }
                        Snackbar.make(HomeFragment.this.view,result, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        dao.close();
                    });
                    builder.show();
                    break;
            }

        }else if(items.get(position) instanceof News){
            News news = (News)items.get(position);
            Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
            intent.putExtra(NewsDetailsActivity.ARGS_NEWS_ID,news.getId());
            getContext().startActivity(intent);
        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {

        return false;
    }


    @Override
    public void onItemClick(String country,String city,String company,String itineraryId,String way) {
        AppManager.onSettingsDone(getActivity(),country,city,company,itineraryId,way);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_empty:
                mCallback.addFragment(ItinerariesFragment.TAG);
                break;
        }
    }
}


