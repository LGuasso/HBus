package br.com.expressobits.hbusgenerator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.database.PushBusesASyncTask;
import br.com.expressobits.hbus.database.PushCitiesASyncTask;
import br.com.expressobits.hbus.database.PushCodesASyncTask;
import br.com.expressobits.hbus.database.PushCompaniesASyncTask;
import br.com.expressobits.hbus.database.PushItinerariesASyncTask;
import br.com.expressobits.hbus.file.ReadFile;
import br.com.expressobits.hbus.gae.ProgressAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.DAOUtils;

public class FirebaseActivity extends Activity implements ResultListenerAsyncTask<Integer>,ProgressAsyncTask{


    ReadFile readFile;

    List<City> cities = new ArrayList<>();
    HashMap<City,List<Company>> companies = new HashMap<>();
    HashMap<City,HashMap<Company,List<Code>>> codes = new HashMap<>();
    HashMap<City,HashMap<Company,List<Itinerary>>> itineraries = new HashMap<>();
    HashMap<City,HashMap<Company,HashMap<Itinerary,List<Bus>>>> buses = new HashMap<>();

    TextView porcentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        readFile = new ReadFile(this);
        readData();
        pushData();

        porcentTextView = (TextView) findViewById(R.id.porcent);
    }


    private void readData(){
        cities = readFile.getCities();
        for(City city:cities){
            companies.put(city,readFile.getCompanies(city));
            HashMap<Company,List<Code>> listCodes = new HashMap<>();
            HashMap<Company,List<Itinerary>> listItineraries = new HashMap<>();
            HashMap<Company,HashMap<Itinerary,List<Bus>>> listBuses = new HashMap<>();
            for(Company company:companies.get(city)){
                listCodes.put(company,readFile.getCodes(city,company));

                List<Itinerary> list = readFile.getItineraries(city,company);
                listItineraries.put(company,list);
                listBuses.put(company,readFile.getBuses(city,company,list));

                /**HashMap<Company,List<Itinerary>> listItineraries = new HashMap<>();
                listItineraries.put(company,readFile.getItineraries(city,company));
                itineraries.put(city,listItineraries);

                HashMap<Company,HashMap<Itinerary,List<Bus>>> listHashMap = new HashMap<>();
                HashMap<Itinerary,List<Bus>> listHashMap1 = readFile.getBuses(city,company,itineraries.get(city).get(company));
                listHashMap.put(company,listHashMap1);
                buses.put(city,listHashMap);
                //buses.put(city,readFile.getBuses(city,itineraries.get(city,company)));*/
            }
            codes.put(city,listCodes);
            itineraries.put(city,listItineraries);
            buses.put(city,listBuses);
        }

    }

    private void removeAllValues(String table,City city,Company company,Itinerary itinerary){
        Log.e("FIREBASE","Remove " +table);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference tableRef = database.getReference(table);
        DatabaseReference countryRef = tableRef.child(city.getCountry());
        DatabaseReference cityRef = countryRef.child(city.getName());
        DatabaseReference companyRef = cityRef.child(company.getName());
        DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
        itineraryRef.removeValue();
    }

    private void removeAllValues(){
        Log.e("FIREBASE","Remove all VALUES!" );
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().removeValue();
    }

    private void pushData(){
        removeAllValues();
        for(City city:cities){
            push(city);
            for(Company company:companies.get(city)){
                push(city,company);
                Log.e("FIREBASE","\tcompany "+company.getName());
                List<Code> codesList = codes.get(city).get(company);
                List<Itinerary> itineraryList = itineraries.get(city).get(company);
                HashMap<Itinerary,List<Bus>> busList = buses.get(city).get(company);
                if(codesList==null){
                    Log.e("FIREBASE","\t\tcodeList null!"+company.getName());
                }else{
                    for(Code code:codesList){
                        push(city,company,code);
                    }
                    Log.d("FIREBASE","\t\tcodeList size!"+codesList.size());
                }

                if(itineraryList==null){
                    Log.e("FIREBASE","\t\titineraryList null!"+company.getName());
                }else{
                    for(Itinerary itinerary:itineraryList){
                        push(city,company,itinerary);

                        List<Bus> busLited = busList.get(itinerary);
                        if(busLited==null){
                            Log.e("FIREBASE","\t\t\tbusLited null!"+itinerary.getName());
                        }else{
                            removeAllValues("buses",city,company,itinerary);
                            push(city,company,itinerary,busLited);
                            Log.d("FIREBASE","\t\t\tbusLited size!"+busLited.size());
                        }

                    }
                    Log.d("FIREBASE","\t\titineraryList size!"+itineraryList.size());
                }

            }
        }
    }

    private void push(City city){
        PushCitiesASyncTask pushCitiesASyncTask = new PushCitiesASyncTask();
        pushCitiesASyncTask.setProgressAsyncTask(null);
        pushCitiesASyncTask.setResultListenerAsyncTask(null);
        pushCitiesASyncTask.execute(city);
    }

    private void push(City city,Company company){
        PushCompaniesASyncTask pushCompaniesASyncTask = new PushCompaniesASyncTask();
        pushCompaniesASyncTask.setProgressAsyncTask(null);
        pushCompaniesASyncTask.setResultListenerAsyncTask(null);
        Pair<City,Company> pair = new Pair<>(city,company);
        pushCompaniesASyncTask.execute(pair);
    }

    private void push(City city,Company company,Code code){
        PushCodesASyncTask pushCodesASyncTask = new PushCodesASyncTask();
        pushCodesASyncTask.setProgressAsyncTask(null);
        pushCodesASyncTask.setResultListenerAsyncTask(null);
        Pair<Company,Code> pairCompany = new Pair<>(company,code);
        Pair<City,Pair<Company,Code>> pair = new Pair<>(city,pairCompany);
        pushCodesASyncTask.execute(pair);
    }

    private void push(City city,Company company,Itinerary itinerary){
        PushItinerariesASyncTask pushItinerariesASyncTask = new PushItinerariesASyncTask();
        pushItinerariesASyncTask.setProgressAsyncTask(null);
        pushItinerariesASyncTask.setResultListenerAsyncTask(null);
        Pair<Company,Itinerary> pairItinerary = new Pair<>(company,itinerary);
        Pair<City,Pair<Company,Itinerary>> pair = new Pair<>(city,pairItinerary);
        pushItinerariesASyncTask.execute(pair);
    }

    private void push(City city,Company company,Itinerary itinerary,List<Bus> buses){
        PushBusesASyncTask pushBusesASyncTask = new PushBusesASyncTask();
        pushBusesASyncTask.setProgressAsyncTask(null);
        pushBusesASyncTask.setResultListenerAsyncTask(this);
        Pair<Itinerary,List<Bus>> pairBus = new Pair<>(itinerary,buses);
        Pair<Company,Pair<Itinerary,List<Bus>>> pairItinerary = new Pair<>(company,pairBus);
        Pair<City,Pair<Company,Pair<Itinerary,List<Bus>>>> pair = new Pair<>(city,pairItinerary);
        pushBusesASyncTask.execute(pair);
    }



    @Override
    public void finished(List<Integer> integers) {
        /**count++;
        porcentTextView.setText((count/itineraries.size())*100);*/
    }

    @Override
    public void setProgressUdate(Integer progress, Class c) {

    }
}
