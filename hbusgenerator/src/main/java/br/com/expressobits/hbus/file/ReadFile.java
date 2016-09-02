package br.com.expressobits.hbus.file;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.HoursUtils;
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
    private static final String CITIES_FILE = "BR/cities.dat";
    private static final String ITINERARIES_FILE = "itineraries.dat";
    private static final String COMPANIES_FILE = "companies.dat";
    private static final String CODES_FILE = "codes.dat";
    private static final String TAG = "FILEGENERATOR";
    private static final String FORMAT = ".dat";
    Context context;


    public ReadFile(Context context){
        this.context = context;
    }

    public City toCity(String text){
        City city = new City();
        city.setActived(text.split(SPLIT_FILE)[0].equals("1"));
        city.setName(text.split(SPLIT_FILE)[1]);
        city.setCountry(text.split(SPLIT_FILE)[2]);
        city.setCompanyDefault(text.split(SPLIT_FILE)[3]);
        HashMap<String,Double> hashMap = new HashMap<>();
        hashMap.put(CityContract.City.COLUMN_NAME_LATITUDE,Double.parseDouble(text.split(SPLIT_FILE)[4].split(SPLIT_FILE_SECONDARY)[0]));
        hashMap.put(CityContract.City.COLUMN_NAME_LONGITUDE,Double.parseDouble(text.split(SPLIT_FILE)[4].split(SPLIT_FILE_SECONDARY)[1]));
        city.setLocalization(hashMap);
        return city;
    }

    public Company toCompany(String text){
        Company company = new Company();

        company.setActived(text.split(SPLIT_FILE)[0].equals("1"));
        company.setName(text.split(SPLIT_FILE)[1]);
        company.setEmail(text.split(SPLIT_FILE)[2]);
        company.setWebsite(text.split(SPLIT_FILE)[3]);
        company.setPhoneNumber(text.split(SPLIT_FILE)[3]);
        return company;
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
        bus.setTime(HoursUtils.getTimeInCalendar(text.split(SPLIT_FILE_TIMES)[0]).getTimeInMillis());
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

    public List<Company> getCompanies(City city){
        List<Company> companies = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+city.getName()+BARS+COMPANIES_FILE)){
            companies.add(toCompany(text));
        }
        return companies;
    }

    public List<Itinerary> getItineraries(City city,Company company){
        List<Itinerary> itineraries = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+ITINERARIES_FILE)){
            itineraries.add(toItinerary(text));
        }
        return itineraries;
    }

    public List<Code> getCodes(City city,Company company){
        List<Code> codes = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+CODES_FILE)){
            codes.add(toCode(text));
        }
        return codes;
    }

    public List<Bus> getBuses(City city,Company company,Itinerary itinerary,String way,String typeday){


        List<Bus> buses = new ArrayList<>();
        for(String text:readFile(city.getCountry()+BARS+
                        city.getName()+BARS+company.getName()+BARS+
                        TextUtils.toSimpleNameFile(itinerary.getName())+BARS+
                        TextUtils.toSimpleNameWay(way)+"_"+typeday+FORMAT
        )){
            buses.add(toBus(text));
        }
        return buses;
    }

    public HashMap<Itinerary,List<Bus>> getBuses(City city,Company company,List<Itinerary> itineraries){


        HashMap<Itinerary,List<Bus>> buses = new HashMap<>();
        for(Itinerary itinerary:itineraries){


            List<Bus> buses1 = new ArrayList<>();
            for(String way:itinerary.getWays()){
                for(int i=0;i<3;i++){
                    for(String text:readFile(city.getCountry() + BARS +
                                    city.getName() + BARS +company.getName()+BARS+
                                    TextUtils.toSimpleNameFile(itinerary.getName()) + BARS +
                                    TextUtils.toSimpleNameWay(way) + "_" + TextUtils.getTypeDayInt(i) + FORMAT
                    )){
                        Bus bus = toBus(text);
                        bus.setWay(way);
                        bus.setTypeday(TextUtils.getTypeDayInt(i));
                        buses1.add(bus);
                    }
                }
            }
            buses.put(itinerary, buses1);

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
