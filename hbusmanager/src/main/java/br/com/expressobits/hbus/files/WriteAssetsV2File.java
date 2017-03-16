package br.com.expressobits.hbus.files;

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

/**
 * @author Rafael Correa
 * @since 15/03/17
 */

public class WriteAssetsV2File {

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

    public String toText(Itinerary itinerary){
        String texto = itinerary.getName();
        texto=texto.concat(SPLIT_FILE);
        texto=texto.concat(StringUtils.getSentidosinString(itinerary.getWays()));
        return texto;
    }

    public String toText(Code code){
        String texto = code.getName();
        texto=texto.concat(SPLIT_FILE);
        texto=texto.concat(code.getDescrition());
        return texto;
    }

    public String toText(Bus bus){
        String texto = TimeUtils.getFormatTime(bus.getTime());
        texto=texto.concat(SPLIT_FILE);
        texto=texto.concat(bus.getCode());
        return texto;
    }


    public void writeItineraries(City city, Company company,List<Itinerary> itineraries){
        for(Itinerary itinerary:itineraries){
            FileManager.write(true,toText(itinerary),"assets"+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+ITINERARIES_FILE);
        }
    }

    public void writeCodes(City city, Company company, List<Itinerary> itineraries, HashMap<Itinerary,List<Code>> codes){

        for(Itinerary itinerary:itineraries){
            for(Code code:codes.get(itinerary)){
                FileManager.write(true,
                        toText(code),
                        "assets"+BARS+city.getCountry()+BARS+city.getName()+BARS+company.getName()+BARS+
                                StringUtils.toSimpleNameFile(itinerary.getName())+BARS+CODES_FILE);


            }

        }
    }

    public void writeBuses(
            City city,Company company,List<Itinerary> itineraries,HashMap<Itinerary,List<Bus>> buses){
        for(Itinerary itinerary:itineraries){
            List<Bus> buses1 = buses.get(itinerary);

            for (Bus bus:buses1){
                FileManager.write(DEBUG,
                        toText(bus),
                        "assets"+BARS+
                                city.getCountry() + BARS +
                                city.getName() + BARS +company.getName()+BARS+
                                StringUtils.toSimpleNameFile(itinerary.getName()) + BARS +
                                StringUtils.toSimpleNameWay(bus.getWay()) + "_" + bus.getTypeday() + FORMAT);
            }
        }
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
