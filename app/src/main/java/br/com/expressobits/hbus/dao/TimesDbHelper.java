package br.com.expressobits.hbus.dao;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;

/**
 * @deprecated
 * @author Rafael Correa
 * @since 31/07/2015
 */
public class TimesDbHelper extends SQLiteAssetHelper{

    public TimesDbHelper(Context context) {
        super(context, TimesHelper.DATABASE_NAME, null, TimesHelper.DATABASE_VERSION);
        //setForcedUpgrade(DATABASE_VERSION);
    }



    public City getCity(Long id){
        return TimesHelper.getCity(getReadableDatabase(),id);
    }

    public Itinerary getItinerary(Long id){
        return TimesHelper.getItinerary(getReadableDatabase(), id);
    }

    public Code getCode(Long id){
        return TimesHelper.getCode(getReadableDatabase(), id);
    }

    public Bus getBus(Long id){
        return TimesHelper.getBus(getReadableDatabase(), id);
    }

    public List<City> getCities(){
        return TimesHelper.getCities(getReadableDatabase());
    }

    public List<Itinerary> getItineraries(){
        return TimesHelper.getItineraries(getReadableDatabase());
    }

     /**public List<Itinerary> getItineraries(boolean favorite){
        List<Itinerary> itineraries = new ArrayList<>();
        if(!favorite){
            List<Itinerary> its = TimesHelper.getItineraries(getReadableDatabase());
            for()
        }
        return
    }*/
    public List<Itinerary> getItineraries(Long cityId){
        return TimesHelper.getItineraries(getReadableDatabase(), cityId);
    }

    public List<Code> getCodes(){
        return TimesHelper.getCodes(getReadableDatabase());
    }

    public List<Bus> getBuses(){
        return TimesHelper.getBuses(getReadableDatabase());
    }

    public List<Bus> getBuses(Long itineraryId,String way,String typeday){
        return TimesHelper.getBuses(getReadableDatabase(), itineraryId, way, typeday);
    }

    public List<Bus> getNextBus(Itinerary itinerary){ return TimesHelper.getNextBuses(getReadableDatabase(), itinerary);}

    public void insertCities(List<City> cities){
        TimesHelper.deleteAllcities(getWritableDatabase());
        for(City city:cities){
            insert(city);
        }
    }

    public void insertItinearies(List<Itinerary> itineraries){
        TimesHelper.deleteAllItineraries(getWritableDatabase());
        for(Itinerary itinerary:itineraries){
            insert(itinerary);
        }
    }

    public Long insert(City city){
        return TimesHelper.insert(getWritableDatabase(), city);
    }

    public Long insert(Itinerary itinerary){
        return TimesHelper.insert(getWritableDatabase(), itinerary);
    }

    public Long insert(Code code){
        return TimesHelper.insert(getWritableDatabase(), code);
    }

    public Long insert(Bus bus){
        return TimesHelper.insert(getWritableDatabase(), bus);
    }


    public void deleteAllCities(){
        TimesHelper.deleteAllcities(getWritableDatabase());
    }

    public void deleteAllItineraries(){
        TimesHelper.deleteAllItineraries(getWritableDatabase());
    }

    public void deleteAllCodes(){
        TimesHelper.deleteAllCodes(getWritableDatabase());
    }

    public void deleteAllBuses(){
        TimesHelper.deleteAllBuses(getWritableDatabase());
    }

}
