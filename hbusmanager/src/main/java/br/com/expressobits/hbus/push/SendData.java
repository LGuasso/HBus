package br.com.expressobits.hbus.push;

import br.com.expressobits.hbus.files.ReadFile;
import br.com.expressobits.hbus.model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Rafael Correa
 * @since 09/12/16
 */

public class SendData {

    List<City> cities = new ArrayList<>();
    HashMap<City, List<Company>> companies = new HashMap<>();
    HashMap<City, HashMap<Company, List<Code>>> codes = new HashMap<>();
    HashMap<City, HashMap<Company, List<Itinerary>>> itineraries = new HashMap<>();
    HashMap<City, HashMap<Company, HashMap<Itinerary, List<Bus>>>> buses = new HashMap<>();

    public boolean isCitySend = false;
    public boolean isCompanySend = false;
    public boolean isItinerarySend = false;
    public boolean isCodeSend = false;
    public boolean isBusSend = false;

    public ReadFile readFile;

    public SendData(){
        readFile = new ReadFile();
    }

    public void readData(String country) {
        cities = readFile.getCities(country);
        for (City city : cities) {
            companies.put(city, readFile.getCompanies(city));
            HashMap<Company, List<Code>> listCodes = new HashMap<>();
            HashMap<Company, List<Itinerary>> listItineraries = new HashMap<>();
            HashMap<Company, HashMap<Itinerary, List<Bus>>> listBuses = new HashMap<>();
            for (Company company : companies.get(city)) {
                listCodes.put(company, readFile.getCodes(city, company));

                List<Itinerary> itineraries = readFile.getItineraries(city, company);
                listItineraries.put(company, itineraries);
                listBuses.put(company, readFile.getBuses(city, company, itineraries));

            }
            codes.put(city, listCodes);
            itineraries.put(city, listItineraries);
            buses.put(city, listBuses);
        }
        JOptionPane.showMessageDialog(null,"Read sucesso!");

    }

    public City readData(String country,String cityName) {
        cities = readFile.getCities(country);
        for (City city : cities) {
            if(city.getName().equals(cityName)){
                companies.put(city, readFile.getCompanies(city));
                HashMap<Company, List<Code>> listCodes = new HashMap<>();
                HashMap<Company, List<Itinerary>> listItineraries = new HashMap<>();
                HashMap<Company, HashMap<Itinerary, List<Bus>>> listBuses = new HashMap<>();
                for (Company company : companies.get(city)) {
                    listCodes.put(company, readFile.getCodes(city, company));

                    List<Itinerary> itineraries = readFile.getItineraries(city, company);
                    listItineraries.put(company, itineraries);
                    listBuses.put(company, readFile.getBuses(city, company, itineraries));

                }
                codes.put(city, listCodes);
                itineraries.put(city, listItineraries);
                buses.put(city, listBuses);

                JOptionPane.showMessageDialog(null,"Read sucesso!");
                return city;
            }

        }
        return null;

    }

    public void removeAllValuesFromCity(String country,String cityName){
        cities = readFile.getCities(country);
        for(City city:cities){
            if(city.getName().equals(cityName)){
                removeAllValuesFromCity(city);
            }
        }
    }

    public void removeAllValuesFromCity(City city){
        SendCityToFirebase.removeAllValues(city);
        SendCompanyToFirebase.removeAllValues(city);
        SendCodeToFirebase.removeAllValues(city);
        SendItineraryToFirebase.removeAllValues(city);
        SendBusToFirebase.removeAllValues(city);
    }


    private void pushAllCities(){
        for(City city:cities){
            pushCity(city);
        }
    }

    public void pushCity(City city) {
        push(city);
        for (Company company : companies.get(city)) {
            pushCompany(city, company);
        }
    }

    private void pushCompany(City city, Company company) {
        push(city, company);
        System.out.println("\tcompany " + company.getName());
        List<Code> codesList = codes.get(city).get(company);
        List<Itinerary> itineraryList = itineraries.get(city).get(company);
        if (codesList == null) {
            System.out.println("\t\tcodeList null!" + company.getName());
        } else {
            for (Code code : codesList) {
                push(city, company, code);
            }
            System.out.println("\t\tcodeList size!" + codesList.size());
        }
        if (itineraryList == null) {
            System.out.println("\t\titineraryList null!" + company.getName());
        } else {
            for (Itinerary itinerary : itineraryList) {
                pushItinerary(city, company, itinerary);

            }
            System.out.println("\t\titineraryList size!" + itineraryList.size());
        }
    }

    private void pushItinerary(City city, Company company, Itinerary itinerary) {
        push(city, company, itinerary);
        HashMap<Itinerary, List<Bus>> busList = buses.get(city).get(company);
        List<Bus> busListed = busList.get(itinerary);
        if (busListed == null) {
            System.out.println("\t\t\tbusLited null!" + itinerary.getName());
        } else {
            push(city, company, itinerary, busListed);
            System.out.println("\t\t\tbusLited size!" + busListed.size());
        }
    }

    private void push(City city){
        SendCityToFirebase.sendToFirebase(city);
    }

    private void push(City city,Company company){
        SendCompanyToFirebase.sendToFirebase(city,company);
    }

    private void push(City city,Company company,Code code){
        SendCodeToFirebase.sendToFirebase(city,company,code);
    }

    private void push(City city,Company company,Itinerary itinerary){
        SendItineraryToFirebase.sendToFirebase(city,company,itinerary);
    }

    private void push(City city,Company company,Itinerary itinerary,List<Bus> bus){
        SendBusToFirebase.sendToFirebase(city,company,itinerary,bus);
    }
}
