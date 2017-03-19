package br.com.expressobits.hbus.files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.dao.SendDataToSQL;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.StringUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

import static br.com.expressobits.hbus.files.FileManager.readFile;

/**
 * @author Rafael Correa
 * @since 09/12/16
 */

public class ReadAssetsV1File {

    private static final String BARS = "/";
    private static final String SPLIT_FILE = ";";
    private static final String SPLIT_FILE_SECONDARY = ",";
    private static final String CITIES_FILE = "cities.csv";
    private static final String ITINERARIES_FILE = "itineraries.csv";
    private static final String COMPANIES_FILE = "companies.csv";
    private static final String CODES_FILE = "codes.csv";
    private static final String TAG = "FILEGENERATOR";
    private static final String FORMAT = ".csv";
    private static final boolean DEBUG = false;
    private String country;

    public City toCity(String text){
        City city = new City();
        city.setActived(text.split(SPLIT_FILE)[0].equals("1"));
        city.setName(text.split(SPLIT_FILE)[1]);
        city.setCountry(country);
        city.setCompanyDefault(text.split(SPLIT_FILE)[2]);
        HashMap<String,Double> hashMap = new HashMap<>();
        hashMap.put("latitude",Double.parseDouble(text.split(SPLIT_FILE)[3].split(SPLIT_FILE_SECONDARY)[0]));
        hashMap.put("longitude",Double.parseDouble(text.split(SPLIT_FILE)[3].split(SPLIT_FILE_SECONDARY)[1]));
        city.setLocalization(hashMap);
        return city;
    }

    public Company toCompany(String text){
        Company company = new Company();
        company.setName(text.split(SPLIT_FILE)[0]);
        company.setEmail(text.split(SPLIT_FILE)[1]);
        company.setWebsite(text.split(SPLIT_FILE)[2]);
        company.setPhoneNumber(text.split(SPLIT_FILE)[3]);
        company.setAddress(text.split(SPLIT_FILE).length<5?"":text.split(SPLIT_FILE)[4]);
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
        bus.setTime(TimeUtils.getTimeInCalendar(text.split(SPLIT_FILE)[0]).getTimeInMillis());

        try{
            bus.setCode(text.split(SPLIT_FILE)[1]);
        }catch (Exception e){
            //System.out.println(" "+"TEXTO("+text+")");
            bus.setCode("");

        }
        return bus;
    }

    public List<City> getCities(String country){
        this.country = country;
        List<City> cities = new ArrayList<>();
        for(String text:readFile(DEBUG,"assets"+BARS+country+BARS+CITIES_FILE)){
            cities.add(toCity(text));
        }
        return cities;
    }

    public List<Company> getCompanies(City city){
        List<Company> companies = new ArrayList<>();
        for(String text:readFile(DEBUG,"assets"+BARS+city.getCountry()+BARS+city.getName()+BARS+COMPANIES_FILE)){
            try{
                Company company = toCompany(text);
                company.setId(SQLConstants.getIdCompany(city.getCountry(),city.getName(),company.getName()));
                companies.add(company);
            }catch (Exception e){
                System.err.println("Erro ao converter compania "+text);
                e.printStackTrace();
            }

        }
        return companies;
    }

    public List<Itinerary> getItineraries(City city,Company company){
        List<Itinerary> itineraries = new ArrayList<>();
        for(String text:readFile(DEBUG,"assets"+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+ITINERARIES_FILE)){
            Itinerary itinerary = toItinerary(text);
            itinerary.setId(SQLConstants.getIdItinerary(city.getCountry(),city.getName(),company.getName(),itinerary.getName()));
            itineraries.add(itinerary);
        }
        return itineraries;
    }

    public HashMap<Itinerary,List<Code>> getCodes(City city,Company company,List<Itinerary> itineraries){
        HashMap<Itinerary,List<Code>> codesHash = new HashMap<>();
        for(Itinerary itinerary:itineraries){
            List<Code> codes = new ArrayList<>();
            for(String text:readFile(DEBUG,"assets"+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+
                    StringUtils.toSimpleNameFile(itinerary.getName())+BARS+CODES_FILE)){
                Code code = toCode(text);
                if(SendDataToSQL.DATABASE_VERSION==1){
                    code.setId(SQLConstants.getIdCodeVersion1(city.getCountry(),city.getName(),company.getName(),code.getName()));
                }else{
                    code.setId(SQLConstants.getIdCode(city.getCountry(),city.getName(),company.getName(),itinerary.getName(),code.getName()));
                }
                if(codes.contains(code)){
                    System.err.println(company.getName()+"/"+itinerary.getName()+"/CODE EXISTS CODE:"+code.getName());
                }else {
                    codes.add(code);
                }

            }
            codesHash.put(itinerary,codes);
        }
        return codesHash;
    }

    public HashMap<Itinerary,List<Bus>> getBuses(City city,Company company,List<Itinerary> itineraries){


        HashMap<Itinerary,List<Bus>> buses = new HashMap<>();
        for(Itinerary itinerary:itineraries){


            List<Bus> buses1 = new ArrayList<>();
            for(String way:itinerary.getWays()){
                if(itinerary.getWays().size()==1&&way.equals("")){
                    System.err.println(">>>"+itinerary.getName().toUpperCase()+" NAO EXISTE WAYS!");
                }else{
                    for(int i=0;i<3;i++){
                        for(String text: FileManager.readFile(
                                DEBUG,
                                "assets"+BARS+
                                city.getCountry() + BARS +
                                city.getName() + BARS +company.getName()+BARS+
                                StringUtils.toSimpleNameFile(itinerary.getName()) + BARS +
                                StringUtils.toSimpleNameWay(way) + "_" + StringUtils.getTypeDayInt(i) + FORMAT
                        )){
                            try{
                                Bus bus = toBus(text);
                                verifyEqualTimes(city, company, itinerary, buses1, way, i, bus);
                                bus.setId(SQLConstants.getIdBus(city.getCountry(),city.getName(),company.getName(),
                                        itinerary.getName(),way,StringUtils.getTypeDayInt(i),String.valueOf(bus.getTime())));
                                bus.setWay(way);
                                bus.setTypeday(StringUtils.getTypeDayInt(i));
                                buses1.add(bus);
                            }catch (Exception e){
                                e.printStackTrace();
                                System.out.println("Erro ao transformar bus "+itinerary.getName()+" "+way+" "+ StringUtils.getTypeDayInt(i));
                                System.exit(1);
                            }

                        }
                    }
                }

            }
            buses.put(itinerary, buses1);

        }


        return buses;
    }

    /**
     * Método que verifica se existe mesmo time in bus
     * @param city Cidade
     * @param company compania
     * @param itinerary itinerário
     * @param buses1 lista de ônibus
     * @param way Sentido
     * @param i iterator
     * @param bus ônibus
     */
    private void verifyEqualTimes(City city, Company company, Itinerary itinerary, List<Bus> buses1, String way, int i, Bus bus) {
        while (buses1.contains(bus)){
            if(DEBUG){
                System.out.println("BUS encontrado com mesmo tempo ANTES"+ SQLConstants.getIdBus(city.getCountry(),city.getName(),company.getName(),
                        itinerary.getName(),way, StringUtils.getTypeDayInt(i),String.valueOf(bus.getTime())));
            }

            bus.setTime(bus.getTime()+1L);
            if(DEBUG){
                System.out.println("BUS encontrado com mesmo tempo DEPOIS"+SQLConstants.getIdBus(city.getCountry(),city.getName(),company.getName(),
                        itinerary.getName(),way,StringUtils.getTypeDayInt(i),String.valueOf(bus.getTime())));
            }



        }
    }



    public void setCountry(String country) {
        this.country = country;
    }
}
