package br.com.expressobits.hbusgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.file.ReadFileCloud;
import hbus.model.Bus;
import hbus.model.City;
import hbus.model.Code;
import hbus.model.Itinerary;

/**
 * @deprecated
 */
public class DataManagerActivity extends AppCompatActivity /*implements View.OnClickListener,AdapterView.OnItemClickListener*/{

    private Button buttonReadCity;
    private Button buttonReadItinerary;
    private Button buttonReadCodes;
    private Button buttonReadBus;
    private Button buttonPushCity;
    private Button buttonPushItinerary;
    private Button buttonPushCodes;
    private Button buttonPushBus;
    private Spinner spinnerCity;
    private Spinner spinnerItinerary;
    private Spinner spinnerCodes;
    private Spinner spinnerBus;
    private Button buttonPushAll;
    private Button buttonPushAllCodes;
    private Button buttonPushAllItinerary;

    ReadFileCloud readFileCloud;

    private List<City> cities = new ArrayList<>();
    private HashMap<City,List<Itinerary>> itineraries = new HashMap<>();
    private HashMap<City,List<Code>> codes = new HashMap<>();
    private HashMap<City,HashMap<Itinerary,List<Bus>>> buses = new HashMap<>();

    /**private CityAdapter adapterCities;
    private ItineraryAdapter adapterItineraries;
    private CodeAdapter adapterCodes;
    private BusAdapter adapterBus;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manager);
        initComponents();

        readFileCloud = new ReadFileCloud(this);
    }
    
    private void initComponents(){
        buttonReadCity = (Button)findViewById(R.id.buttonReadCity);
        buttonReadItinerary = (Button)findViewById(R.id.buttonReadItineraries);
        buttonReadCodes = (Button)findViewById(R.id.buttonReadCodes);
        buttonReadBus = (Button)findViewById(R.id.buttonReadBus);
        buttonPushCity = (Button)findViewById(R.id.buttonPushCity);
        buttonPushItinerary = (Button)findViewById(R.id.buttonPushItineraries);
        buttonPushCodes = (Button)findViewById(R.id.buttonPushCodes);
        buttonPushBus = (Button)findViewById(R.id.buttonPushBus);
        buttonPushAll = (Button)findViewById(R.id.buttonPushAll);
        buttonPushAllCodes = (Button)findViewById(R.id.buttonAllCodes);
        buttonPushAllItinerary = (Button)findViewById(R.id.buttonAllItinerary);
        spinnerCity = (Spinner)findViewById(R.id.spinnerCities);
        spinnerItinerary = (Spinner)findViewById(R.id.spinnerItineraries);
        spinnerCodes = (Spinner)findViewById(R.id.spinnerCodes);
        spinnerBus = (Spinner)findViewById(R.id.spinnerBus);
        /*buttonReadCity.setOnClickListener(this);
        buttonReadItinerary.setOnClickListener(this);
        buttonReadCodes.setOnClickListener(this);
        buttonReadBus.setOnClickListener(this);
        buttonPushCity.setOnClickListener(this);
        buttonPushItinerary.setOnClickListener(this);
        buttonPushCodes.setOnClickListener(this);
        buttonPushBus.setOnClickListener(this);
        buttonPushAll.setOnClickListener(this);
        buttonPushAllItinerary.setOnClickListener(this);
        buttonPushAllCodes.setOnClickListener(this);*/
        buttonReadCity.setEnabled(true);
        buttonReadItinerary.setEnabled(false);
        buttonReadCodes.setEnabled(false);
        buttonReadBus.setEnabled(false);
        buttonPushCity.setEnabled(false);
        buttonPushItinerary.setEnabled(false);
        buttonPushCodes.setEnabled(false);
        buttonPushBus.setEnabled(false);
        buttonPushAll.setEnabled(true);
        buttonPushAllItinerary.setEnabled(true);
        buttonPushAllCodes.setEnabled(true);
        spinnerCity.setEnabled(false);
        spinnerItinerary.setEnabled(false);
        spinnerCodes.setEnabled(false);
        spinnerBus.setEnabled(false);


    }
/*
    @Override
    public void onClick(View v) {
        City city;
        switch (v.getId()){
            case R.id.buttonReadCity:
                adapterCities = new CityAdapter(this,cities);
                spinnerCity.setAdapter(adapterCities);
                spinnerCity.setEnabled(true);

                buttonReadItinerary.setEnabled(true);
                buttonReadCodes.setEnabled(true);

                buttonPushCity.setEnabled(true);
                buttonReadCity.setEnabled(false);
                break;
            case R.id.buttonReadItineraries:
                city = (City)spinnerCity.getSelectedItem();
                //itineraries.put(city, readFileCloud.getItineraries(city));
                adapterItineraries = new ItineraryAdapter(this,itineraries.get(city));
                spinnerItinerary.setAdapter(adapterItineraries);
                spinnerItinerary.setEnabled(true);

                buttonReadBus.setEnabled(true);

                buttonPushItinerary.setEnabled(true);
                buttonReadItinerary.setEnabled(false);
                break;
            case R.id.buttonReadCodes:
                city = (City)spinnerCity.getSelectedItem();
                //codes.put(city, readFileCloud.getCodes(city));
                adapterCodes = new CodeAdapter(this,codes.get(city));
                spinnerCodes.setAdapter(adapterCodes);
                spinnerCodes.setEnabled(true);


                buttonPushCodes.setEnabled(true);
                buttonReadCodes.setEnabled(false);
                break;

            case R.id.buttonReadBus:
                Itinerary itinerary = (Itinerary)spinnerItinerary.getSelectedItem();
                city = (City) spinnerCity.getSelectedItem();
                //buses.put(city, readFileCloud.getBuses(city,itineraries.get(city)));
                adapterBus = new BusAdapter(this,buses.get(city).get(itinerary));
                spinnerBus.setAdapter(adapterBus);
                spinnerBus.setEnabled(true);

                buttonPushAll.setEnabled(true);
                buttonPushAllCodes.setEnabled(true);
                buttonPushAllItinerary.setEnabled(true);
                buttonPushBus.setEnabled(true);
                buttonReadBus.setEnabled(false);
                break;

            case R.id.buttonPushItineraries:
                city = ((City)spinnerCity.getSelectedItem());
                itinerary = (Itinerary)spinnerItinerary.getSelectedItem();
                pushItinerary(city,itinerary);
                break;
            case R.id.buttonPushCity:
                city = ((City)spinnerCity.getSelectedItem());
                pushCity(city);
                break;
            case R.id.buttonPushCodes:
                city = ((City)spinnerCity.getSelectedItem());
                Code code = (Code)spinnerCodes.getSelectedItem();
                pushCode(city,code);
                break;

            case R.id.buttonPushAll:
                pushAll();
                break;
            case R.id.buttonAllItinerary:
                pushAllItinerary();
                break;
            case R.id.buttonAllCodes:
                pushAllCodes();
                break;

        }
    }*/

    /**private void pushCity(City city){
        PushCitiesEndpointsAsyncTask pushCitiesEndpointsAsyncTask = new PushCitiesEndpointsAsyncTask();
        pushCitiesEndpointsAsyncTask.setContext(this);
        pushCitiesEndpointsAsyncTask.setProgressAsyncTask(this);
        pushCitiesEndpointsAsyncTask.execute(city);
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

    public void pushAll(){
        City city = (City)spinnerCity.getSelectedItem();
        pushCity(city);
        for (Itinerary itinerary : itineraries.get(city)) {
            pushItinerary(city, itinerary);
            for (Bus bus : buses.get(city).get(itinerary)) {
                    pushBus(city, itinerary, bus);
            }
        }
        for (Code code : codes.get(city)) {
            pushCode(city, code);
        }
    }

    public void pushAllItinerary(){
        City city = (City)spinnerCity.getSelectedItem();
        Itinerary itinerary = (Itinerary) spinnerItinerary.getSelectedItem();
        pushCity(city);
        pushItinerary(city, itinerary);
        for (Bus bus : buses.get(city).get(itinerary)) {
            pushBus(city, itinerary, bus);
        }

    }

    public void pushAllCodes(){
        City city = (City)spinnerCity.getSelectedItem();
        pushCity(city);
        for (Code code : codes.get(city)) {
            pushCode(city, code);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spinnerItineraries:
                break;
        }
    }

    @Override
    public void setProgressUdate(Integer progress,Class c) {
        if(c.equals(City.class)){
            Toast.makeText(this,"Porcent test "+progress.toString()+"%",Toast.LENGTH_LONG).show();
        }

    }


    class CityAdapter extends BaseAdapter{

        private List<City> cities = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public CityAdapter(Context context,List<City> cities){
            this.cities = cities;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return cities.size();
        }

        @Override
        public Object getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            TextView textView =(TextView)view.findViewById(android.R.id.text1);
            textView.setText(cities.get(position).getName()+" - "+cities.get(position).getCountry());
            return view;
        }
    }

    class ItineraryAdapter extends BaseAdapter{

        private List<Itinerary> itineraries = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public ItineraryAdapter(Context context,List<Itinerary> itineraries){
            this.itineraries = itineraries;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return itineraries.size();
        }

        @Override
        public Object getItem(int position) {
            return itineraries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            TextView textView =(TextView)view.findViewById(android.R.id.text1);
            textView.setText(itineraries.get(position).getName());
            return view;
        }
    }

    class CodeAdapter extends BaseAdapter{

        private List<Code> codes = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public CodeAdapter(Context context,List<Code> codes){
            this.codes = codes;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return codes.size();
        }

        @Override
        public Object getItem(int position) {
            return codes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            TextView textView =(TextView)view.findViewById(android.R.id.text1);
            textView.setText(codes.get(position).getName());
            return view;
        }
    }

    class BusAdapter extends BaseAdapter{

        private List<Bus> buses = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public BusAdapter(Context context,List<Bus> buses){
            this.buses = buses;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return buses.size();
        }

        @Override
        public Object getItem(int position) {
            return buses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            TextView textView =(TextView)view.findViewById(android.R.id.text1);
            textView.setText(buses.get(position).getTime() + "-"+buses.get(position).getWay());
            return view;
        }
    }*/

}
