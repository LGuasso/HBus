package br.com.expressobits.hbus.push;

import br.com.expressobits.hbus.Sender;
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

public class SendData{

    List<City> cities = new ArrayList<>();
    HashMap<City, List<Company>> companies = new HashMap<>();
    HashMap<City, HashMap<Company, List<Code>>> codes = new HashMap<>();
    HashMap<City, HashMap<Company, List<Itinerary>>> itineraries = new HashMap<>();
    HashMap<City, HashMap<Company, HashMap<Itinerary, List<Bus>>>> buses = new HashMap<>();

    private boolean isCitySend = false;
    private boolean isCompanySend = false;
    private boolean isItinerarySend = false;
    private boolean isCodeSend = false;
    private boolean isBusSend = false;

    private ReadListener readListener;

    public ReadFile readFile;

    public SendData(){
        readFile = new ReadFile();
    }

    public boolean isBusSend() {
        return isBusSend;
    }

    public boolean isCitySend() {
        return isCitySend;
    }

    public boolean isCodeSend() {
        return isCodeSend;
    }

    public boolean isCompanySend() {
        return isCompanySend;
    }

    public boolean isItinerarySend() {
        return isItinerarySend;
    }

    public void setBusSend(boolean busSend) {
        isBusSend = busSend;
    }

    public void setCitySend(boolean citySend) {
        isCitySend = citySend;
    }

    public void setCodeSend(boolean codeSend) {
        isCodeSend = codeSend;
    }

    public void setCompanySend(boolean companySend) {
        isCompanySend = companySend;
    }

    public void setItinerarySend(boolean itinerarySend) {
        isItinerarySend = itinerarySend;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Company> getCompanies(City city){
        return companies.get(city);
    }

    public List<Itinerary> getItineraries(City city, Company company){
        return itineraries.get(city).get(company);
    }

    public List<Code> getCodes(City city, Company company){
        return codes.get(city).get(company);
    }

    public int getSizeBusesOfItinerary(City city, Company company, Itinerary itinerary){
        return buses.get(city).get(company).get(itinerary).size();
    }

    public void readDataCities(String country) {
        cities = readFile.getCities(country);

    }

    public City readData(String country,City city1) {
        cities = readFile.getCities(country);
        for (City city : cities) {
            if(city.getName().equals(city1.getName())){
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

    /**public void removeAllValuesFromCity(String country,String cityName){
        cities = readFile.getCities(country);
        for(City city:cities){
            if(city.getName().equals(cityName)){
                removeAllValuesFromCity(city);
            }
        }
    }

    public void removeAllValuesFromCity(City city){
        SendCityToFirebase.removeAllValues(city);
        SendCompanyToFirebase.removeAllValues(city,null);
        SendCodeToFirebase.removeAllValues(city,null);
        SendItineraryToFirebase.removeAllValues(city,null);
        SendBusToFirebase.removeAllValues(city,null);
    }*/


    public void pushCity(City city,Company company) {
        if(isCitySend){
            push(city);
        }

        pushCompany(city,company);

    }



    private void pushCompany(City city, Company company) {
        if(isCompanySend){
            push(city, company);
            System.out.println("\tcompany " + company.getName());
        }

        List<Code> codesList = codes.get(city).get(company);
        List<Itinerary> itineraryList = itineraries.get(city).get(company);
        if (codesList == null) {
            System.out.println("\t\tcodeList null!" + company.getName());
        } else {
            if(isCodeSend){
                for (Code code : codesList) {
                    push(city, company, code);
                }
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

    public void pushItinerary(City city, Company company, Itinerary itinerary) {
        if(isItinerarySend) {
            push(city, company, itinerary);
        }
        HashMap<Itinerary, List<Bus>> busList = buses.get(city).get(company);
        List<Bus> busListed = busList.get(itinerary);
        if (busListed == null) {
            System.out.println("\t\t\tbusLited null!" + itinerary.getName());
        } else {
            if(isBusSend) {
                push(city, company, itinerary, busListed);
                System.out.println("\t\t\tbusLited size!" + busListed.size());
            }
        }
    }

    public void push(City city){
        SendCityToFirebase.sendToFirebase(city);
    }

    public void push(City city,Company company){
        SendCompanyToFirebase.sendToFirebase(city,company);
    }

    public void push(City city,Company company,Code code){
        SendCodeToFirebase.sendToFirebase(city,company,code);
    }

    public void push(City city,Company company,Itinerary itinerary){
        SendItineraryToFirebase.sendToFirebase(city,company,itinerary);
    }

    public void push(City city,Company company,Itinerary itinerary,List<Bus> bus){
        SendBusToFirebase.sendToFirebase(city,company,itinerary,bus);
    }

}
