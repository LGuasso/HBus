package br.com.expressobits.hbus.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ItemCityAdapter;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.gae.ProgressAsyncTask;
import br.com.expressobits.hbus.gae.PullCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.ui.ManagerInit;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.dialog.DownloadDataDialogFragment;
import br.com.expressobits.hbus.ui.dialog.FinishListener;
import br.com.expressobits.hbus.util.NetworkUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectCityActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack,
        FinishListener, ProgressAsyncTask,ResultListenerAsyncTask<City> {

    private List<City> cities;
    public boolean initial = false;
    public static final String TAG = "city";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView imageViewNetworkError;
    private BusDAO db;
    private boolean starter = false;
    public static final String NOT_CITY = "not_city";
    public static final String STARTER_MODE = "starter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        starter = getIntent().getBooleanExtra(STARTER_MODE,false);
        setContentView(R.layout.activity_select_city);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_action_refresh) {
            refreshDatabaseCities("RS");
        }
        return false;
    }

    private void initActionBar() {
        //Define se e primeira vez do app ou se tem alguma cidade definida...
        initial = (PreferenceManager.getDefaultSharedPreferences(this).getString(TAG,NOT_CITY).equals(NOT_CITY));
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

        db = new BusDAO(this);
        cities = db.getCities();
        refreshRecyclerView();
        //Se houver cidades no database local não haverá procura no remoto
        if(cities.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            refreshDatabaseCities("RS");
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
        Log.d(TAG, "Selection city id=" + cities.get(position).getId());

        /**AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_donwload_data_for_city, cities.get(position).getName()));
        builder.setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SelectCityActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TAG,cities.get(position).getId());
                editor.apply();

                finish();
            }
        });
        builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SelectCityActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TAG,cities.get(position).getId());
                editor.apply();
                //TODO implementar download do cloud datastore
                //DownloadDataDialogFragment dialoge = new DownloadDataDialogFragment();
                //dialoge.setParameters(SelectCityActivity.this,SelectCityActivity.this,cities.get(position));
                //dialoge.show(SelectCityActivity.this.getSupportFragmentManager(), "DOWNLOAD");
                //TODO implementa download do firebase por cidades especificas
                //Com progress bar/
                finish();

            }
        });
        builder.show();*/

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SelectCityActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG, cities.get(position).getId());
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

    @Override
    public void setProgressUdate(Integer progress, Class c) {
        Toast.makeText(this,"Download cities "+progress+"%",Toast.LENGTH_LONG).show();
    }

    public void refreshDatabaseCities(String country){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        if(NetworkUtils.isWifiConnected(this) || NetworkUtils.isMobileConnected(this)){
            PullCitiesEndpointsAsyncTask pullCitiesEndpointsAsyncTask = new PullCitiesEndpointsAsyncTask();
            pullCitiesEndpointsAsyncTask.setProgressAsyncTask(this);
            pullCitiesEndpointsAsyncTask.setContext(this);
            pullCitiesEndpointsAsyncTask.setResultListenerAsyncTask(this);
            pullCitiesEndpointsAsyncTask.execute(country);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            imageViewNetworkError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finished(List result) {
        if(result!=null){
            cities = result;
            for (City city:cities){
                db.insert(city);
            }
            Log.i(TAG,cities.toString());
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            //imageViewNetworkError.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
        refreshRecyclerView();

    }
}
