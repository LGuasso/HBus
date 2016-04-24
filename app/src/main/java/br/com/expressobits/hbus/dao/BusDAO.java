package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;

/**
 * @author Rafael
 * @since 19/01/16.
 */
public class BusDAO extends SQLiteAssetHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bus_database.db";
    private static final String TAG = "BusDAO";

    public BusDAO(Context context) {
        //super(context, BusHelper.DATABASE_NAME, null, BusHelper.DATABASE_VERSION); //SQLITEOPENHELPER
        super(context, DATABASE_NAME,null, DATABASE_VERSION);//SQLITE ASSET HELPER
    }

    /**@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BusHelper.SQL_CREAT_CITIES);
        db.execSQL(ItineraryContract.SQL_CREATE_TABLE);
        db.execSQL(CodeContract.SQL_CREATE_TABLE);
        db.execSQL(BusContract.SQL_CREATE_TABLE);
                //setForcedUpgrade(DATABASE_VERSION);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(City city){
        if((getItinerary(city.getId()))==null){
            Log.d(TAG,"insert new "+ CityContract.City.TABLE_NAME+"!\t"+city.getId());
            BusHelper.insert(getWritableDatabase(), city);
            return true;
        }else {
            Log.d(TAG,"update old "+ CityContract.City.TABLE_NAME+"!\t"+city.getId());
            BusHelper.update(getWritableDatabase(), city);
            return false;
        }
    }

    public boolean insert(Itinerary itinerary){
        if((getItinerary(itinerary.getId()))==null){
            Log.d(TAG,"insert new "+ ItineraryContract.Itinerary.TABLE_NAME+"!\t"+itinerary.getId());
            BusHelper.insert(getWritableDatabase(), itinerary);
            return true;
        }else {
            Log.d(TAG,"update old "+ ItineraryContract.Itinerary.TABLE_NAME+"!\t"+itinerary.getId());
            BusHelper.update(getWritableDatabase(), itinerary);
            return false;
        }

    }

    public boolean insert(Code code){
        if((getCode(code.getId()))==null){
            Log.d(TAG,"insert new "+ CodeContract.Code.TABLE_NAME+"!\t"+code.getId());
            BusHelper.insert(getWritableDatabase(), code);
            return true;
        }else {
            Log.d(TAG,"update old "+ CodeContract.Code.TABLE_NAME+"!\t"+code.getId());
            BusHelper.update(getWritableDatabase(), code);
            return false;
        }

    }

    public boolean insert(Bus bus){
        if((getBus(bus.getId()))==null){
            Log.d(TAG,"insert new "+ BusContract.Bus.TABLE_NAME+"!\t"+bus.getId());
            BusHelper.insert(getWritableDatabase(), bus);
            return true;
        }else {
            Log.d(TAG,"update old "+ BusContract.Bus.TABLE_NAME+"!\t"+bus.getId());
            BusHelper.update(getWritableDatabase(), bus);
            return false;
        }
    }

    public int deleteItineraries(String cityId){
        return BusHelper.deleteItineraries(getWritableDatabase(), getCity(cityId));
    }

    public int deleteCodes(String cityId){
        return BusHelper.deleteCodes(getWritableDatabase(), getCity(cityId));
    }

    public int deleteBuses(String cityId,String itineraryId){
        return BusHelper.deleteBuses(getWritableDatabase(), getCity(cityId), getItinerary(itineraryId));
    }

    public City getCity(String id){
        return BusHelper.getCity(getReadableDatabase(), id);
    }

    public Itinerary getItinerary(String itineraryId){
        return  BusHelper.getItinerary(getReadableDatabase(), itineraryId);
    }

    public Code getCode(String codeId){
        return  BusHelper.getCode(getReadableDatabase(), codeId);
    }
    public Code getCode(String cityId,String code){
        return  BusHelper.getCode(getReadableDatabase(), getCity(cityId), code);
    }

    public Bus getBus(String busId){
        return  BusHelper.getBus(getReadableDatabase(), busId);
    }

    public List<City> getCities(){
        return BusHelper.getCities(getReadableDatabase());
    }

    public List<Itinerary> getItineraries(String cityId){

        return BusHelper.getItineraries(getReadableDatabase(), getCity(cityId));
    }

    public List<Itinerary> getItineraries() {
        return BusHelper.getItineraries(getReadableDatabase());
    }

    public List<Code> getCodes(String cityId){
        return BusHelper.getCodes(getReadableDatabase(), getCity(cityId));
    }

    public List<Bus> getBuses(String cityId,String itineraryId,String way,TypeDay typeday){
        return BusHelper.getBuses(getReadableDatabase(), getCity(cityId), getItinerary(itineraryId), way, typeday);
    }

    public List<Bus> getBuses(String cityId,String itineraryId){
        return BusHelper.getBuses(getReadableDatabase(),getCity(cityId),getItinerary(itineraryId));
    }

    public List<Bus> getNextBus(String cityId,String itineraryId){
        return BusHelper.getNextBuses(getReadableDatabase(),getCity(cityId), getItinerary(itineraryId));}



}
