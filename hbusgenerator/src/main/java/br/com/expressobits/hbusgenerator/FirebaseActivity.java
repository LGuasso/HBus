package br.com.expressobits.hbusgenerator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.database.PushBusesASyncTask;
import br.com.expressobits.hbus.database.PushCitiesASyncTask;
import br.com.expressobits.hbus.database.PushCodesASyncTask;
import br.com.expressobits.hbus.database.PushCompaniesASyncTask;
import br.com.expressobits.hbus.database.PushItinerariesASyncTask;
import br.com.expressobits.hbus.file.ReadFile;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.DAOUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class FirebaseActivity extends AppCompatActivity implements
        View.OnClickListener,CompoundButton.OnCheckedChangeListener{


    ReadFile readFile;

    private static final String COUNTRY = "BR/RS";

    private Button buttonRead;
    private CheckBox checkBoxCities;
    private Spinner spinnerCities;
    private CheckBox checkBoxCompanies;
    private Spinner spinnerCompanies;
    private CheckBox checkBoxItineraries;
    private Spinner spinnerItineraries;

    List<City> cities = new ArrayList<>();
    HashMap<City, List<Company>> companies = new HashMap<>();
    HashMap<City, HashMap<Company, List<Code>>> codes = new HashMap<>();
    HashMap<City, HashMap<Company, List<Itinerary>>> itineraries = new HashMap<>();
    HashMap<City, HashMap<Company, HashMap<Itinerary, List<Bus>>>> buses = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        initViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Firebase");

        readFile = new ReadFile(this);

        /**removeAllValues(FirebaseUtils.BUS_TABLE);
         removeAllValues(FirebaseUtils.ITINERARY_TABLE);
         removeAllValues(FirebaseUtils.CITY_TABLE);
         removeAllValues(FirebaseUtils.COMPANY_TABLE);
         removeAllValues(FirebaseUtils.CODE_TABLE);*/

    }

    private void initViews() {
        buttonRead = (Button) findViewById(R.id.buttonsRead);
        checkBoxCities = (CheckBox) findViewById(R.id.checkboxCities);
        spinnerCities = (Spinner) findViewById(R.id.spinnerCities);
        checkBoxCompanies = (CheckBox) findViewById(R.id.checkboxCompanies);
        spinnerCompanies = (Spinner) findViewById(R.id.spinnerCompanies);
        checkBoxItineraries = (CheckBox) findViewById(R.id.checkboxItineraries);
        spinnerItineraries = (Spinner) findViewById(R.id.spinnerItineraries);

        buttonRead.setOnClickListener(this);
        checkBoxCities.setOnCheckedChangeListener(this);
        checkBoxCompanies.setOnCheckedChangeListener(this);
        checkBoxItineraries.setOnCheckedChangeListener(this);
    }

    private void loadItensOnSpinnerCity(List<City> cities){
        ArrayAdapter<City> arrayAdapter = new ArrayAdapter<City>(this,
                android.R.layout.simple_list_item_1,cities);
        spinnerCities.setAdapter(arrayAdapter);
    }

    private void loadItensOnSpinnerCompany(List<Company> companies){
        ArrayAdapter<Company> arrayAdapter = new ArrayAdapter<Company>(this,
                android.R.layout.simple_list_item_1,companies);
        spinnerCompanies.setAdapter(arrayAdapter);
    }

    private void loadItensOnSpinnerItinerary(List<Itinerary> itineraries){
        ArrayAdapter<Itinerary> arrayAdapter = new ArrayAdapter<Itinerary>(this,
                android.R.layout.simple_list_item_1,itineraries);
        spinnerItineraries.setAdapter(arrayAdapter);
    }


    private void readData(String country) {
        readFile.setCountry(country);
        cities = readFile.getCities(country);
        for (City city : cities) {
            companies.put(city, readFile.getCompanies(city));
            HashMap<Company, List<Code>> listCodes = new HashMap<>();
            HashMap<Company, List<Itinerary>> listItineraries = new HashMap<>();
            HashMap<Company, HashMap<Itinerary, List<Bus>>> listBuses = new HashMap<>();
            for (Company company : companies.get(city)) {
                listCodes.put(company, readFile.getCodes(city, company));

                List<Itinerary> itineraries = readFile.getItineraries(city, company);
                listItineraries.put(company, itineraries);
                listBuses.put(company, readFile.getBuses(city, company, itineraries));

            }
            codes.put(city, listCodes);
            itineraries.put(city, listItineraries);
            buses.put(city, listBuses);
        }
        Toast.makeText(this,"Read sucesso!",Toast.LENGTH_SHORT).show();

    }

    private void removeAllValues(String table) {
        Log.e("FIREBASE", "Remove all VALUES!");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference tableRef = database.getReference(table);

        tableRef.removeValue();
    }

    //PUSH
    private void push(){
        if(checkBoxCities.isChecked()){
            pushCity(cities.get(spinnerCities.getSelectedItemPosition()));
        }else {
            pushAllCities();
        }
    }

    private void pushAllCities(){
        for(City city:cities){
            pushCity(city);
        }
    }


    private void pushCity(City city) {
        push(city);
        for (Company company : companies.get(city)) {
            pushCompany(city, company);
        }
    }

    private void pushCompany(City city, Company company) {
        push(city, company);
        Log.e("FIREBASE", "\tcompany " + company.getName());
        List<Code> codesList = codes.get(city).get(company);
        List<Itinerary> itineraryList = itineraries.get(city).get(company);
        if (codesList == null) {
            Log.e("FIREBASE", "\t\tcodeList null!" + company.getName());
        } else {
            for (Code code : codesList) {
                push(city, company, code);
            }
            Log.d("FIREBASE", "\t\tcodeList size!" + codesList.size());
        }
        if (itineraryList == null) {
            Log.e("FIREBASE", "\t\titineraryList null!" + company.getName());
        } else {
            for (Itinerary itinerary : itineraryList) {
                pushItinerary(city, company, itinerary);

            }
            Log.d("FIREBASE", "\t\titineraryList size!" + itineraryList.size());
        }
    }

    private void pushItinerary(City city, Company company, Itinerary itinerary) {
        push(city, company, itinerary);
        HashMap<Itinerary, List<Bus>> busList = buses.get(city).get(company);
        List<Bus> busLited = busList.get(itinerary);
        if (busLited == null) {
            Log.e("FIREBASE", "\t\t\tbusLited null!" + itinerary.getName());
        } else {
            push(city, company, itinerary, busLited);
            Log.d("FIREBASE", "\t\t\tbusLited size!" + busLited.size());
        }
    }

    private void push(City city) {
        PushCitiesASyncTask pushCitiesASyncTask = new PushCitiesASyncTask();
        pushCitiesASyncTask.execute(city);
    }

    private void push(City city, Company company) {
        PushCompaniesASyncTask pushCompaniesASyncTask = new PushCompaniesASyncTask();
        Pair<City, Company> pair = new Pair<>(city, company);
        pushCompaniesASyncTask.execute(pair);
    }

    private void push(City city, Company company, Code code) {
        PushCodesASyncTask pushCodesASyncTask = new PushCodesASyncTask();
        Pair<Company, Code> pairCompany = new Pair<>(company, code);
        Pair<City, Pair<Company, Code>> pair = new Pair<>(city, pairCompany);
        pushCodesASyncTask.execute(pair);
    }

    private void push(City city, Company company, Itinerary itinerary) {
        PushItinerariesASyncTask pushItinerariesASyncTask = new PushItinerariesASyncTask();
        Pair<Company, Itinerary> pairItinerary = new Pair<>(company, itinerary);
        Pair<City, Pair<Company, Itinerary>> pair = new Pair<>(city, pairItinerary);
        pushItinerariesASyncTask.execute(pair);

    }

    private void push(City city, Company company, Itinerary itinerary, List<Bus> buses) {
        PushBusesASyncTask pushBusesASyncTask = new PushBusesASyncTask();
        Pair<Itinerary, List<Bus>> pairBus = new Pair<>(itinerary, buses);
        Pair<Company, Pair<Itinerary, List<Bus>>> pairItinerary = new Pair<>(company, pairBus);
        Pair<City, Pair<Company, Pair<Itinerary, List<Bus>>>> pair = new Pair<>(city, pairItinerary);
        pushBusesASyncTask.execute(pair);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.checkboxCities:
                    spinnerCities.setEnabled(b);
                    checkBoxCompanies.setEnabled(b);
                    spinnerCompanies.setEnabled(b);
                    checkBoxItineraries.setEnabled(b);
                    spinnerItineraries.setEnabled(b);

                    if(b){
                        loadItensOnSpinnerCity(cities);
                    }
                break;
            case R.id.checkboxCompanies:
                spinnerCompanies.setEnabled(b);
                checkBoxItineraries.setEnabled(b);
                spinnerItineraries.setEnabled(b);
                if(b){
                    loadItensOnSpinnerCompany(companies.get(cities.get(spinnerCities.getSelectedItemPosition())));
                }
                break;
            case R.id.checkboxItineraries:
                spinnerItineraries.setEnabled(b);
                if(b){
                    City city = cities.get(spinnerCities.getSelectedItemPosition());
                    Company company = companies.get(city).get(spinnerCompanies.getSelectedItemPosition());
                    loadItensOnSpinnerItinerary(itineraries.get(city).get(company));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_firebase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_firebase) {
            if(checkBoxCities.isChecked()){
                City city = cities.get(spinnerCities.getSelectedItemPosition());
                if(checkBoxCompanies.isChecked()){
                    Company company = companies.get(city).get(spinnerCompanies.getSelectedItemPosition());
                    if(checkBoxItineraries.isChecked()){
                        Itinerary itinerary = itineraries.get(city).get(company).get(
                                spinnerItineraries.getSelectedItemPosition());
                        pushItinerary(city,company,itinerary);
                    }else {
                        pushCompany(city,company);
                    }

                }else{
                    pushCity(city);
                }
            }else {
                pushAllCities();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        readData(COUNTRY);
    }
}
