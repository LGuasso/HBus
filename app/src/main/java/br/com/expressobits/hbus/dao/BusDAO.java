package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;

/**
 * @author Rafael
 * @since 19/01/16.
 */
public class BusDAO extends SQLiteOpenHelper{


    public BusDAO(Context context) {
        super(context, BusHelper.DATABASE_NAME, null, BusHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BusHelper.SQL_CREATE_CITIES);
        db.execSQL(ItineraryContract.SQL_CREATE_TABLE);
        db.execSQL(CodeContract.SQL_CREATE_TABLE);
        db.execSQL(BusContract.SQL_CREATE_TABLE);
                //setForcedUpgrade(DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(City city){
        BusHelper.insert(getWritableDatabase(), city);
    }

    public void insert(Itinerary itinerary){
        BusHelper.insert(getWritableDatabase(), itinerary);
    }

    public void insert(Code code){
        BusHelper.insert(getWritableDatabase(), code);
    }

    public void insert(Bus bus){
        BusHelper.insert(getWritableDatabase(), bus);
    }



    public City getCity(String id){
        return BusHelper.getCity(getReadableDatabase(), id);
    }

    public Itinerary getItinerary(String itineraryId){
        return  BusHelper.getItinerary(getReadableDatabase(), itineraryId);
    }
    public Code getCode(String cityId,String code){
        return  BusHelper.getCode(getReadableDatabase(),getCity(cityId),code);
    }

    public List<City> getCities(){
        return BusHelper.getCities(getReadableDatabase());
    }

    public List<Itinerary> getItineraries(String cityId){

        return BusHelper.getItineraries(getReadableDatabase(), getCity(cityId));
    }

    public List<Code> getCodes(String cityId){
        return BusHelper.getCodes(getReadableDatabase(), getCity(cityId));
    }

    public List<Bus> getBuses(String cityId,String itineraryId,String way,TypeDay typeday){
        return BusHelper.getBuses(getReadableDatabase(),getCity(cityId),getItinerary(itineraryId), way, typeday);
    }

    public List<Bus> getNextBus(String cityId,String itineraryId){
        return BusHelper.getNextBuses(getReadableDatabase(),getCity(cityId), getItinerary(itineraryId));}



}
