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

    public static final String EXTENSION_IMAGE = ".jpg";
    public static final String FLAG_TEXT_FILE = "_flag";
    public static final String GENERAL = "general";
    public static final String BARS = "/";

    public static final String CITY_TABLE = "city";
    public static final String COMPANY_TABLE = "company";
    public static final String ITINERARY_TABLE = "itinerary";
    public static final String CODE_TABLE = "code";
    public static final String BUS_TABLE = "bus";

    public static final String REF_STORAGE_HBUS = "gs://hbus-1206.appspot.com";
    public static final String NEWS_TABLE = "news";

    public static String getIdNewsGeneral(String time){
        return BARS+NEWS_TABLE+BARS+GENERAL+BARS+time;
    }

    public static String getIdNewsCity(String time, String country, String city){
        return BARS+NEWS_TABLE+BARS+CITY_TABLE+BARS+country+BARS+city+BARS+time;
    }

    public static String getIdNewsCompany(String time, String country, String city, String company){
        return BARS+NEWS_TABLE+BARS+COMPANY_TABLE+BARS+country+BARS+city+BARS+company+BARS+time;
    }

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
        return id.split(BARS)[1]+BARS+id.split(BARS)[2];
    }

    public static String getCityName(String id){
        return id.split(BARS)[3];
    }

    public static String getCompany(String id){
        return id.split(BARS)[4];
    }

    public static String getItinerary(String id){
        return id.split(BARS)[5];
    }

    public static String getWay(String id){
        return id.split(BARS)[6];
    }

    public static String getTimeForBus(String id) {
        return id.split(BARS)[8];
    }

    public static String getNewsCityName(String id){
        if(id.split(BARS).length>5){
            return id.split(BARS)[5];
        }else {
            return null;
        }
    }

    public static String getNewsCompany(String id){
        if(id.split(BARS).length>5){
            return id.split(BARS)[5];
        }else {
            return null;
        }
    }

    public static String getNewsItinerary(String id){
        if(id.split(BARS).length>7){
            return id.split(BARS)[7];
        }else {
            return null;
        }
    }

    public static String getIdForSubscribeCity(String id){
        return id.replace(" ","%").replace(FirebaseUtils.BARS,"-");
    }

}
