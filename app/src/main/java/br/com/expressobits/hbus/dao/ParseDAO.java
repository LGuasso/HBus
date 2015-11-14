package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * Created by rafael on 29/10/15.
 */
public class ParseDAO {

    public static final String TAG = "ParseDAO";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String IMAGE = "image";
    public static final String WAYS = "ways";
    public static final String CITY = "city";

    Context context;


    public ParseDAO(Context context){
        this.context = context;
    }

    /**
     * Método que puxa do parse todas informaçãoes de cidades
     *
     * @return Lista de cidades
     */
    public List<City> getCities(){
        final ArrayList<City> cities = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");


        query.findInBackground(new FindCallback<ParseObject>() {


            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    cities.add(parseToCity(object));
                    //object.pinInBackground();
                }

            }
        });

        return cities;
    }

    public void insertItineraries(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Itinerary");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                BusDAO dao = new BusDAO(context);
                dao.deleteAllItinerary();
                for (ParseObject object:objects){
                    dao.insert(parseToItinerary(object));
                    Log.i(TAG,"insert parse object itinerary"+object.getString(NAME));
                }
            }
        });
    }

    /**
     * Converte um obejto do parse em cidade
     * com convenções especificas
     * @param parseObject
     * @return
     */
    public City parseToCity(ParseObject parseObject){
        City city = new City();
        city.setName(parseObject.getString(NAME));
        city.setCountry(parseObject.getString(COUNTRY));
        /**try {
            city.setImage(FileUtils.getProfilepciture(parseObject.getParseFile(IMAGE)));
        } catch (ParseException e) {
            city.setImage(context.getResources().getDrawable(R.drawable.default_city));
        }catch (OutOfMemoryError e) {
            city.setImage(context.getResources().getDrawable(R.drawable.default_city));
        }*/
         city.setImage(context.getResources().getDrawable(R.drawable.default_city));
        return city;
    }

    /**
     * Converte um obejto do parse em cidade
     * com convenções especificas
     * @param parseObject
     * @return
     */
    public Itinerary parseToItinerary(ParseObject parseObject){
        Itinerary itinerary = new Itinerary();
        itinerary.setName(parseObject.getString(NAME));
        itinerary.setWays(new ArrayList<String>(Arrays.asList(parseObject.getString(WAYS).split(","))));
        return itinerary;
    }


}
