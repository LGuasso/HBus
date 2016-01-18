package br.com.expressobits.hbusgenerator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.dao.FirebaseDAO;
import br.com.expressobits.hbus.dao.BusContract;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.file.ReadFile;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GENERATOR";
    private static long countBus = 1;
    private Button buttonReadCities;
    private Button buttonReadItineraries;
    private Button buttonDeleteAllData;
    private Button buttonDeleteAllDataFirebase;
    private FloatingActionButton fab;
    private List<City> cities;
    private List<Itinerary> itineraries;
    private Spinner spinnerCities;
    ReadFile file = new ReadFile(MainActivity.this);

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
        buttonReadCities = (Button) findViewById(R.id.button_read_data_cities);
        buttonReadItineraries = (Button) findViewById(R.id.button_read_data_itineraries);
        buttonDeleteAllData = (Button) findViewById(R.id.button_delete_all_data);
        buttonDeleteAllDataFirebase = (Button) findViewById(R.id.button_delete_all_data_firebase);
        buttonReadCities.setOnClickListener(this);
        buttonReadItineraries.setOnClickListener(this);
        buttonDeleteAllData.setOnClickListener(this);
        buttonDeleteAllDataFirebase.setOnClickListener(this);
    }

    public List<City> readFileCity(){
        return file.getCities();
    }

    public List<Itinerary> readFileItinerary(City city){
        return file.getItineraries(city.getId(), city.getName(), city.getCountry());
    }

    public List<Code> readFileCode(City city){
        return file.getCodes(city.getId(), city.getName(), city.getCountry());
    }

    public List<Bus> readFileBus(City city,Itinerary itinerary,String way,String typeday){
        return file.getBuses(city.getId(), city.getName(), city.getCountry(), itinerary, way, typeday);
    }

    public void save(FirebaseDAO firebaseDAO){
        lookmessage("  >Inserindo cities");
        List<City> cities = readFileCity();
        for(City city:cities){
            //cities.get(0).setId(i + 1l);
            firebaseDAO.insert(city);
            //saveOnFirebaseItinerary(firebaseDAO,cities.get(i));
            //saveOnFirebaseCode(firebaseDAO,cities.get(i));
        }
    }

    private void saveOnFirebaseItinerary(FirebaseDAO firebaseDAO,City city) {
        lookmessage("  >Inserindo itineraries");
        List<Itinerary> itineraries = readFileItinerary(city);
        for(int i=0;i<itineraries.size();i++){
            itineraries.get(i).setId(i + 1l);
            firebaseDAO.insert(itineraries.get(i));
            saveOnFirebaseBus(firebaseDAO, city, itineraries.get(i));
        }
    }

    private void saveOnFirebaseCode(FirebaseDAO firebaseDAO,City city) {
        lookmessage("  >Inserindo codes");
        List<Code> codes = readFileCode(city);
        for(int i=0;i<codes.size();i++){
            codes.get(i).setId(i + 1l);
            firebaseDAO.insert(codes.get(i));
        }
    }

    private void saveOnFirebaseBus(FirebaseDAO firebaseDAO,City city,Itinerary itinerary) {
        lookmessage("  >Inserindo buses");
        for(int i=0;i<itinerary.getWays().size();i++){
            for(int j=0;j<3;j++){

                List<Bus> buses1 = readFileBus(city,itinerary,itinerary.getWays().get(i),TextUtils.getTypeDayInt(j));
                for (Bus bus:buses1){
                    bus.setId(countBus++);
                    firebaseDAO.insert(bus);
                }
            }

        }


    }


    public void lookmessage(String message){
        Snackbar.make(findViewById(R.id.fab),message,Snackbar.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        String text;

        switch (v.getId()){
            case R.id.fab:
                FirebaseDAO.setContext(this);
                final FirebaseDAO firebase = new FirebaseDAO("https://hbus.firebaseio.com/");
                new AsyncTask<FirebaseDAO,String,String>(){

                    @Override
                    protected String doInBackground(FirebaseDAO... params) {
                        save(firebase);
                        return null;
                    }
                };
                break;
            case R.id.button_read_data_cities:
                DbManager.getInstance(MainActivity.this).saveCities();
                break;

            case R.id.button_delete_all_data:
                DbManager.getInstance(MainActivity.this).deleteAllData();
                break;
            case R.id.button_delete_all_data_firebase:
                FirebaseDAO.setContext(this);
                final FirebaseDAO base = new FirebaseDAO("https://hbus.firebaseio.com/");
                base.removeAllValues();
                break;

        }


        //deleteAllData();
        //BusDAO dao = new BusDAO(this);
        //dao.createTables();
        //saveCities();
    }


}
