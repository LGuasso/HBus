package br.com.expressobits.hbus.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FirebaseDAO;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemCityAdapter;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.ui.ManagerInit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectCityActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack,
        ChildEventListener,ValueEventListener{

    private List<City> cities;
    public boolean initial = false;
    public static final String TAG = "city";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initViews();
    }

    private void initActionBar() {

        //Define se � primeira vez do app ou se tem alguma cidade definida...
        initial = (PreferenceManager.getDefaultSharedPreferences(this).getLong(TAG,0l)==0l);
        Toolbar pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
    }

    private void initViews(){
        initProgressBar();
        initActionBar();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_cities);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        recyclerView.setClickable(true);

        LinearLayoutManager llmUseful = new LinearLayoutManager(this);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);

        cities = new ArrayList<>();

        refreshRecyclerView();

        FirebaseDAO dao = new FirebaseDAO("https://hbus.firebaseio.com/");
        dao.getCities(this);

        //TimesDbHelper db = new TimesDbHelper(this);
        //cities = db.getCities();

        //TODO lista que vem apartir do parse



    }

    private void refreshRecyclerView() {
        ItemCityAdapter itemCityAdapter = new ItemCityAdapter(this,cities);
        itemCityAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(itemCityAdapter);
    }

    private void initProgressBar() {
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClickListener(View view, int position) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TAG, cities.get(position).getId());
        editor.apply();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_donwload_data_for_city,cities.get(position).getName()));
        builder.setNegativeButton(getString(android.R.string.no), null);
        builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ParseDAO parseDAO = new ParseDAO(getBaseContext());
                //parseDAO.insertItineraries();
                //parseDAO.insertCodes();
                //TODO implementa download do firebase por cidades especificas
                //Com progress bar
                if(initial){
                    ManagerInit.manager(SelectCityActivity.this);
                }else{
                    SelectCityActivity.this.finish();
                }
            }
        });
        builder.show();






    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }


    /**
    public void done(List<ParseObject> objects, ParseException e) {
        ParseDAO dao = new ParseDAO(this);
        for (ParseObject object : objects) {
            cities.add(dao.parseToCity(object));
        }
        ItemCityAdapter itemCityAdapter = new ItemCityAdapter(this,cities);
        itemCityAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewCities.setAdapter(itemCityAdapter);
    }*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        //TODO implementar falta de conexão de internet
        cities = new ArrayList<>();
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            City city = postSnapshot.getValue(City.class);
            cities.add(city);
        }


        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        TimesDbHelper db = new TimesDbHelper(this);
        db.insertCities(cities);



    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        City city = dataSnapshot.getValue(City.class);
        cities.add(city);
        //TimesDbHelper db = new TimesDbHelper(this);
        //db.insertCity(city);
        refreshRecyclerView();
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        City city = dataSnapshot.getValue(City.class);
        cities.add(city);
        //TimesDbHelper db = new TimesDbHelper(this);
        //db.insertCity(city);
        refreshRecyclerView();
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
