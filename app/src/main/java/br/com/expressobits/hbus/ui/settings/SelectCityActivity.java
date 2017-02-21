package br.com.expressobits.hbus.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.analytics.FirebaseAnalyticsManager;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.application.ManagerInit;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemCityAdapter;
import br.com.expressobits.hbus.ui.dialog.FinishListener;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class SelectCityActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack,FinishListener{

    private List<City> cities;
    public boolean initial = false;
    public static final String TAG = "city";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView imageViewNetworkError;
    private boolean starter = false;
    public static final String NOT_CITY = "not_city";
    public static final String STARTER_MODE = "starter";
    public static final String DEFAULT_COUNTRY = "BR/RS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        starter = getIntent().getBooleanExtra(STARTER_MODE,false);
        setContentView(R.layout.activity_select_city);
        //Define se e primeira vez do app ou se tem alguma cidade definida...
        initial = (PreferenceManager.getDefaultSharedPreferences(this).getString(TAG,NOT_CITY).equals(NOT_CITY));
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_refresh) {
            refresh(DEFAULT_COUNTRY);
        }
        return false;
    }

    private void initActionBar() {
        Toolbar pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
    }

    private void initViews() {
        initProgressBar();
        initActionBar();
        imageViewNetworkError = (ImageView) findViewById(R.id.imageNetworkError);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_cities);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        recyclerView.setClickable(true);
        LinearLayoutManager llmUseful = new LinearLayoutManager(this);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);
        cities = new ArrayList<>();
        //Se houver cidades no database local não haverá procura no remoto
        if(cities.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            imageViewNetworkError.setVisibility(View.INVISIBLE);
        }else{
            refresh(DEFAULT_COUNTRY);
        }
    }

    /**
     * Update recyclerview with arraylist city
     */
    private void refreshRecyclerView() {
        ItemCityAdapter itemCityAdapter = new ItemCityAdapter(this, cities);
        itemCityAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(itemCityAdapter);
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClickListener(View view, final int position) {
        City city = cities.get(position);
        pullCompanies(city.getCountry(), city.getName());
        Log.d(TAG, "Selection city id=" + city.getId());
        SharedPreferences sharedPreferences = saveCityPreference(city);
        unsSubscribe(sharedPreferences.getString(TAG,SelectCityActivity.NOT_CITY));
        subscribe(city.getId());
        FirebaseAnalyticsManager.registerEventCity(this,city.getCountry(),city.getName());
        if(starter){
            ManagerInit.manager(this);
        }
        finish();
    }

    @NonNull
    private SharedPreferences saveCityPreference(City city) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SelectCityActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG, city.getId());
        editor.putString(city.getId(), city.getCompanyDefault());
        editor.apply();
        return sharedPreferences;
    }

    private void unsSubscribe(String id){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseUtils.getIdForSubscribeCity(id));
        Log.d(TAG, "Unsubscribed to news topic");
    }

    private void subscribe(String id){
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtils.getIdForSubscribeCity(id));
        Log.d(TAG, "Subscribe to news topic");
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

    @Override
    public void onFinish() {
        //Verify if first open app
        if (initial) {
            ManagerInit.manager(SelectCityActivity.this);
        } else {
            SelectCityActivity.this.finish();
        }
    }

    /**
     * Add city in recycler view adapter
     * @param city city object from database
     */
    private void addCity(City city){
        if(!cities.contains(city)){
            cities.add(city);
            refreshRecyclerView();
            if(cities.size()>0){
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addCompany(Company company){
        Log.d(TAG,"add company "+company.getName());
        //TODO implementar adiçao de empresa
    }



    private void pullCompanies(final String country, final String city){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference itinerariesTableRef = database.getReference(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryRef = itinerariesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotCompany : dataSnapshot.getChildren()) {
                    Company company = dataSnapshotCompany.getValue(Company.class);
                    company.setId(FirebaseUtils.getIdCompany(country, city, company.getName()));
                    addCompany(company);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        cityRef.addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * Update cities of recyclerview with data from firebase realtime database
     * @param country Region with country
     */
    public void refresh(final String country){
        cities.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = citiesTableRef.child(country);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotCity : dataSnapshot.getChildren()) {
                    City city = dataSnapshotCity.getValue(City.class);
                    city.setId(FirebaseUtils.getIdCity(country, city.getName()));
                    addCity(city);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                imageViewNetworkError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        };
        countryRef.addListenerForSingleValueEvent(valueEventListener);

    }

}
