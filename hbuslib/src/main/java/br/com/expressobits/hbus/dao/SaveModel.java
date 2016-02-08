package br.com.expressobits.hbus.dao;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * Created by rafael on 18/01/16.
 */
public interface SaveModel {

    void saveCities();
    void saveItineraries(City city);
    void saveCodes(City city);
    void saveBus(City city,Itinerary itinerary,String way,String typeday);


}
