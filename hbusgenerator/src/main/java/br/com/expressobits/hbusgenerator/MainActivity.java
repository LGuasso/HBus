package br.com.expressobits.hbusgenerator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import br.com.expressobits.hbus.dao.FirebaseDAO;
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
        return file.getItineraries(city);
    }

    public List<Code> readFileCode(City city){
        return file.getCodes(city);
    }

    public List<Bus> readFileBus(City city,Itinerary itinerary,String way,String typeday){
        return file.getBuses(city, itinerary, way, typeday);
    }

    public void save(FirebaseDAO firebaseDAO){
        lookmessage("  >Inserindo cities");
        List<City> cities = readFileCity();
        for(City city:cities){
            //cities.get(0).setId(i + 1l);
            firebaseDAO.insert(city);
            saveOnFirebaseCode(firebaseDAO, city);
            saveOnFirebaseItinerary(firebaseDAO, city);

        }
    }

    private void saveOnFirebaseItinerary(FirebaseDAO firebaseDAO,City city) {
        lookmessage(">Inserindo itineraries");
        List<Itinerary> itineraries = readFileItinerary(city);
        for(Itinerary itinerary:itineraries){
            firebaseDAO.insert(city, itinerary);
            saveOnFirebaseBus(firebaseDAO, city, itinerary);
        }
    }

    private void saveOnFirebaseCode(FirebaseDAO firebaseDAO,City city) {
        lookmessage("  >Inserindo codes");
        List<Code> codes = readFileCode(city);
        for(Code code:codes){
            firebaseDAO.insert(city,code);
        }
    }

    private void saveOnFirebaseBus(FirebaseDAO firebaseDAO,City city,Itinerary itinerary) {
        lookmessage("  >Inserindo buses");
        for(int i=0;i<itinerary.getWays().size();i++){
            for(int j=0;j<3;j++){

                String way = itinerary.getWays().get(i);
                String typeday = TextUtils.getTypeDayInt(j);

                List<Bus> buses1 = readFileBus(city,itinerary,way,typeday);
                for (Bus bus:buses1){
                    firebaseDAO.insert(city,itinerary,bus,way,typeday);
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


        switch (v.getId()){
            case R.id.fab:
                FirebaseDAO.setContext(this);
                FirebaseDAO firebase = new FirebaseDAO("https://hbus.firebaseio.com/");
                new AsyncTask<FirebaseDAO,String,String>(){

                    @Override
                    protected String doInBackground(FirebaseDAO... params) {
                        save(params[0]);
                        return null;
                    }
                }.doInBackground(firebase);
                break;
            case R.id.button_read_data_cities:
                DbManager.getInstance(MainActivity.this).saveCities();
                break;

            case R.id.button_delete_all_data:
                DbManager.getInstance(MainActivity.this).deleteAllData();
                break;
            case R.id.button_delete_all_data_firebase:
                FirebaseDAO.setContext(this);
                FirebaseDAO base = new FirebaseDAO("https://hbus.firebaseio.com/");
                base.removeAllValues();
                break;

        }


        //deleteAllData();
        //BusDAO dao = new BusDAO(this);
        //dao.createTables();
        //saveCities();
    }


}
