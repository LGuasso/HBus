package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.TextUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael Correa
 * @since 31/07/2015
 */
public class TimesDbHelper extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "bus_data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_DELETE_ITINERARIES =
            "DROP TABLE IF EXISTS " + ItineraryContract.Itinerary.TABLE_NAME;

    private static final String[] COLS_CITY = {"_id","name","country","position"};
    private static final String[] COLS_BUS = {"_id","time","code","itinerary","way","typeday","city"};
    private static final String[] COLS_CODE = {"_id","name","description","city"};
    private static final String[] COLS_ITINERARY = {"_id","name","ways","city"};
    private static final String TABLE_CITY = "City";
    private static final String TABLE_BUS = "Bus";
    private static final String TABLE_CODE = "Code";
    private static final String TABLE_ITINERARY = "Itinerary";


    public TimesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //setForcedUpgrade(DATABASE_VERSION);
    }

    public List<City> getCities(){
        ArrayList<City> cities = new ArrayList<City>();
        Cursor c;

        c = getWritableDatabase().query(TABLE_CITY, COLS_CITY, null, null, null, null, null);
        while(c.moveToNext()){
            City city = new City();
            city.setId(c.getLong(0));
            city.setName(c.getString(1));
            city.setCountry(c.getString(2));
            city.setPosition(c.getString(3));
            cities.add(city);

        }
        c.close();
        return cities;
    }


    public List<Itinerary> getItineraries(){
        ArrayList<Itinerary> itinerariess = new ArrayList<Itinerary>();
        Cursor c;

        c = getWritableDatabase().query(TABLE_ITINERARY, COLS_ITINERARY, null, null, null, null, null);
        while(c.moveToNext()){
            Itinerary itinerary = new Itinerary();
            itinerary.setId(c.getLong(0));
            itinerary.setName(c.getString(1));
            itinerary.setWays(new ArrayList<String>(Arrays.asList(c.getString(2).split(","))));
            //itinerary.setCity(getCity(c.getString(3)));
            itinerariess.add(itinerary);

        }
        c.close();
        return itinerariess;
    }


    public void delete(Bus bus){
        //TODO implement app manager of times
        //getWritableDatabase().delete(TABLE, "name=?", new String[]{linha});
    }

    public City getCity(String cityName) {
        //ArrayList<Code> codes = new ArrayList<Code>();
        City city = new City();
        Cursor c;
        String where = "name = ? AND country = ?";
        String arguments[] = {cityName.split(" - ")[0],cityName.split(" - ")[1]};

        c = getWritableDatabase().query(TABLE_CITY, COLS_CITY, where, arguments, null, null, null);
        while (c.moveToNext()) {
            city = new City();
            city.setId(c.getLong(0));
            city.setName(c.getString(1));
            city.setCountry(c.getString(2));
            city.setPosition(c.getString(3));
        }
        c.close();
        return city;
    }


    public Itinerary getItinerary(String itineraryName) {
        //ArrayList<Code> codes = new ArrayList<Code>();
        Itinerary itinerary = new Itinerary();
        Cursor c;
        String where = "name = ?";
        String arguments[] = {itineraryName};

        c = getWritableDatabase().query(TABLE_ITINERARY, COLS_ITINERARY, where, arguments, null, null, null);
        while (c.moveToNext()) {
            itinerary = new Itinerary();
            itinerary.setId(c.getLong(0));
            itinerary.setName(c.getString(1));
            itinerary.setWays(new ArrayList<String>(Arrays.asList(c.getString(2).split(","))));
            //itinerary.setCity(getCity(c.getString(3)));
        }
        c.close();
        return itinerary;
    }
    public List<Code> getListaCode(){
        ArrayList<Code> codes = new ArrayList<Code>();
        Code code;
        Cursor c;

        c = getWritableDatabase().query(TABLE_CODE, COLS_CODE, null, null, null, null, null);
        while(c.moveToNext()){
            code = new Code();
            code.setName(c.getString(1));
            code.setDescrition(c.getString(2));
            code.setCity(getCity(c.getString(3)));
            codes.add(code);
        }
        c.close();

        return codes;
    }

    public Code getCode(String codeName) {
        Code code=new Code();
        Cursor c = null;
        String where = "name = ?";
        String[] arguments = {codeName};

        //Try and finally evita o erro de carregar sqlite
        //Na maioria das vezes a causa para este erro são cursores não fechadas. Certifique-se de fechar todos os cursores após usá-los (mesmo no caso de um erro).
        //@link{http://stackoverflow.com/questions/31361618/cursor-window-allocation-of-2048-kb-failed-open-cursors-1-cursors-opened-b}
        try{
            c = getWritableDatabase().query(TABLE_CODE, COLS_CODE, where, arguments, null, null, null);
            while(c.moveToNext()){
                code = new Code();
                code.setName(c.getString(1));
                code.setDescrition(c.getString(2));
                code.setCity(getCity(c.getString(3)));
                return code;
            }
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return code;
    }




    /**
     * Busca no banco de dados lista de {@link Bus} baseado nos parâmetros
     * @param nameLine Nome da linha
     * @param way Sentido do ônibus
     * @param typeDay tipo de dia {@link br.com.expressobits.hbus.model.TypeDay}
     * @return Lista de {@link Bus}
     */
    public List<Bus> getBusList(String nameLine, String way, String typeDay,boolean fastMode){
        ArrayList<Bus> buses = new ArrayList<Bus>();

        way = TextUtils.toSimpleNameWay(way);

        Cursor c;
        String where = "itinerary = ? AND way = ? AND typeday = ?";
        String arguments[] = {nameLine,way,typeDay};

        c = getWritableDatabase().query(TABLE_BUS, COLS_BUS, where, arguments, null, null, null);
        while(c.moveToNext()){
            Bus bus = new Bus();
            bus.setTime(c.getString(1));

            Code code;
            if(fastMode){
                code = new Code();
                code.setName(c.getString(2));
            }else{
                code = getCode(c.getString(2));
            }

            bus.setCode(code);
            Itinerary itinerary = new Itinerary();
            itinerary.setName(c.getString(3));
            bus.setItinerary(itinerary);

            bus.setWay(c.getString(4));


            bus.setTypeday(TimeUtils.getTypeDayforString(c.getString(5)));
            bus.setCity(getCity(c.getString(6)));
            buses.add(bus);
        }
        c.close();

        return buses;
    }

    /**
     * Busca no banco de dados lista de {@link Bus} baseado nos parâmetros
     * @param nameLine Nome da linha
     * @return Lista de {@link Bus}
     */
    public List<Bus> getBusList(String nameLine){
        ArrayList<Bus> buses = new ArrayList<Bus>();

        Cursor c;
        String where = "itinerary = ?";
        String arguments[] = {nameLine};

        c = getWritableDatabase().query(TABLE_BUS, COLS_BUS, where, arguments, null, null, null);
        while(c.moveToNext()){
            Bus bus = new Bus();
            bus.setTime(c.getString(1));

            Code code = getCode(c.getString(2));
            bus.setCode(code);

            Itinerary itinerary = new Itinerary();
            itinerary.setName(c.getString(3));
            bus.setItinerary(itinerary);

            bus.setWay(c.getString(4));


            bus.setTypeday(TimeUtils.getTypeDayforString(c.getString(5)));
            bus.setCity(getCity(c.getString(6)));
            buses.add(bus);
        }
        c.close();

        return buses;
    }



    public List<Bus> getNextBuses(Itinerary itinerary){

        ArrayList<Bus> next = new ArrayList<Bus>();
        for(int j = 0;j< itinerary.getWays().size();j++) {
            next.add(getNextBusforList(getBusList(itinerary.getName(), itinerary.getWays().get(j), TimeUtils.getStringTipoDeDia(Calendar.getInstance()).toString(), false)));
        }
        return next;
    }

    private Bus getNextBusforList(List<Bus> buses){
        //TODO Create metodo separado
        Bus nowBus = new Bus();
        Bus nextBus;
        nowBus.setTime(TimeUtils.getNowTimeinString());
        if(buses.size() > 0) {
            nextBus = buses.get(0);
            for (int i = 0; i < buses.size(); i++) {
                nextBus = buses.get(i);
                if (nowBus.compareTo(nextBus) <= 0) {
                    nextBus = buses.get(i);
                    return nextBus;
                } else {

                }
            }
            return nextBus;
        }else{
            nowBus.setTime(" --- ");
            return nowBus;
        }
    }

}
