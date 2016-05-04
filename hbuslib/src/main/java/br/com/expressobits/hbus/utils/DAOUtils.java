package br.com.expressobits.hbus.utils;

import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;

/**
 * Class utils import to generator ids
 * @author Rafael Correa
 * @since 17/02/16
 */
public class DAOUtils {

    public static String BARS = "/";
    public static String ERROR = "ERROR";

    public static String getId(City city){
        return city.getCountry()+BARS+city.getName();
    }

    public static String getId(City city,Itinerary itinerary){
        return city.getCountry()+BARS+city.getName()+BARS+itinerary.getName();
    }

    public static String getId(City city,Code code){
        return city.getCountry()+BARS+city.getName()+BARS+code.getName();
    }

    public static String getId(City city,Itinerary itinerary,Bus bus){
        return city.getCountry()+BARS+city.getName()+BARS+itinerary.getName()+
                BARS+bus.getWay()+BARS+bus.getTypeday()+BARS+bus.getTime();
    }

    public static String getNameItinerary(String id){
        if(id!=null && id.split(BARS).length>=3){
            return id.split(BARS)[2];
        }else {
            return ERROR;
        }

    }

    public static String getNameCity(String id){
        if(id!=null && id.split(BARS).length>=2){
            return id.split(BARS)[1];
        }else {
            return ERROR;
        }

    }

    public static String getNameCountry(String id){
        if(id!=null && id.split(BARS).length>=1){
            return id.split(BARS)[0];
        }else {
            return ERROR;
        }

    }

    public static String getTypedayBus(String id){
        if(id!=null && id.split(BARS).length>=5){
            return id.split(BARS)[4];
        }else {
            return ERROR;
        }

    }

    /**
     * /RS/Santa Maria/Cerrrito/Centro > Bairro/useful/12:12
     * @param id
     * @return
     */
    public static String getTimeForBus(String id){
        if(id!=null && id.split(BARS).length>=6){
            return id.split(BARS)[5];
        }else {
            return ERROR;
        }

    }

    /**
     * /RS/Santa Maria/Cerrrito/Centro > Bairro/useful/12:12
     * @param id
     * @return
     */
    public static String getTimeForAlarm(String id){
        if(id!=null && id.split(BARS).length>=5){
            return id.split(BARS)[4];
        }else {
            return ERROR;
        }

    }

    /**
     * /RS/Santa Maria/Cerrrito/Centro > Bairro/useful/12:12
     * @param id
     * @return
     */
    public static String getIdAlarmForBus(String id){
        if(id!=null && id.split(BARS).length>=6){
            return id.split(BARS)[5];
        }else {
            return ERROR;
        }

    }

    /**
     * /RS/Santa Maria/Cerrrito/Centro > Bairro/useful/12:12
     * @param id
     * @return
     */
    public static String getWayForBus(String id){
        if(id!=null && id.split(BARS).length>=4){
            return id.split(BARS)[3];
        }else {
            return ERROR;
        }

    }
}
