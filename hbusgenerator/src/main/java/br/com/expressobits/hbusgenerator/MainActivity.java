package br.com.expressobits.hbusgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import br.com.expressobits.hbus.file.ReadFileCloud;
import br.com.expressobits.hbus.model.City;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GENERATOR";
    private Button buttonSaveAllData;
    private Button buttonReadAllData;
    private Button buttonReadItineraries;
    private Button buttonPushAllData;
    private Button buttonDeleteAllDataFirebase;
    private FloatingActionButton fab;
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




    private void pushCitytoFirebase(City city){
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
                break;

            case R.id.button_save_in_database_all_data:
                //saveInDataBaseAllData(this);
                break;

            case R.id.button_push_all_data:
                break;
            case R.id.button_delete_all_data_firebase:
                break;

        }

    }



}
