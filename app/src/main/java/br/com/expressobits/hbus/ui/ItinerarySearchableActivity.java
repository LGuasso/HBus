package br.com.expressobits.hbus.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.analytics.FirebaseAnalyticsManager;
import br.com.expressobits.hbus.application.AdManager;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.provider.ItinerarySearchableProvider;
import br.com.expressobits.hbus.ui.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.settings.PrivacyPreferenceFragment;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

import static br.com.expressobits.hbus.R.id.linearLayoutItemList;

public class ItinerarySearchableActivity extends AppCompatActivity implements
        ChooseWayDialogListener,RecyclerViewOnClickListenerHack {

    private static final String TAG = "ItinerarySearchable";
    private String country;
    private String city;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private List<Object> itinerariesSearchList;
    private ItemItineraryAdapter itemItineraryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_searchable);
        initViews();
        AdManager.initAdInterstitial(this);
        handleSearch(getIntent());
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.itineraryRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        itemItineraryAdapter = new ItemItineraryAdapter(this, itinerariesSearchList);
        itemItineraryAdapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(itemItineraryAdapter);
    }

    private void loadData() {
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        city = FirebaseUtils.getCityName(cityId);
        country = FirebaseUtils.getCountry(cityId);
        itinerariesSearchList = new ArrayList<>();
    }

    private void loadItinerariesFromDatabase(String itineraryQuery, final String country, final String city) {
        ScheduleDAO dao = new ScheduleDAO(getBaseContext(),country,city);
        List<Itinerary> itineraries = dao.getItineraries();
        for (Itinerary itinerary : itineraries) {
            if(itinerary.getName().toLowerCase().contains(itineraryQuery.toLowerCase())){
                addItinerary(itinerary);
            }
        }
    }

    private void addItinerary(Itinerary itinerary) {
        itinerariesSearchList.add(itinerary);
        itemItineraryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }


    public void handleSearch(Intent intent) {
        itinerariesSearchList.clear();
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String q = intent.getStringExtra(SearchManager.QUERY);
            FirebaseAnalyticsManager.registerEventSearchItinerary(this,country,city,q);
            if(getSupportActionBar()!=null){
                getSupportActionBar().setTitle(q);
            }
            loadItinerariesFromDatabase(q,country,city);
            if(!PreferenceManager.getDefaultSharedPreferences(this).
                    getBoolean(PrivacyPreferenceFragment.PREFERENCE_PAUSE_SEARCH_HISTORY,false)){
                SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                        ItinerarySearchableProvider.AUTHORITY,
                        ItinerarySearchableProvider.MODE);
                searchRecentSuggestions.saveRecentQuery(q, null);
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /**outState.putParcelableArrayList("mList", (ArrayList<Itinerary>) mList);
         outState.putParcelableArrayList("itinerariesSearchList", (ArrayList<Itinerary>) itinerariesSearchList);*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchable_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) item.getActionView();
        } else {
            searchView = (SearchView) MenuItemCompat.getActionView(item);
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.itinerary_search_hint));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onItemClick(String country,String city,String company,String itinerary, String way) {
        AppManager.onSettingsDone(this,country,city,company,itinerary,way);
    }

    public void onCreateDialogChooseWay(Itinerary itinerary) {
        List<String> ways;
        String company = FirebaseUtils.getCompany(itinerary.getId());
        ways = itinerary.getWays();
        if (ways.size() > 1) {
            ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
            chooseWayDialogFragment.setParameters(this,country,city,company,itinerary.getName(), ways);
            chooseWayDialogFragment.show(ItinerarySearchableActivity.this.getSupportFragmentManager(), ChooseWayDialogFragment.TAG);
        } else {
            AppManager.onSettingsDone(this,country,city,company,itinerary.getName(),ways.get(0));
        }
    }

    @Override
    public void onClickListener(View view, int position) {
        if(itinerariesSearchList.get(position) instanceof Itinerary){
            Itinerary itinerary = (Itinerary)itinerariesSearchList.get(position);
            switch (view.getId()) {
                case R.id.icon:
                    BookmarkItineraryDAO dao = new BookmarkItineraryDAO(this);
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
                        Snackbar.make(
                                view,
                                getResources().getString(R.string.added_bookmark_itinerary_with_sucess),
                                Snackbar.LENGTH_LONG).show();
                    }
                    break;
                case linearLayoutItemList:
                    onCreateDialogChooseWay(itinerary);
                    break;

            }
        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
