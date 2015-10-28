package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.TextUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael Correa
 * @since 31/07/2015
 */
public class BusDAO extends SQLiteAssetHelper{


    private static final String[] COLS_BUS = {"_id","time","code","itinerary","way","typeday"};
    private static final String[] COLS_CODE = {"_id","name","descrition"};
    private static final String[] COLS_ITINERARY = {"_id","name","favorite","sentidos"};
    private static final String TABLE_BUS = "Bus";
    private static final String TABLE_CODE = "Code";
    private static final String TABLE_ITINERARY = "Itinerary";



    private static final String DB_PATH = "/data/data/br.com.expressobits.hbus/databases/";
    private static final String DATABASE_NAME = "santa_maria_rs_bus_data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG= "DAO";

    public BusDAO(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,"Have "+getListaCode().size()+" codes in bd");
    }


    public void deleteAll(){
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS "+ TABLE_ITINERARY);
        getWritableDatabase().execSQL(sb.toString());

        sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS "+ TABLE_CODE);
        getWritableDatabase().execSQL(sb.toString());

        sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS " + TABLE_BUS);
        getWritableDatabase().execSQL(sb.toString());

        onCreate(getWritableDatabase());
    }

    private void createTableItinerary(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_ITINERARY + " ");
        sb.append("(_id INTEGER PRIMARY KEY, ");
        sb.append(" name TEXT,");
        sb.append(" favorite INTEGER,");
        sb.append(" sentidos TEXT);");


        db.execSQL(sb.toString());
    }

    private void createTableBus(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_BUS + " ");
        sb.append("(_id INTEGER PRIMARY KEY, ");
        sb.append(" time TEXT,");
        sb.append(" code TEXT,");
        sb.append(" itinerary TEXT,");
        sb.append(" way TEXT,");
        sb.append(" typeday TEXT);");
        db.execSQL(sb.toString());
    }

    private void createTableCode(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_CODE + " ");
        sb.append("(_id INTEGER PRIMARY KEY, ");
        sb.append(" name TEXT,");
        sb.append(" descrition TEXT);");
        db.execSQL(sb.toString());
    }


    /**
     * Insere novo codigo de �nibus no banco de dados
     * @param itinerary
     */
    public void insert(Itinerary itinerary){
        ContentValues c = new ContentValues();
        c.put("name", itinerary.getName());
        c.put("favorite", itinerary.getFavorite());
        String sentido="";

        for (String sb : itinerary.getSentidos()){
            sentido+=sb;
            sentido+=",";
        }
        sentido=sentido.substring(0, sentido.length()-1);
        c.put("sentidos", sentido);
        getWritableDatabase().insert(TABLE_ITINERARY, null, c);
    }


    /**
     * Insere novo codigo de �nibus no banco de dados
     * @param code
     */
    public void insert(Code code){
        ContentValues c = new ContentValues();
        c.put("name", code.getName());
        c.put("descrition", code.getDescrition());
        getWritableDatabase().insert(TABLE_CODE, null, c);
    }

    /**
     * Insere novo hor�rio de �nibus no banco de dados
     * @param bus
     */
    public void insert(Bus bus){
        ContentValues c = new ContentValues();
        c.put("time", bus.getTime());
        c.put("code", bus.getCode().getName());
        c.put("itinerary", bus.getItinerary().getName());
        //c.put("way", LinhaFile.toSimpleName(bus.getWay()));
        c.put("typeDay", bus.getTypeday().toString());
        getWritableDatabase().insert(TABLE_BUS, null, c);
    }

    /**
     * Insere novo codigo de �nibus no banco de dados
     * @param itinerary
     */
    public void update(Itinerary itinerary){
        ContentValues c = new ContentValues();
        c.put("name", itinerary.getName());
        c.put("favorite", itinerary.getFavorite());
        String where = "name = ?";
        String arguments[] = {itinerary.getName()};
        getWritableDatabase().update(TABLE_ITINERARY, c, where, arguments);
    }

    public List<Itinerary> getItineraries(){
        ArrayList<Itinerary> itinerariess = new ArrayList<Itinerary>();
        Cursor c;

        c = getWritableDatabase().query(TABLE_ITINERARY, COLS_ITINERARY, null, null, null, null, null);
        while(c.moveToNext()){
            Itinerary itinerary = new Itinerary();
            itinerary.setName(c.getString(1));

            if(c.getString(2).equals("0")){
                itinerary.setFavorite(false);
            }else{
                itinerary.setFavorite(true);
            }

            itinerary.setSentidos(new ArrayList<String>(Arrays.asList(c.getString(3).split(","))));
            itinerariess.add(itinerary);

        }
        c.close();
        return itinerariess;
    }

    /**
     * Retorna linhas favoritas ou n�o favoritas baseadas no flag.
     * @param favorite flag que define se queremos uma linha favorita ou n�o favorita
     * @return Lista de itiner�rios
     * @since 01/08/2015
     */
    public List<Itinerary> getItineraries(boolean favorite){
        ArrayList<Itinerary> itinerariess = new ArrayList<Itinerary>();
        Cursor c;
        String where = "favorite = ?";
        String arguments[];
        if(favorite){
            arguments = new String[]{"1"};
        }else{
            arguments = new String[]{"0"};
        }


        c = getWritableDatabase().query(TABLE_ITINERARY, COLS_ITINERARY, where, arguments, null, null, null);
        while(c.moveToNext()){
            Itinerary itinerary = new Itinerary();
            itinerary.setName(c.getString(1));
            itinerary.setFavorite(favorite);
            itinerary.setSentidos(new ArrayList<String>(Arrays.asList(c.getString(3).split(","))));
            itinerariess.add(itinerary);

        }
        c.close();
        return itinerariess;
    }


    public void delete(Bus bus){
        //TODO implement app manager of times
        //getWritableDatabase().delete(TABLE, "name=?", new String[]{linha});
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
            itinerary.setName(c.getString(1));
            if(c.getString(2).equals("0")){
                itinerary.setFavorite(false);
            } else {
                itinerary.setFavorite(true);
            }
            itinerary.setSentidos(new ArrayList<String>(Arrays.asList(c.getString(3).split(","))));
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
            codes.add(code);
        }
        c.close();

        return codes;
    }

    public Code getCode(String codeName) {
        Code code=new Code();
        Cursor c;
        String where = "name = ?";
        String[] arguments = {codeName};

        c = getWritableDatabase().query(TABLE_CODE, COLS_CODE, where, arguments, null, null, null);
        while(c.moveToNext()){
            code = new Code();
            code.setName(c.getString(1));
            code.setDescrition(c.getString(2));
            return code;
        }
        c.close();

        return code;
    }




    /**
         * Busca no banco de dados lista de {@link Bus} baseado nos par�metros
         * @param nameLine Nome da linha
         * @param way Sentido do �nibus
         * @param typeDay tipo de dia {@link br.com.expressobits.hbus.model.TypeDay}
         * @return Lista de {@link Bus}
         */
    public List<Bus> getBusList(String nameLine, String way, String typeDay,boolean fastMode){
        ArrayList<Bus> buses = new ArrayList<Bus>();

        way = TextUtils.toSimpleName(way);

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
            buses.add(bus);
        }
        c.close();

        return buses;
    }

    /**
     * Busca no banco de dados lista de {@link Bus} baseado nos par�metros
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
            buses.add(bus);
        }
        c.close();

        return buses;
    }



    public List<Bus> getNextBuses(Itinerary itinerary){

        ArrayList<Bus> next = new ArrayList<Bus>();
        //TODO Typeday set por dia identificar o dia do typeday
        for(int j = 0;j< itinerary.getSentidos().size();j++) {
            next.add(getNextBusforList(getBusList(itinerary.getName(), itinerary.getSentidos().get(j), TypeDay.USEFUL.toString(),false)));
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
