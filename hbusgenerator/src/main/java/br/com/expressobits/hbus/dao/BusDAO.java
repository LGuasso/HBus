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
public class BusDAO extends SQLiteOpenHelper{

    private static final String TAG = "GENERATOR";

    public BusDAO(Context context){
        super(context, TimesHelper.DATABASE_NAME, null, TimesHelper.DATABASE_VERSION);
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
        TimesHelper.createTableCities(db);
        TimesHelper.createTableItineraries(db);
        TimesHelper.createTableCodes(db);
        TimesHelper.createTableBuses(db);
    }

    public void deleteTable(String table){
        Log.i(TAG, "Delete tabela " + table);
        getWritableDatabase().delete(table, null, null);
    }

    public Long insertCity(City city){
        return TimesHelper.insert(getWritableDatabase(), city);
    }

    public Long insertItineraries(Itinerary itinerary){
        return TimesHelper.insert(getWritableDatabase(), itinerary);
    }

    public Long insertCodes(Code code){
        return TimesHelper.insert(getWritableDatabase(), code);
    }

    public Long insertBus(Bus bus){
        return TimesHelper.insert(getWritableDatabase(), bus);
    }

    public City getCity(Long id){
        return TimesHelper.getCity(getReadableDatabase(), id);
    }

    public City getCity(String name,String country){
        return TimesHelper.getCity(getReadableDatabase(), name,country);
    }

    public Code getCode(String name,Long cityId){
        return TimesHelper.getCode(getReadableDatabase(), name,cityId);
    }

    public List<City> getCities(){
        return TimesHelper.getCities(getReadableDatabase());
    }

    public List<Itinerary> getItineraries(Long cityId){
        return TimesHelper.getItineraries(getReadableDatabase(),cityId);
    }



}
