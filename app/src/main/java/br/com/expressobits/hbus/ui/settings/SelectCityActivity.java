package br.com.expressobits.hbus.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemCityAdapter;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.ui.MainActivity;

public class SelectCityActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private Toolbar pToolbar;
    private RecyclerView recyclerViewCities;
    private List<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initViews();
    }

    private void initActionBar() {
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        pToolbar = (Toolbar) findViewById(R.id.primary_toolbar);
        setSupportActionBar(pToolbar);

        //pToolbar.setTitle("My lines");
        //pToolbar.setSubtitle("subtitle");


    }

    private void initViews(){
        recyclerViewCities = (RecyclerView) findViewById(R.id.recyclerView_cities);
        recyclerViewCities.setHasFixedSize(true);
        recyclerViewCities.setSelected(true);
        recyclerViewCities.setClickable(true);

        LinearLayoutManager llmUseful = new LinearLayoutManager(this);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCities.setLayoutManager(llmUseful);

        cities = new ArrayList<City>();
        City city = new City();
        city.setName("Santa Maria");
        city.setImage(getResources().getDrawable(R.drawable.ic_bus));
        cities.add(city);
        city = new City();
        city.setName("Porto Alegre");
        city.setImage(getResources().getDrawable(R.drawable.ic_delete_grey600_24dp));
        cities.add(city);

        //TODO lista que vem apartir do parse
        ItemCityAdapter itemCityAdapter = new ItemCityAdapter(this,cities);
        itemCityAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewCities.setAdapter(itemCityAdapter);

        initActionBar();
    }

    @Override
    public void onClickListener(View view, int position) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city",cities.get(position).getName());
        editor.apply();

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
