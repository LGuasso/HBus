package br.com.expressobits.hbus.dao;

/**
 * @author Rafael Correa
 * @since 01/03/17
 */
public class SQLConstants {
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String TEXT_TYPE = " TEXT";
    public static final String REAL_TYPE = " REAL";
    public static final String COMMA_SEP = ",";
    public static final String TEXT_PRIMARY_KEY = " TEXT PRIMARY KEY";
    public static final String PARENTES = " )";
    public static final String POINTCOMMA = ";";
    public static final String BARS = "/";
    public static final String CREATE_TABLE = "CREATE TABLE ";

    public static final String DATABASE_PATTERN_NAME = "hbus_schedule";

    public static String getIdCompany(String country,String city,String company){
        return getIdCityDefault(country,city)+BARS+company;
    }

    public static String getIdItinerary(String country,String city,String company,String itinerary){
        return getIdCityDefault(country,city)+BARS+company+BARS+itinerary;
    }

    public static String getIdCodeVersion1(String country,String city,String company,String code){
        return getIdCityDefault(country,city)+BARS+company+BARS+code;
    }

    public static String getIdCode(String country,String city,String company,String itinerary,String code){
        return getIdCityDefault(country,city)+BARS+company+BARS+itinerary+BARS+code;
    }

    public static String getIdBus(String country,String city,String company,String itinerary,String way,String typeday,String time){
        return getIdCityDefault(country,city)+BARS+company+BARS+itinerary+BARS+way+BARS+typeday+BARS+time;
    }

    public static String getIdCityDefault(String country,String city) {
        return BARS+country+BARS+city;
    }

    public static String getCountryFromBusId(String id) {
        return id.split(BARS)[1]+BARS+id.split(BARS)[2];
    }

    public static String getCityFromBusId(String id) {
        return id.split(BARS)[3];
    }

    public static String getCompanyFromBusId(String id) {
        return id.split(BARS)[4];
    }

    public static String getCountry(String id) {
        return id.split(BARS)[1]+BARS+id.split(BARS)[2];
    }

    public static String getCity(String id) {
        return id.split(BARS)[3];
    }

    public static String getItinerary(String id) {
        return id.split(BARS)[5];
    }
}
