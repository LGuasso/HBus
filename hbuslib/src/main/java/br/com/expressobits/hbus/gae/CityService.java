package br.com.expressobits.hbus.gae;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.City;

/**
 * Created by rafael on 30/01/16.
 */
public class CityService implements Service{

    public static List<City> cities = new ArrayList<>();


    @Override
    public City add(String id, String name, String country) throws Exception{
        //Check for already exists
        City city = new City();
        city.setId(id);
        city.setName(name);
        city.setCountry(country);
        if (cities.contains(city)) throw new Exception("Quote Record already exists");
        cities.add(city);
        return city;
    }

    @Override
    public City update(City city) {
        return city;
    }

    @Override
    public void remove(City city) throws Exception{

        if (cities.contains(city))
            throw new Exception("Quote Record does not exist");
        cities.remove(city);
    }

    @Override
    public List<City> getCities() {
        return cities;
    }
}
