package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.dao.BusContract;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.dao.SaveModel;
import br.com.expressobits.hbus.dao.TimesHelper;
import br.com.expressobits.hbus.file.ReadFile;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * @author Rafael
 * @since 18/01/16.
 */
public class DbManager implements SaveModel{

    private static DbManager instance;
    private Context context;
    private BusDAO dao;
    private ReadFile file;
    private static final String TAG = "DBMANAGER";

    public DbManager(Context context){
        this.context = context;
        dao = new BusDAO(context);
        file = new ReadFile(context);
    }

    public static DbManager getInstance(Context context) {
        if(instance==null){
            instance = new DbManager(context);
        }
        return instance;
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

    @Override
    public void saveCities() {
        Log.i(TAG, "Inserindo cidades");
        for (City city:readFileCity()) {
            Long cityId = dao.insertCity(city);
            city.setId(cityId);
            Log.i(TAG, "> " + city.getName());
            saveCodes(city);
            saveItineraries(city);
        }
    }

    @Override
    public void saveItineraries(City city) {
        Log.i(TAG,"  >Inserindo itinerarios");
        for(Itinerary itinerary:readFileItinerary(city)){
            itinerary.setId(dao.insertItineraries(itinerary));
            Log.i(TAG, city.getName()+"> > " + itinerary.getName());
            for(String way:itinerary.getWays()){
                for(int i=0;i<3;i++){
                    saveBus(city,itinerary,way,TextUtils.getTypeDayInt(i));
                }

            }

        }
    }

    @Override
    public void saveCodes(City city) {
        Log.i(TAG,"  >Inserindo codes");
        for (Code code:readFileCode(city)) {
            dao.insertCodes(code);
            Log.i(TAG, city.getName()+"> > " + code.getName());
        }
    }

    @Override
    public void saveBus(City city, Itinerary itinerary, String way, String typeday) {
        Log.i(TAG,"  >Inserindo buses");
        Log.i(TAG,"  > >>>>Inserindo buses");
        for (Bus bus:readFileBus(city,itinerary,way,typeday)){
            Log.i(TAG, city.getName()+" "+itinerary.getName()+" "+way+" "+typeday+"> > >" + bus.getTime());
            dao.insertBus(bus);



        }
    }

    public void deleteAllData(){
        dao.deleteTable(CityContract.City.TABLE_NAME);
        dao.deleteTable(ItineraryContract.Itinerary.TABLE_NAME);
        dao.deleteTable(CodeContract.Code.TABLE_NAME);
        dao.deleteTable(BusContract.Bus.TABLE_NAME);
        dao.close();
    }
}