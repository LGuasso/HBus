package br.com.expressobits.hbus.utils;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * @author Rafael
 * @since 19/01/16
 */
public class FirebaseUtils {

    public static String BARS = "/";

    public static String getIdCity(City city){
        return BARS+city.getCountry()+BARS+city.getName();
    }

    public static String getIdItinerary(City city,Itinerary itinerary){
        return BARS+city.getCountry()+BARS+city.getName()+BARS+itinerary.getName();
    }

    public static String getIdCode(City city,Code code){
        return BARS+city.getCountry()+BARS+city.getName()+BARS+code.getName();
    }

    public static String getIdBus(City city,Itinerary itinerary,String way,String typeday,String time){
        return BARS+city.getCountry()+BARS+city.getName()+BARS+itinerary.getName()+BARS+way+BARS+typeday+BARS+time;
    }

    public static String getIdCityforItinerary(City city){
        return BARS+ CityContract.City.TABLE_NAME+BARS+city.getCountry()+BARS+city.getName();
    }
}
