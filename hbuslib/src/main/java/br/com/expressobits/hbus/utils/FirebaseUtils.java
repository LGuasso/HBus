package br.com.expressobits.hbus.utils;

import android.util.Log;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * @author Rafael
 * @since 19/01/16
 */
public class FirebaseUtils {

    public static String BARS = "/";

    public static String CITY_TABLE = "city";
    public static String COMPANY_TABLE = "company";
    public static String ITINERARY_TABLE = "itinerary";
    public static String CODE_TABLE = "code";
    public static String BUS_TABLE = "bus";

    public static String getIdCity(String country, String cityName){
        return BARS+country+BARS+cityName;
    }

    public static String getIdCompany(String country,String city, String company){
        return BARS+country+BARS+city+BARS+company;
    }

    public static String getIdItinerary(String country,String city, String company,String itinerary){
        return BARS+country+BARS+city+BARS+company+BARS+itinerary;
    }

    public static String getIdCode(String country,String city,String company,String code){
        return BARS+country+BARS+city+BARS+company+BARS+code;
    }

    public static String getIdBus(String country,String city,String company,String itinerary,String way,String typeday,String time){
        return BARS+country+BARS+city+BARS+company+BARS+itinerary+BARS+way+BARS+typeday+BARS+time;
    }

    public static String getIdCityforItinerary(City city){
        return BARS+ CityContract.City.TABLE_NAME+BARS+city.getCountry()+BARS+city.getName();
    }



    public static String getCountry(String id){
        return id.split(BARS)[1];
    }

    public static String getCityName(String id){
        return id.split(BARS)[2];
    }

    public static String getCompany(String id){
        return id.split(BARS)[3];
    }

    public static String getItinerary(String id){
        return id.split(BARS)[4];
    }

    public static String getWay(String id){
        return id.split(BARS)[5];
    }

    public static String getTimeForBus(String id) {
        return id.split(BARS)[7];
    }
}
