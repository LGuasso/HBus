package br.com.expressobits.hbus.utils;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.model.City;

/**
 * Created by rafael on 19/01/16.
 */
public class FirebaseUtils {

    public static String BARS = "/";

    public static String getPath(City city){
        return BARS+city.getCountry()+BARS+city.getName();
    }

    public static String getPathforItinerary(City city){
        return BARS+ ItineraryContract.Itinerary.TABLE_NAME+BARS+city.getCountry()+BARS+city.getName();
    }
}
