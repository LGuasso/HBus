package br.com.expressobits.hbus.file;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * Created by rafael on 08/12/15.
 */
public class ReadFile {

    private static final String BARS = "/";
    private static final String SPLIT_FILE = ";";
    private static final String SPLIT_FILE_SECONDARY = ",";
    private static final String SPLIT_FILE_TIMES = " - ";
    private static final String CITIES_FILE = "cities.dat";
    private static final String ITINERARIES_FILE = "itineraries.dat";
    private static final String CODES_FILE = "codes.dat";
    private static final String TAG = "FILEGENERATOR";
    private static final String FORMAT = ".dat";
    Context context;


    public ReadFile(Context context){
        this.context = context;
    }

    public City toCity(String text){
        City city = new City();
        city.setId(Long.parseLong(text.split(SPLIT_FILE)[0]));
        city.setName(text.split(SPLIT_FILE)[1]);
        city.setCountry(text.split(SPLIT_FILE)[2]);
        city.setPosition(text.split(SPLIT_FILE)[3]);
        return city;
    }

    public Itinerary toItinerary(String text,Long cityId){
        Itinerary itinerary = new Itinerary();
        itinerary.setName(text.split(SPLIT_FILE)[0]);
        itinerary.setWays(
                new ArrayList<String>(
                        Arrays.asList(text.split(SPLIT_FILE)[1].split(SPLIT_FILE_SECONDARY))
                ));
        itinerary.setCityid(cityId);
        return itinerary;
    }

    public Code toCode(String text,Long cityId){
        Code code = new Code();
        code.setName(text.split(SPLIT_FILE)[0]);
        if(text.split(SPLIT_FILE).length>1){
            code.setDescrition(text.split(SPLIT_FILE)[1]);
        }
        code.setCityid(cityId);
        return code;
    }

    public Bus toBus(String text,Long cityId,Long itineraryId,String way,String typeday){
        Bus bus = new Bus();
        bus.setTime(text.split(SPLIT_FILE_TIMES)[0]);
        BusDAO dao = new BusDAO(context);
        try{
            bus.setCodeId(dao.getCode(text.split(SPLIT_FILE_TIMES)[1],cityId).getId());
        }catch (Exception e){
            Log.e(TAG,"ERRO!  TEXTO("+text.split(SPLIT_FILE_TIMES)[1]+")");
            bus.setCodeId(0l);
        }

        bus.setCityid(cityId);
        bus.setWay(way);
        bus.setTypeday(TextUtils.getTypeDAyString(typeday));
        bus.setItineraryId(itineraryId);

        return bus;
    }

    public List<City> getCities(){
        List<City> cities = new ArrayList<>();
        for(String text:readFile(CITIES_FILE)){
            cities.add(toCity(text));
        }
        return cities;
    }

    public List<Itinerary> getItineraries(Long cityId,String name,String country){
        List<Itinerary> itineraries = new ArrayList<>();
        for(String text:readFile(country+BARS+name+BARS+ITINERARIES_FILE)){
            itineraries.add(toItinerary(text, cityId));
        }
        return itineraries;
    }

    public List<Code> getCodes(Long cityId,String name,String country){
        List<Code> codes = new ArrayList<>();
        for(String text:readFile(country+BARS+name+BARS+CODES_FILE)){
            codes.add(toCode(text, cityId));
        }
        return codes;
    }

    public List<Bus> getBuses(Long cityId,String name,String country,Itinerary itinerary,String way,String typeday){
        List<Bus> buses = new ArrayList<>();
        for(String text:readFile(country+BARS+
                        name+BARS+
                        TextUtils.toSimpleNameFile(itinerary.getName())+BARS+
                        TextUtils.toSimpleNameWay(way)+"_"+typeday+FORMAT
        )){
            buses.add(toBus(text, cityId,itinerary.getId(),way,typeday));
        }
        return buses;
    }

    public List<String> readFile(String fileName){

        List<String> text = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.add(mLine);
                Log.i(TAG, "          line " + mLine);
            }
            Log.i(TAG,"sucess "+fileName);
        } catch (IOException e) {
            Log.i(TAG, "error " + fileName);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return text;
    }

}
