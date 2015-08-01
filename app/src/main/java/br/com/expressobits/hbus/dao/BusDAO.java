package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.Line;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael Correa
 * @since 31/07/2015
 */
public class BusDAO extends SQLiteOpenHelper{


    private static final String[] COLS_BUS = {"id","time","code","line","way","typeday"};
    private static final String[] COLS_CODE = {"id","code","descrition"};
    private static final String[] COLS_ITINERARY = {"id","name","favorite"};
    private static final String TABLE_BUS = "Bus";
    private static final String TABLE_CODE = "Code";
    private static final String TABLE_ITINERARY = "Itinerary";
    private static final String DATABASE_NAME = "HBusDB";
    private static final int DATABASE_VERSION = 1;

    public BusDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Cria banco de dados no armazenamento externo
        //super(context, Environment.getExternalStorageDirectory().getAbsolutePath()
                //+ "/HBus/database/" + DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableItinerary(db);
        createTableCode(db);
        createTableBus(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS "+ TABLE_ITINERARY);
        db.execSQL(sb.toString());

        sb.append("DROP TABLE IF EXISTS "+ TABLE_CODE);
        db.execSQL(sb.toString());

        sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS " + TABLE_BUS);
        db.execSQL(sb.toString());

        onCreate(db);
    }

    private void createTableItinerary(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_ITINERARY + " ");
        sb.append("(id INTEGER PRIMARY KEY, ");
        sb.append(" name TEXT,");
        sb.append(" favorite INTEGER);");
        db.execSQL(sb.toString());
    }

    private void createTableBus(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_BUS + " ");
        sb.append("(id INTEGER PRIMARY KEY, ");
        sb.append(" time TEXT,");
        sb.append(" code TEXT,");
        sb.append(" line TEXT,");
        sb.append(" way TEXT,");
        sb.append(" typeday TEXT);");
        db.execSQL(sb.toString());
    }

    private void createTableCode(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_CODE + " ");
        sb.append("(id INTEGER PRIMARY KEY, ");
        sb.append(" code TEXT,");
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
        getWritableDatabase().insert(TABLE_ITINERARY, null, c);
    }


    /**
     * Insere novo codigo de �nibus no banco de dados
     * @param code
     */
    public void insert(Code code){
        ContentValues c = new ContentValues();
        c.put("code", code.getCode());
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
        c.put("code", bus.getCode().getCode());
        c.put("itinerary", bus.getItinerary().getName());
        c.put("way", bus.getWay());
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

        c = getWritableDatabase().query(TABLE_ITINERARY,COLS_ITINERARY,null,null,null,null,null);
        while(c.moveToNext()){
            Itinerary itinerary = new Itinerary();
            itinerary.setName(c.getString(1));
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
        if(favorite){
            String arguments[] = {"1"};
        }else{
            String arguments[] = {"0"};
        }


        c = getWritableDatabase().query(TABLE_ITINERARY,COLS_ITINERARY,null,null,null,null,null);
        while(c.moveToNext()){
            Itinerary itinerary = new Itinerary();
            itinerary.setName(c.getString(1));
            itinerary.setFavorite(favorite);
            itinerariess.add(itinerary);

        }
        c.close();
        return itinerariess;
    }


    public void delete(Bus bus){
        //TODO implement app manager of times
        //getWritableDatabase().delete(TABLE, "name=?", new String[]{linha});
    }

    public Code getListaCode(String codeName){
        //ArrayList<Code> codes = new ArrayList<Code>();
        Code code = new Code();
        Cursor c;
        String where = "code = ?";
        String arguments[] = {codeName};

        c = getWritableDatabase().query(TABLE_ITINERARY,COLS_ITINERARY,where,arguments,null,null,null);
        while(c.moveToNext()){
            code = new Code();
            code.setCode(c.getString(1));
            code.setDescrition(c.getString(2));
            //codes.add(code);
        }
        c.close();

        return code;
    }


    public Itinerary getItinerary(String itineraryName) {
        //ArrayList<Code> codes = new ArrayList<Code>();
        Itinerary itinerary = new Itinerary();
        Cursor c;
        String where = "name = ?";
        String arguments[] = {itineraryName};

        c = getWritableDatabase().query(TABLE_ITINERARY,COLS_ITINERARY, where, arguments, null, null, null);
        while (c.moveToNext()) {
            itinerary = new Itinerary();
            itinerary.setName(c.getString(1));
            //codes.add(code);
        }
        c.close();
        return itinerary;
    }



        /**
         * Busca no banco de dados lista de {@link Bus} baseado nos par�metros
         * @param nameLine Nome da linha
         * @param way Sentido do �nibus
         * @param typeDay tipo de dia {@link br.com.expressobits.hbus.model.TypeDay}
         * @return Lista de {@link Bus}
         */
    public List<Bus> getLista(String nameLine,String way,String typeDay){
        ArrayList<Bus> buses = new ArrayList<Bus>();

        Cursor c;
        String where = "line = ? AND way = ? AND typeday = ?";
        String arguments[] = {nameLine,way,typeDay};

        c = getWritableDatabase().query(TABLE_BUS,COLS_BUS,where,arguments,null,null,null);
        while(c.moveToNext()){
            Bus bus = new Bus();
            bus.setTime(c.getString(1));

            Code code = new Code();
            code.setCode(c.getString(2));
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
}
