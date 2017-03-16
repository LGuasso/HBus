package br.com.expressobits.hbusmanager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;

/**
 * @author Rafael
 * @since 29/11/16
 */
public class MainTest {

    public static void main(String[] args) throws FileNotFoundException {


        /*FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("HBus-908dede63e15.json"))
                .setDatabaseUrl("https://hbus-1206.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);*/


        /**SendData sendData = new SendData();
        City city = sendData.readData("BR/RS","Cruz Alta");
        sendData.pushCity(city);
        JOptionPane.showMessageDialog(null,"Aguarde!");*/

        //HBusManager hBusManager = new HBusManager();

        runV2();

        /*Itinerary itinerary = new Itinerary();
        itinerary.setId("/BR/RS/Santa Maria/SIMSM/7 de Dezembro");
        itinerary.setName("7 de Dezembro");
        List<String> sentidos = new ArrayList<>();
        sentidos.add("Centro > Bairro");
        sentidos.add("Bairro > Centro");
        itinerary.setWays(sentidos);

        Code code = new Code();
        code.setId("/BR/RS/Santa Maria/SIMSM/150a");
        code.setName("150a");
        code.setDescrition("Vai bem longe ele");

        Bus bus = new Bus();
        bus.setId("/BR/RS/Santa Maria/SIMSM/7 de Dezembro/Centro > Bairro/useful");
        bus.setTime(System.currentTimeMillis());
        bus.setCode("150a");

        Company company = new Company();
        company.setId("/BR/RS/Santa Maria/SIMSM");
        company.setName("SIMSM");
        company.setEmail("simsm@gmail.cm");
        company.setPhoneNumber("665987892");
        company.setWebsite("www.simsm.com.br");
        company.setAddress("Rua longe");

        ScheduleSQLite scheduleSQLite = new ScheduleSQLite("schedule/BR/RS/Santa Maria/schedule_br_rs_santa_maria_1.db");
        try {

            scheduleSQLite.createTables();
            scheduleSQLite.insert(company);
            scheduleSQLite.insert(code);
            scheduleSQLite.insert(itinerary);
            scheduleSQLite.insert(bus);
            scheduleSQLite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    public static void runV2(){
        City city = new City();
        city.setName("Santa Maria");
        city.setCountry("BR/RS");
        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setName("Expresso Medianeira");
        companies.add(company);
        Asset2Converter asset2Converter = new Asset2Converter();
        asset2Converter.setCity(city);
        asset2Converter.setCompanies(companies);
        asset2Converter.read();

        asset2Converter.write();
    }
}
