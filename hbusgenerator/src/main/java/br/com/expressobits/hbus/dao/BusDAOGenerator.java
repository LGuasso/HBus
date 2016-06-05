package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;


/**
 * Created by rafael on 08/12/15.
 */
public class BusDAOGenerator extends SQLiteOpenHelper{

    private static final String TAG = "GENERATOR";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bus_database.db";

    public BusDAOGenerator(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTables(SQLiteDatabase db){
        Log.i(TAG, "Create tables...");
        BusHelper.createTableCities(db);
        BusHelper.createTableItineraries(db);
        BusHelper.createTableCodes(db);
        BusHelper.createTableBuses(db);
    }

    public void deleteTable(String table){
        Log.i(TAG, "Delete tabela " + table);
        getWritableDatabase().delete(table, null, null);
    }

    public void insertCity(City city){
        Log.d(TAG,"insert city "+city.getId()+" "+city.getName()+" "+city.getCountry()+" "+city.getLocalization());
        BusHelper.insert(getWritableDatabase(), city);
    }

    public void insertItineraries(Itinerary itinerary){
        Log.d(TAG,"insert itinerary "+itinerary.getId()+" "+itinerary.getName()+" "+itinerary.getWays()+" "+itinerary.getCodes());
        BusHelper.insert(getWritableDatabase(), itinerary);
    }

    public void insertCodes(Code code){
        Log.d(TAG,"insert code "+code.getId()+" "+code.getName()+" "+code.getDescrition());
        BusHelper.insert(getWritableDatabase(), code);
    }

    public void insertBus(Bus bus){
        Log.d(TAG,"insert bus "+bus.getId()+" "+bus.getTime()+" "+bus.getCode()+" "+bus.getTypeday()+" "+bus.getWay());
        BusHelper.insert(getWritableDatabase(), bus);
    }

    public City getCity(String id){
        return BusHelper.getCity(getReadableDatabase(), id);
    }

    /**public City getCityName(String name,String country){
        return BusHelper.getCityName(getReadableDatabase(), name,country);
    }*/

    public Code getCode(String name,Long cityId){
        return BusHelper.getCode(getReadableDatabase(), name,cityId);
    }

    public List<City> getCities(){
        return BusHelper.getCities(getReadableDatabase());
    }

    public List<Itinerary> getItineraries(City city){
        return BusHelper.getItineraries(getReadableDatabase(),city);
    }



}
