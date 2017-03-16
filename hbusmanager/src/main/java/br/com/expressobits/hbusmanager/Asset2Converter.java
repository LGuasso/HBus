package br.com.expressobits.hbusmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.files.ReadAssetsV2File;
import br.com.expressobits.hbus.files.WriteAssetsV2File;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * @author Rafael Correa
 * @since 14/03/17
 */

public class Asset2Converter {

    private City city;
    private List<Company> companies;
    private HashMap<Company,List<Itinerary>> itineraries = new HashMap<>();
    private HashMap<Company,HashMap<Itinerary,List<Code>>> codes = new HashMap<>();
    private HashMap<Company,HashMap<Itinerary,HashMap<String,Code>>> codesNoFormatedHash = new HashMap<>();
    private HashMap<Company,HashMap<Itinerary,List<Bus>>> buses = new HashMap<>();

    public void setCity(City city){
        this.city = city;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public void read(){
        ReadAssetsV2File readAssetsV2File = new ReadAssetsV2File();
        for (Company company:companies){
            itineraries.put(company,readAssetsV2File.getItineraries(city,company));
            codesNoFormatedHash.put(company,readAssetsV2File.getHashCodes(city,company,itineraries.get(company)));
            buses.put(company,readAssetsV2File.getBuses(city,company,itineraries.get(company)));
        }
    }

    public void write(){
        WriteAssetsV2File writeAssetsV2File = new WriteAssetsV2File();
        for(Company company:companies){
            codes.put(company,new HashMap<>());
            writeAssetsV2File.writeItineraries(city,company,itineraries.get(company));

            for(Itinerary itinerary:itineraries.get(company)){

                HashMap<String,Code> codesFormated = new HashMap<>();
                List<Code> codesList = new ArrayList<>();
                for (Bus bus:buses.get(company).get(itinerary)){
                    String codeName = bus.getCode();
                    if(!codesFormated.containsKey(codeName.replace(" ",""))){
                        Code code = new Code();
                        code.setName(codeName.replace(" ",""));
                        code.setDescrition(getDescriptionCodeForamted(company,itinerary,codeName));
                        codesFormated.put(codeName.replace(" ",""),code);
                        codesList.add(code);
                    }

                    bus.setCode(codeName.replace(" ",""));
                }
                codes.get(company).put(itinerary,codesList);
            }

            writeAssetsV2File.writeBuses(city,company,itineraries.get(company),buses.get(company));
            writeAssetsV2File.writeCodes(city,company,itineraries.get(company),codes.get(company));
        }
    }

    public void testPrint(){

        for(Company company:companies){
            for(Itinerary itinerary:itineraries.get(company)){
                System.out.println("V2 - >"+itinerary.getName());
                /**for (Code code:codes.get(company).get(itinerary)){
                    //System.out.println("V2 - >"+"\t"+code.getName());
                }*/
                for(Bus bus:buses.get(company).get(itinerary)){
                    System.out.println("V2 - >"+"\t"+bus.getCode());
                }
            }
        }
    }


    /**public String getCodeFormated(Company company,Itinerary itinerary,String codeNoFormated){
        return codesNoFormated.get(company).get(itinerary).contains()
    }*/


    public String getDescriptionCodeForamted(Company company,Itinerary itinerary,String codeNoFormated){
        String texto = "";
        for(String text:codeNoFormated.split(" ")){
            texto=texto.concat(","+codesNoFormatedHash.get(company).get(itinerary).get(text).getDescrition());
        }
        texto=texto.replaceFirst(",","");
        return texto;
    }



}
