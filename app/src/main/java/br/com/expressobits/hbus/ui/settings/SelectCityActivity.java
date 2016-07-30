package br.com.expressobits.hbus.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.ui.adapters.ItemCityAdapter;
import br.com.expressobits.hbus.ui.ManagerInit;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.dialog.FinishListener;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    public static final String DEFAULT_COUNTRY = "RS";

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
        }else{
            refresh(DEFAULT_COUNTRY);
        }
    }

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
        pullCompanies(cities.get(position).getCountry(),cities.get(position).getName());
        Log.d(TAG, "Selection city id=" + cities.get(position).getId());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SelectCityActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG, cities.get(position).getId());
        editor.putString(cities.get(position).getId(),cities.get(position).getCompanyDefault());
        editor.apply();
        if(starter){
            ManagerInit.manager(this);
        }
        finish();
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onFinish() {
        if (initial) {
            ManagerInit.manager(SelectCityActivity.this);
        } else {
            SelectCityActivity.this.finish();
        }
    }

    private void addCity(City city){

        cities.add(city);
        refreshRecyclerView();
        if(cities.size()>0){
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }


    }

    private void addCompany(Company company){
        Log.d(TAG,"add company "+company.getName());
        //TODO implementar adiçao de empresa
    }


    /**
     * @param country
     * @param city
     */
    private void pullCompanies(final String country, final String city){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference itinerariesTableRef = database.getReference(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryRef = itinerariesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        cityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Company company = dataSnapshot.getValue(Company.class);
                company.setId(FirebaseUtils.getIdCompany(country,city,company.getName()));
                addCompany(company);
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




    public void refresh(final String country){
        cities.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = citiesTableRef.child(country);
        countryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE","onChildAdded");
                City city = dataSnapshot.getValue(City.class);
                city.setId(FirebaseUtils.getIdCity(country,city.getName()));
                addCity(city);
                Log.d("FIREBASE",city.getId());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE","onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE","onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE","onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE","onCancelled");
            }
        });
    }
}
