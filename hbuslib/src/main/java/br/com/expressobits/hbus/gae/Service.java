package br.com.expressobits.hbus.gae;

import java.util.List;

import br.com.expressobits.hbus.model.City;

/**
 * Created by rafael on 30/01/16.
 */
public interface Service {

    public City add(String id,String name,String country) throws Exception;
    public City update(City city);
    public void remove(City city) throws Exception;
    public List<City> getCities();

}
