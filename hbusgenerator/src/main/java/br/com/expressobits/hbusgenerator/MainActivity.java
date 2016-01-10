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
    private ReadFile file;
    private static long countBus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    public List<City> readFileCity(){
        ReadFile file = new ReadFile(this);
        return file.getCities();
    }

    public List<Itinerary> readFileItinerary(City city){
        ReadFile file = new ReadFile(this);
        return file.getItineraries(city.getId(), city.getName(), city.getCountry());
    }

    public List<Code> readFileCode(City city){
        ReadFile file = new ReadFile(this);
        return file.getCodes(city.getId(), city.getName(), city.getCountry());
    }

    public List<Bus> readFileBus(City city,Itinerary itinerary,String way,String typeday){
        ReadFile file = new ReadFile(this);
        return file.getBuses(city.getId(), city.getName(), city.getCountry(), itinerary, way, typeday);
    }

    public void save(FirebaseDAO firebaseDAO){
        lookmessage("  >Inserindo cities");
        List<City> cities = readFileCity();
        for(int i=0;i<cities.size();i++){
            cities.get(i).setId(i + 1l);
            firebaseDAO.insert(cities.get(i));
            saveOnFirebaseItinerary(firebaseDAO,cities.get(i));
            saveOnFirebaseCode(firebaseDAO,cities.get(i));
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



    public void saveCities(){
        Log.i(TAG, "Inserindo cidades");
        BusDAO dao = new BusDAO(this);
        ReadFile file = new ReadFile(this);
        for(City city:file.getCities()){
            Long cityId = dao.insertCity(city);
            city.setId(cityId);
            Log.i(TAG, "> " + city.getName());
            saveCodes(city);
            saveItineraries(city);

        }
    }

    public void saveItineraries(City city){
        Log.i(TAG,"  >Inserindo itinerarios");
        BusDAO dao = new BusDAO(this);
        ReadFile file = new ReadFile(this);
        for(Itinerary itinerary:file.getItineraries(city.getId(), city.getName(), city.getCountry())){
            itinerary.setId(dao.insertItineraries(itinerary));
            Log.i(TAG, "> > " + itinerary.getName());
            saveBuses(city,itinerary);
        }
    }

    public void saveCodes(City city){
        Log.i(TAG,"  >Inserindo codes");
        BusDAO dao = new BusDAO(this);
        ReadFile file = new ReadFile(this);
        for(Code code:file.getCodes(city.getId(), city.getName(), city.getCountry())){
            dao.insertCodes(code);
            Log.i(TAG,"> > "+code.getName());
        }
    }


    private void saveBuses(City city,Itinerary itinerary){
        Log.i(TAG,"  >Inserindo buses");
        BusDAO dao = new BusDAO(this);
        ReadFile file = new ReadFile(this);
        for(int i=0;i<itinerary.getWays().size();i++){
            for(int j=0;j<3;j++){
                Log.i(TAG,"  > >>>>Inserindo buses");
                List<Bus> buses = new ArrayList<>();
                buses = file.getBuses(city.getId(),
                        city.getName(),
                        city.getCountry(),
                        itinerary,itinerary.getWays().get(i),
                        TextUtils.getTypeDayInt(j));
                for (Bus bus:buses){
                    dao.insertBus(bus);
                    Log.i(TAG, "> > >" + bus.getTime());
                }

            }
        }
    }

    public void deleteAllData(){
        BusDAO dao = new BusDAO(this);
        dao.deleteTable(CityContract.City.TABLE_NAME);
        dao.deleteTable(ItineraryContract.Itinerary.TABLE_NAME);
        dao.deleteTable(CodeContract.Code.TABLE_NAME);
        dao.deleteTable(BusContract.Bus.TABLE_NAME);
        dao.close();
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
        FirebaseDAO.setContext(this);
        final FirebaseDAO firebase = new FirebaseDAO("https://hbus.firebaseio.com/");
        new AsyncTask<FirebaseDAO,String,String>(){

            @Override
            protected String doInBackground(FirebaseDAO... params) {
                save(firebase);
                return null;
            }
        };

        //deleteAllData();
        //BusDAO dao = new BusDAO(this);
        //dao.createTables();
        //saveCities();
    }


}
