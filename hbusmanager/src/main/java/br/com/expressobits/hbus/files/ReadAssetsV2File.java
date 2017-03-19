package br.com.expressobits.hbus.files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.dao.SQLConstants;
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
 * @since 15/03/17
 */

public class ReadAssetsV2File {

    private static final String BARS = "/";
    private static final String SPLIT_FILE_TIME = " ";
    private static final String SPLIT_FILE = ";";
    private static final String SPLIT_FILE_SECONDARY = ",";
    private static final String SPLIT_FILE_CODE_ASSETSV2 = " - ";
    private static final String CITIES_FILE = "cities.csv";
    private static final String ITINERARIES_FILE = "itineraries.csv";
    private static final String COMPANIES_FILE = "companies.csv";
    private static final String CODES_FILE = "codes.csv";
    private static final String TAG = "FILEGENERATOR";
    private static final String FORMAT = ".csv";
    private static final String ASSET_DIRECTORY = "assets-v2";
    private static final boolean DEBUG = true;
    private String country;

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
        code.setName(text.split(SPLIT_FILE_CODE_ASSETSV2)[0].replace(" ",""));
        if(text.split(SPLIT_FILE_CODE_ASSETSV2).length>1){
            code.setDescrition(text.split(SPLIT_FILE_CODE_ASSETSV2)[1]);
        }
        return code;
    }

    public Bus toBus(String text){
        Bus bus = new Bus();
        if(text.contains(" ")){
            bus.setTime(TimeUtils.getTimeInCalendar(text.split(SPLIT_FILE_TIME)[0]).getTimeInMillis());
            if(text.split(SPLIT_FILE_TIME).length>1){
                try{
                    bus.setCode(text.replace(text.split(SPLIT_FILE_TIME)[0]+SPLIT_FILE_TIME,""));
                }catch (Exception e){
                    System.out.println(" "+"TEXTO("+text+")");
                    bus.setCode("NOT CODE");
                }
            }else{
                bus.setCode("NOT CODE");
            }
        }else{
            bus.setTime(TimeUtils.getTimeInCalendar(text).getTimeInMillis());
            bus.setCode("NOT CODE");
        }

        return bus;
    }


    public List<Itinerary> getItineraries(City city, Company company){
        List<Itinerary> itineraries = new ArrayList<>();
        for(String text:readFile(DEBUG,ASSET_DIRECTORY+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+ITINERARIES_FILE)){
            Itinerary itinerary = toItinerary(text);
            itinerary.setId(SQLConstants.getIdItinerary(city.getCountry(),city.getName(),company.getName(),itinerary.getName()));
            itineraries.add(itinerary);
        }
        return itineraries;
    }

    public HashMap<Itinerary,List<Code>> getCodes(City city, Company company, List<Itinerary> itineraries){
        HashMap<Itinerary,List<Code>> codesHash = new HashMap<>();
        for(Itinerary itinerary:itineraries){
            List<Code> codes = new ArrayList<>();
            for(String text:readFile(DEBUG,ASSET_DIRECTORY+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+
                    StringUtils.toSimpleNameFile(itinerary.getName())+BARS+CODES_FILE)){
                Code code = toCode(text);
                code.setId(SQLConstants.getIdCode(city.getCountry(),city.getName(),company.getName(),itinerary.getName(),code.getName()));
                if(codes.contains(code)){
                    System.err.println("CODE EXISTS CODE:"+code.getName());
                }else {
                    codes.add(code);
                }

            }
            codesHash.put(itinerary,codes);
        }
        return codesHash;
    }

    public HashMap<Itinerary,HashMap<String,Code>> getHashCodes(City city, Company company, List<Itinerary> itineraries){
        HashMap<Itinerary,HashMap<String,Code>> codesHash = new HashMap<>();
        for(Itinerary itinerary:itineraries){
            HashMap<String,Code> codes = new HashMap<>();
            for(String text:readFile(DEBUG,ASSET_DIRECTORY+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+
                    StringUtils.toSimpleNameFile(itinerary.getName())+BARS+CODES_FILE)){
                Code code = toCode(text);
                code.setId(SQLConstants.getIdCode(city.getCountry(),city.getName(),company.getName(),itinerary.getName(),code.getName()));
                if(codes.containsKey(code.getName())){
                    System.err.println("CODE EXISTS CODE:"+code.getName());
                }else {
                    codes.put(code.getName(),code);
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
                                ASSET_DIRECTORY+BARS+
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
            System.out.println("BUS encontrado com mesmo tempo ANTES"+ SQLConstants.getIdBus(city.getCountry(),city.getName(),company.getName(),
                    itinerary.getName(),way, StringUtils.getTypeDayInt(i),String.valueOf(bus.getTime())));
            bus.setTime(bus.getTime()+1L);
            System.out.println("BUS encontrado com mesmo tempo DEPOIS"+SQLConstants.getIdBus(city.getCountry(),city.getName(),company.getName(),
                    itinerary.getName(),way,StringUtils.getTypeDayInt(i),String.valueOf(bus.getTime())));
        }
    }

}
