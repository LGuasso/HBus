package br.com.expressobits.hbus.dao;

import br.com.expressobits.hbus.ScheduleSQLite;
import br.com.expressobits.hbus.files.ReadFile;
import br.com.expressobits.hbus.model.*;
import br.com.expressobits.hbus.push.*;
import br.com.expressobits.hbus.utils.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Rafael
 * @since 02/03/17
 */
public class SendDataToSQL {

    private City city;
    private List<Company> companies = new ArrayList<>();
    private HashMap<Company, List<Code>> codes = new HashMap<>();
    private HashMap<Company, List<Itinerary>> itineraries = new HashMap<>();
    private HashMap<Company, HashMap<Itinerary, List<Bus>>> buses = new HashMap<>();
    private ScheduleSQLite scheduleSQLite;

    private ReadFile readFile;

    public SendDataToSQL(City city){
        scheduleSQLite = new ScheduleSQLite(
                SQLConstants.DATABASE_PATTERN_NAME+"/"+
                        city.getCountry()+"/"+
                        city.getName()+"/"+
                        StringUtils.getNameDatabase(city.getCountry(),city.getName(),1));
        readFile = new ReadFile();
    }

    public List<Company> getCompanies(City city){
        return companies;
    }

    public List<Itinerary> getItineraries(City city, Company company){
        return itineraries.get(company);
    }

    public List<Code> getCodes(City city, Company company){
        return codes.get(company);
    }

    public int getSizeBusesOfItinerary( Company company, Itinerary itinerary){
        return buses.get(company).get(itinerary).size();
    }

    public City readData(City city) {
        companies = readFile.getCompanies(city);
        HashMap<Company, List<Code>> listCodes = new HashMap<>();
        HashMap<Company, List<Itinerary>> listItineraries = new HashMap<>();
        HashMap<Company, HashMap<Itinerary, List<Bus>>> listBuses = new HashMap<>();
        for (Company company : companies) {
            listCodes.put(company, readFile.getCodes(city, company));
            List<Itinerary> itineraries = readFile.getItineraries(city, company);
            listItineraries.put(company, itineraries);
            listBuses.put(company, readFile.getBuses(city, company, itineraries));
        }
        codes = listCodes ;
        itineraries = listItineraries;
        buses =listBuses;
        JOptionPane.showMessageDialog(null,"Read sucesso!");
        return city;
    }

    public void sendCompany(City city, Company company) {
        send(city, company);
        System.out.println("\tcompany " + company.getName());

        List<Code> codesList = codes.get(company);
        List<Itinerary> itineraryList = itineraries.get(company);
        if (codesList == null) {
            System.out.println("\t\tcodeList null!" + company.getName());
        } else {
            for (Code code : codesList) {
                send(city, company, code);
            }
            System.out.println("\t\tcodeList size!" + codesList.size());
        }
        if (itineraryList == null) {
            System.out.println("\t\titineraryList null!" + company.getName());
        } else {
            for (Itinerary itinerary : itineraryList) {
                sendItinerary(city, company, itinerary);
            }
            System.out.println("\t\titineraryList size!" + itineraryList.size());
        }
    }

    private void sendItinerary(City city, Company company, Itinerary itinerary) {
        send(city, company, itinerary);
        HashMap<Itinerary, List<Bus>> busList = buses.get(company);
        List<Bus> busListed = busList.get(itinerary);
        if (busListed == null) {
            System.out.println("\t\t\tbusLited null!" + itinerary.getName());
        } else {
            for(Bus bus:busListed){
                send(city, company, itinerary, bus);
            }
            System.out.println("\t\t\tbusLited size!" + busListed.size());
        }
    }

    private void send(City city,Company company){
        scheduleSQLite.insert(city,company);
    }

    private void send(City city,Company company, Code code){
        scheduleSQLite.insert(city,company,code);
    }

    private void send(City city, Company company, Itinerary itinerary){
        scheduleSQLite.insert(city,company,itinerary);
    }

    private void send(City city, Company company, Itinerary itinerary,Bus bus){
        scheduleSQLite.insert(city,company,itinerary,bus);
    }

    public void close() {
        scheduleSQLite.close();
    }

    public void open() {
        scheduleSQLite.open();
        scheduleSQLite.createTables();
    }
}
