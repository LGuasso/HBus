package br.com.expressobits.hbus.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.OnSettingsListener;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;

public class AddFavoriteActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private ArrayList<Itinerary> itineraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        initViews();
    }

    private void initViews(){
        initListViews();
    }

    private void initListViews(){
        RecyclerView recyclerViewItineraries = (RecyclerView) findViewById(R.id.recyclerViewAddItineraries);
        BusDAO dao = new BusDAO(this);
        //TODO implementar linhas que ainda n existem no FAVORITeDAO favoritos
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);

        itineraries = new ArrayList<>(dao.getItineraries(cityId));

        FavoriteDAO favoriteDAO = new FavoriteDAO(this);

        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(this,false,itineraries,favoriteDAO.getItineraries(cityId));
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);
        recyclerViewItineraries.setClickable(true);
        recyclerViewItineraries.addItemDecoration(new SimpleDividerItemDecoration(this));
        LinearLayoutManager llmUseful = new LinearLayoutManager(this);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
        dao.close();
    }

    @Override
    public void onClickListener(View view, int position) {
        TimesDbHelper dao = new TimesDbHelper(this);
        FavoriteDAO favoriteDAO = new FavoriteDAO(this);
        favoriteDAO.insert(itineraries.get(position));
        dao.close();
        finish();
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
