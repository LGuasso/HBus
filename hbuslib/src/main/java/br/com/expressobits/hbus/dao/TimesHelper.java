package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * Created by rafael on 08/12/15.
 */
public class TimesHelper {
    private static final String TAG = "TimesHelper";
    protected static final String DATABASE_NAME = "bus_data.db";
    protected static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";

    protected static final String SQL_CREATE_CITIES =
            "CREATE TABLE " + CityContract.City.TABLE_NAME + " (" +
                    CityContract.City._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_POSITION + TEXT_TYPE +
                    " )";
    protected static final String SQL_CREATE_ITINERARIES =
            "CREATE TABLE " + ItineraryContract.Itinerary.TABLE_NAME + " (" +
                    ItineraryContract.Itinerary._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                    ItineraryContract.Itinerary.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ItineraryContract.Itinerary.COLUMN_NAME_WAYS + TEXT_TYPE + COMMA_SEP +
                    ItineraryContract.Itinerary.COLUMN_NAME_CITY_ID + INTEGER_TYPE +
                    " )";
    protected static final String SQL_CREATE_CODES =
            "CREATE TABLE " + CodeContract.Code.TABLE_NAME + " (" +
                    CodeContract.Code._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_CITY_ID + INTEGER_TYPE +
                    " )";
    protected static final String SQL_CREATE_BUSES =
            "CREATE TABLE " + BusContract.Bus.TABLE_NAME + " (" +
                    BusContract.Bus._ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                    BusContract.Bus.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    BusContract.Bus.COLUMN_NAME_WAY + TEXT_TYPE + COMMA_SEP +
                    BusContract.Bus.COLUMN_NAME_TYPEDAY + TEXT_TYPE + COMMA_SEP +
                    BusContract.Bus.COLUMN_NAME_CITY_ID + INTEGER_TYPE + COMMA_SEP +
                    BusContract.Bus.COLUMN_NAME_ITINERARY_ID + INTEGER_TYPE + COMMA_SEP +
                    BusContract.Bus.COLUMN_NAME_CODE_ID + INTEGER_TYPE +
                    " )";

    public static final String NOT_FOUND_TIME = " --:-- ";

    //CONTENT VALUES
    protected static ContentValues toContentValues(City city){
        ContentValues values = new ContentValues();
        values.put(CityContract.City._ID,city.getId());
        values.put(CityContract.City.COLUMN_NAME_NAME,city.getName());
        values.put(CityContract.City.COLUMN_NAME_COUNTRY,city.getCountry());
        values.put(CityContract.City.COLUMN_NAME_POSITION, city.getPosition());
        return values;
    }

    protected static ContentValues toContentValues(Itinerary itinerary){
        ContentValues values = new ContentValues();
        values.put(ItineraryContract.Itinerary._ID,itinerary.getId());
        values.put(ItineraryContract.Itinerary.COLUMN_NAME_NAME,itinerary.getName());
        values.put(ItineraryContract.Itinerary.COLUMN_NAME_WAYS, TextUtils.getSentidosinString(itinerary.getWays()));
        values.put(ItineraryContract.Itinerary.COLUMN_NAME_CITY_ID,itinerary.getCityid());
        return values;
    }

    protected static ContentValues toContentValues(Code code){
        ContentValues values = new ContentValues();
        values.put(CodeContract.Code._ID,code.getId());
        values.put(CodeContract.Code.COLUMN_NAME_NAME,code.getName());
        values.put(CodeContract.Code.COLUMN_NAME_DESCRIPTION,code.getDescrition());
        values.put(CodeContract.Code.COLUMN_NAME_CITY_ID,code.getCityid());
        return values;
    }

    protected static ContentValues toContentValues(Bus bus){
        ContentValues values = new ContentValues();
        values.put(BusContract.Bus._ID,bus.getId());
        values.put(BusContract.Bus.COLUMN_NAME_TIME,bus.getTime());
        values.put(BusContract.Bus.COLUMN_NAME_CODE_ID,bus.getCodeId());
        values.put(BusContract.Bus.COLUMN_NAME_TYPEDAY,bus.getTypeday().toString());
        values.put(BusContract.Bus.COLUMN_NAME_WAY,bus.getWay());
        values.put(BusContract.Bus.COLUMN_NAME_CITY_ID,bus.getCityid());
        values.put(BusContract.Bus.COLUMN_NAME_ITINERARY_ID,bus.getItineraryId());
        return values;
    }

    //<b>INSERTS</b>
    //////////////////////
    public static long insert(SQLiteDatabase db,City city){
        // Gets the data repository in write mode
        long newRowId;
        newRowId = db.insert(
                CityContract.City.TABLE_NAME,
                null,
                toContentValues(city));
        return newRowId;
    }

    public static long insert(SQLiteDatabase db,Itinerary itinerary){
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ItineraryContract.Itinerary.TABLE_NAME,
                null,
                toContentValues(itinerary));
        return newRowId;
    }

    public static long insert(SQLiteDatabase db,Code code){
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                CodeContract.Code.TABLE_NAME,
                null,
                toContentValues(code));
        return newRowId;
    }

    public static long insert(SQLiteDatabase db,Bus bus){
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                BusContract.Bus.TABLE_NAME,
                null,
                toContentValues(bus));
        return newRowId;
    }

    //CURSORTOOBJET
    protected static City cursorToCity(Cursor c){
        City city = new City();
        city.setId(c.getLong(c.getColumnIndexOrThrow(CityContract.City._ID)));
        city.setName(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_NAME)));
        city.setCountry(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_COUNTRY)));
        city.setPosition(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_POSITION)));
        return city;
    }

    protected static Itinerary cursorToItinerary(Cursor c){
        Itinerary itinerary = new Itinerary();
        itinerary.setId(c.getLong(c.getColumnIndexOrThrow(ItineraryContract.Itinerary._ID)));
        itinerary.setName(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_NAME)));
        itinerary.setWays(new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_WAYS)).split(COMMA_SEP))));
        itinerary.setCityid(c.getLong(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_CITY_ID)));
        return itinerary;
    }

    protected static Code cursorToCode(Cursor c){
        Code code = new Code();
        code.setId(c.getLong(c.getColumnIndexOrThrow(CodeContract.Code._ID)));
        code.setName(c.getString(c.getColumnIndexOrThrow(CodeContract.Code.COLUMN_NAME_NAME)));
        code.setDescrition(c.getString(c.getColumnIndexOrThrow(CodeContract.Code.COLUMN_NAME_DESCRIPTION)));
        code.setCityid(c.getLong(c.getColumnIndexOrThrow(CodeContract.Code.COLUMN_NAME_CITY_ID)));
        return code;
    }

    protected static Bus cursorToBus(Cursor c){
        Bus bus = new Bus();
        bus.setId(c.getLong(c.getColumnIndexOrThrow(BusContract.Bus._ID)));
        bus.setTime(c.getString(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_TIME)));
        bus.setCodeId(c.getLong(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_CODE_ID)));
        bus.setCityid(c.getLong(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_CITY_ID)));
        bus.setItineraryId(c.getLong(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_ITINERARY_ID)));
        bus.setWay(c.getString(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_WAY)));
        bus.setTypeday(HoursUtils.getTypeDayforString(c.getString(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_TYPEDAY))));
        return bus;
    }

    //GETTER
    public static City getCity(SQLiteDatabase db,Long id){
        String where = CityContract.City._ID+" = ?";
        String arguments[] = {id.toString()};
        Cursor cursor = db.query(
                CityContract.City.TABLE_NAME,
                CityContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToCity(cursor);
        }
        return null;
    }

    public static Itinerary getItinerary(SQLiteDatabase db,Long id){
        String where = ItineraryContract.Itinerary._ID+" = ?";
        String arguments[] = {id.toString()};
        Cursor cursor = db.query(
                ItineraryContract.Itinerary.TABLE_NAME,
                ItineraryContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToItinerary(cursor);
        }
        return null;
    }

    public static Code getCode(SQLiteDatabase db,Long id){
        String where = CodeContract.Code._ID+" = ?";
        String arguments[] = {id.toString()};
        Cursor cursor = db.query(
                CodeContract.Code.TABLE_NAME,
                CodeContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToCode(cursor);
        }
        return null;
    }

    public static Bus getBus(SQLiteDatabase db,Long id){
        String where = BusContract.Bus._ID+" = ?";
        String arguments[] = {id.toString()};
        Cursor cursor = db.query(
                BusContract.Bus.TABLE_NAME,
                BusContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToBus(cursor);
        }
        return null;
    }

    public static City getCity(SQLiteDatabase db,String name,String country){
        String where = CityContract.City.COLUMN_NAME_NAME+" = ? AND "+
                CityContract.City.COLUMN_NAME_COUNTRY+" = ?";
        String arguments[] = {name,country};
        Cursor cursor = db.query(
                CityContract.City.TABLE_NAME,
                CityContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToCity(cursor);
        }
        return null;
    }

    public static Code getCode(SQLiteDatabase db,String name,Long cityId){
        String where = CodeContract.Code.COLUMN_NAME_NAME+" = ? AND "+
                CodeContract.Code.COLUMN_NAME_CITY_ID+" = ?";
        String arguments[] = {name,cityId.toString()};
        Cursor cursor = db.query(
                CodeContract.Code.TABLE_NAME,
                CodeContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToCode(cursor);
        }
        return null;
    }


    //GET LIST OBJECT
    public static List<City> getCities(SQLiteDatabase db){
        ArrayList<City> cities = new ArrayList<City>();
        Cursor c;
        c = db.query(
                CityContract.City.TABLE_NAME,
                CityContract.COLS,
                null,
                null,
                null,
                null,
                null);
        while(c.moveToNext()){
            cities.add(cursorToCity(c));
        }
        c.close();
        return cities;
    }


    public static List<Itinerary> getItineraries(SQLiteDatabase db){
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        Cursor c;
        c = db.query(
                ItineraryContract.Itinerary.TABLE_NAME,
                ItineraryContract.COLS,
                null,
                null,
                null,
                null,
                null);
        while(c.moveToNext()){
            itineraries.add(cursorToItinerary(c));
        }
        c.close();
        return itineraries;
    }

    public static List<Code> getCodes(SQLiteDatabase db){
        ArrayList<Code> codes = new ArrayList<Code>();
        Cursor c;
        c = db.query(
                CodeContract.Code.TABLE_NAME,
                CodeContract.COLS,
                null,
                null,
                null,
                null,
                null);
        while(c.moveToNext()){
            codes.add(cursorToCode(c));
        }
        c.close();
        return codes;
    }

    public static List<Bus> getBuses(SQLiteDatabase db){
        ArrayList<Bus> buses = new ArrayList<Bus>();
        Cursor c;
        c = db.query(
                BusContract.Bus.TABLE_NAME,
                BusContract.COLS,
                null,
                null,
                null,
                null,
                null);
        while(c.moveToNext()){
            buses.add(cursorToBus(c));
        }
        Log.e(TAG,"BUSES "+buses.size());
        c.close();
        return buses;
    }

    public static List<Itinerary> getItineraries(SQLiteDatabase db,Long cityId){
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        String where = ItineraryContract.Itinerary.COLUMN_NAME_CITY_ID+" = ?";
        String arguments[] = {cityId.toString()};
        Cursor c;
        c = db.query(
                ItineraryContract.Itinerary.TABLE_NAME,
                ItineraryContract.COLS,
                where,
                arguments,
                null,
                null,
                null);
        while(c.moveToNext()){
            itineraries.add(cursorToItinerary(c));
        }
        c.close();
        return itineraries;
    }

    public static List<Bus> getBuses(SQLiteDatabase db,Long itineraryId,String way,String typeday){
        ArrayList<Bus> buses = new ArrayList<Bus>();
        //way = TextUtils.toSimpleNameWay(way);
        Cursor c;
        String where = BusContract.Bus.COLUMN_NAME_ITINERARY_ID+" = ? AND "+
                BusContract.Bus.COLUMN_NAME_WAY+" = ? AND "+
                BusContract.Bus.COLUMN_NAME_TYPEDAY+" = ?";
        String arguments[] = {String.valueOf(itineraryId),way,typeday};
        c = db.query(
                BusContract.Bus.TABLE_NAME,
                BusContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        while (c.moveToNext()){
            buses.add(cursorToBus(c));
        }
        c.close();
        return buses;
    }

    public static List<Bus> getNextBuses(SQLiteDatabase db,Itinerary itinerary){

        ArrayList<Bus> next = new ArrayList<Bus>();
        for(int j = 0;j< itinerary.getWays().size();j++) {
            next.add(getNextBusforList(
                    getBuses(
                            db,
                            itinerary.getId(),
                            itinerary.getWays().get(j),
                            HoursUtils.getStringTipoDeDia(Calendar.getInstance()).toString())));
        }
        return next;
    }

    private static Bus getNextBusforList(List<Bus> buses){
        //TODO Create metodo separado
        Bus nowBus = new Bus();
        Bus nextBus;
        nowBus.setTime(HoursUtils.getNowTimeinString());
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
            nowBus.setTime(NOT_FOUND_TIME);
            return nowBus;
        }
    }

    //CREATE TABLES
    public static void createTableCities(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_CITIES);
    }

    public static void createTableItineraries(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ITINERARIES);
    }

    public static void createTableCodes(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_CODES);
    }

    public static void createTableBuses(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_BUSES);
    }

}
