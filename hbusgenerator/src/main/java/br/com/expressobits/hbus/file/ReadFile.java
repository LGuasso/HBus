package br.com.expressobits.hbus.file;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * @author Rafael
 * @since 08/12/15.
 */
public class ReadFile {

    private static final String BARS = "/";
    private static final String SPLIT_FILE = ";";
    private static final String SPLIT_FILE_SECONDARY = ",";
    private static final String SPLIT_FILE_TIMES = "\t-\t";
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
        city.setName(text.split(SPLIT_FILE)[0]);
        city.setCountry(text.split(SPLIT_FILE)[1]);
        String[] location = text.split(SPLIT_FILE)[2].split(",");
        city.setLocation(
                new GeoPt()
                        .setLatitude(Float.valueOf(location[0]))
                        .setLongitude(Float.valueOf(location[1]))
        );
        return city;
    }

    public Itinerary toItinerary(String text){
        Itinerary itinerary = new Itinerary();
        itinerary.setName(text.split(SPLIT_FILE)[0]);
        itinerary.setWays(
                new ArrayList<String>(
                        Arrays.asList(text.split(SPLIT_FILE)[1].split(SPLIT_FILE_SECONDARY))
                ));
        return itinerary;
    }

    public Code toCode(String text){
        Code code = new Code();
        code.setName(text.split(SPLIT_FILE)[0]);
        if(text.split(SPLIT_FILE).length>1){
            code.setDescrition(text.split(SPLIT_FILE)[1]);
        }
        return code;
    }

    public Bus toBus(String text){
        Bus bus = new Bus();
        bus.setTime(text.split(SPLIT_FILE_TIMES)[0]);
        try{
            bus.setCode(text.split(SPLIT_FILE_TIMES)[1]);
        }catch (Exception e){
            Log.e(TAG," "+"TEXTO("+text+")");
            bus.setCode(CodeContract.NOT_CODE);
        }

        return bus;
    }

    public List<City> getCities(){
        List<City> cities = new ArrayList<>();
        for(String text:readFile(CITIES_FILE)){
            cities.add(toCity(text));
        }
        return cities;
    }

    public List<Itinerary> getItineraries(City city){
        List<Itinerary> itineraries = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+city.getName()+BARS+ITINERARIES_FILE)){
            itineraries.add(toItinerary(text));
        }
        return itineraries;
    }

    public List<Code> getCodes(City city){
        List<Code> codes = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+city.getName()+BARS+CODES_FILE)){
            codes.add(toCode(text));
        }
        return codes;
    }

    public List<Bus> getBuses(City city,Itinerary itinerary,String way,String typeday){
        List<Bus> buses = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+
                        city.getName()+BARS+
                        TextUtils.toSimpleNameFile(itinerary.getName())+BARS+
                        TextUtils.toSimpleNameWay(way)+"_"+typeday+FORMAT
        )){
            buses.add(toBus(text));
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
