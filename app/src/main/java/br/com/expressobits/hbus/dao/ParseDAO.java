package br.com.expressobits.hbus.dao;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.FileUtils;

/**
 * Created by rafael on 29/10/15.
 */
public class ParseDAO {

    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String IMAGE = "image";

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
                for (ParseObject object :objects){
                    cities.add(parseToCity(object));
                    //object.pinInBackground();
                }

            }
        });

        return cities;
    }

    /**
     * Converte um obejto do parse em cidade
     * com convenções especificas
     * @param parseObject
     * @return
     */
    public static City parseToCity(ParseObject parseObject){
        City city = new City();
        city.setName(parseObject.getString(NAME));
        city.setCountry(parseObject.getString(COUNTRY));
        city.setImage(FileUtils.getProfilepciture(parseObject.getParseFile(IMAGE)));
        return city;
    }
}
