package br.com.expressobits.hbus.dao;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;

/**
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
    public List<Itinerary> getItineraries(Long cityId){
        return TimesHelper.getItineraries(getReadableDatabase(),cityId);
    }

    public List<Code> getCodes(){
        return TimesHelper.getCodes(getReadableDatabase());
    }

    public List<Bus> getBuses(){
        return TimesHelper.getBuses(getReadableDatabase());
    }

    public List<Bus> getBuses(Long itineraryId,String way,String typeday){
        return TimesHelper.getBuses(getReadableDatabase(),itineraryId,way,typeday);
    }

    public List<Bus> getNextBus(Itinerary itinerary){ return TimesHelper.getNextBuses(getReadableDatabase(),itinerary);}





}
