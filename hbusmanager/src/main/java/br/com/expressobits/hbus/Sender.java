package br.com.expressobits.hbus;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;

import java.util.List;

/**
 * @author Rafael Correa
 * @since 02/03/17
 */
public interface Sender {

    List<Company> getCompanies(City city);
    List<Itinerary> getItineraries(City city, Company company);
    List<Code> getCodes(City city, Company company);

}
