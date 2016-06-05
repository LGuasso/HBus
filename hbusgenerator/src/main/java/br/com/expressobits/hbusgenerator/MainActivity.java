package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAOGenerator;
import br.com.expressobits.hbus.file.ReadFileCloud;
import br.com.expressobits.hbus.gae.ProgressAsyncTask;
import br.com.expressobits.hbus.gae.PushBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushCodesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.utils.DAOUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,ProgressAsyncTask{

    private static final String TAG = "GENERATOR";
    private Button buttonSaveAllData;
    private Button buttonReadAllData;
    private Button buttonReadItineraries;
    private Button buttonPushAllData;
    private Button buttonDeleteAllDataFirebase;
    private FloatingActionButton fab;
    private List<City> cities = new ArrayList<>();
    private HashMap<City,List<Itinerary>> itineraries = new HashMap<>();
    private HashMap<City,List<Code>> codes = new HashMap<>();
    private HashMap<City,HashMap<Itinerary,List<Bus>>> buses = new HashMap<>();
    private Spinner spinnerCities;
    ReadFileCloud file = new ReadFileCloud(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        buttonReadAllData = (Button) findViewById(R.id.button_read_all_data);
        buttonSaveAllData = (Button) findViewById(R.id.button_save_in_database_all_data);
        buttonReadItineraries = (Button) findViewById(R.id.button_read_data_itineraries);
        buttonPushAllData = (Button) findViewById(R.id.button_push_all_data);
        buttonDeleteAllDataFirebase = (Button) findViewById(R.id.button_delete_all_data_firebase);
        buttonReadAllData.setOnClickListener(this);
        buttonSaveAllData.setOnClickListener(this);
        buttonReadItineraries.setOnClickListener(this);
        buttonPushAllData.setOnClickListener(this);
        buttonDeleteAllDataFirebase.setOnClickListener(this);
        spinnerCities = (Spinner) findViewById(R.id.spinner_cities);
    }



    /**public void readFiles(){
        cities = file.getCities();

        for(City city:cities){

                itineraries.put(city, file.getItineraries(city));
                codes.put(city, file.getCodes(city));
                buses.put(city,file.getBuses(city,itineraries.get(city)));


        }



    }*/

    /**public void saveInDataBaseAllData(Context context){
        BusDAOGenerator dao = new BusDAOGenerator(context);
        for(City city:cities){

            //Somente cidade de Santa Maria será salva!
            if(city.getName().equals("Santa Maria")){
                dao.insertCity(city.clone().setId(DAOUtils.getId(city)));
                for(Itinerary itinerary : itineraries.get(city)){
                    dao.insertItineraries(itinerary.clone().setId(DAOUtils.getId(city,itinerary)));
                    for(Bus bus:buses.get(city).get(itinerary)) {
                        dao.insertBus(bus.clone().setId(DAOUtils.getId(city,itinerary,bus)));
                    }
                }
                for(Code code : codes.get(city)){
                    dao.insertCodes(code.clone().setId(DAOUtils.getId(city,code)));
                }
            }
        }
    }*/



    public void pushAll(){
        City city = (City)spinnerCities.getSelectedItem();
        pushCity(city);
        for (Itinerary itinerary : itineraries.get(city)) {
            pushItinerary(city, itinerary);
            for (Bus bus : buses.get(city).get(itinerary)) {
                Log.e(TAG, "TEST " + bus.getTime());
                if(itinerary.getName().equals("Vila Oliveira")) {
                    pushBus(city, itinerary, bus);
                }
            }
        }
        for (Code code : codes.get(city)) {
            pushCode(city, code);
        }
    }

    private void pushCitytoFirebase(City city){
    }



    private void pushCity(City city){
        PushCitiesEndpointsAsyncTask pushCitiesEndpointsAsyncTask = new PushCitiesEndpointsAsyncTask();
        pushCitiesEndpointsAsyncTask.setContext(this);
        pushCitiesEndpointsAsyncTask.setProgressAsyncTask(this);
        //pushCitiesEndpointsAsyncTask.execute(city);
    }

    private void pushItinerary(City city,Itinerary itinerary){
        PushItinerariesEndpointsAsyncTask pushItinerariesEndpointsAsyncTask = new PushItinerariesEndpointsAsyncTask();
        pushItinerariesEndpointsAsyncTask.setContext(this);
        pushItinerariesEndpointsAsyncTask.setProgressAsyncTask(this);
        pushItinerariesEndpointsAsyncTask.setCityName(city.getName());
        pushItinerariesEndpointsAsyncTask.setCountry(city.getCountry());
        pushItinerariesEndpointsAsyncTask.execute(itinerary);
    }

    private void pushCode(City city,Code code){
        PushCodesEndpointsAsyncTask pushCodesEndpointsAsyncTask = new PushCodesEndpointsAsyncTask();
        pushCodesEndpointsAsyncTask.setContext(this);
        pushCodesEndpointsAsyncTask.setProgressAsyncTask(this);
        pushCodesEndpointsAsyncTask.setCityName(city.getName());
        pushCodesEndpointsAsyncTask.setCountry(city.getCountry());
        pushCodesEndpointsAsyncTask.execute(code);
    }

    private void pushBus(City city,Itinerary itinerary,Bus bus){
        PushBusEndpointsAsyncTask pushBusEndpointsAsyncTask = new PushBusEndpointsAsyncTask();
        pushBusEndpointsAsyncTask.setContext(this);
        pushBusEndpointsAsyncTask.setProgressAsyncTask(this);
        pushBusEndpointsAsyncTask.setCountry(city.getCountry());
        pushBusEndpointsAsyncTask.setCityName(city.getName());
        pushBusEndpointsAsyncTask.setItineraryName(itinerary.getName());
        pushBusEndpointsAsyncTask.execute(bus);
    }





    public void lookmessage(String message){
        Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_SHORT).show();
    }

    public void refreshSpinner(List<City> cities){
        ArrayAdapter<City> simpleAdapter = new ArrayAdapter<City>(this,android.R.layout.simple_list_item_1,cities);
        spinnerCities.setAdapter(simpleAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_data_manager) {
            startActivity(new Intent(this,FirebaseActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.fab:

                break;
            case R.id.button_read_all_data:
                //readFiles();
                refreshSpinner(cities);
                break;

            case R.id.button_save_in_database_all_data:
                //saveInDataBaseAllData(this);
                break;

            case R.id.button_push_all_data:
                pushAll();
                break;
            case R.id.button_delete_all_data_firebase:
                break;

        }

    }



    @Override
    public void setProgressUdate(Integer progress,Class c) {
        if(c.equals(City.class)){
            Toast.makeText(this,"Porcent test "+progress.toString()+"%",Toast.LENGTH_LONG).show();
        }

    }
}
