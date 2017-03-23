package br.com.expressobits.hbus.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.provider.BusContract;
import br.com.expressobits.hbus.provider.CityContract;
import br.com.expressobits.hbus.provider.CodeContract;
import br.com.expressobits.hbus.utils.BusUtils;
import br.com.expressobits.hbus.utils.TimeUtils;
import br.com.expressobits.hbus.utils.StringUtils;
import static br.com.expressobits.hbus.dao.SQLConstants.*;

/**
 * @author Rafael
 * @since 19/01/16
 */
@SuppressWarnings("ALL")
@SuppressLint("ALL")
@Deprecated
class BusHelper {

    private static final String SQL_CREATE_CITIES =
            "CREATE TABLE " + CityContract.City.TABLE_NAME + " (" +
                    CityContract.City._ID + TEXT_PRIMARY_KEY + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_LONGITUDE + REAL_TYPE +
                    PARENTES;




    //CONTENT VALUES
    private static ContentValues toContentValues(City city){
        ContentValues values = new ContentValues();
        values.put(CityContract.City._ID,city.getId());
        values.put(CityContract.City.COLUMN_NAME_NAME,city.getName());
        values.put(CityContract.City.COLUMN_NAME_COUNTRY,city.getCountry());
        values.put(CityContract.City.COLUMN_NAME_LATITUDE,city.getLocalization().get(CityContract.City.COLUMN_NAME_LATITUDE));
        values.put(CityContract.City.COLUMN_NAME_LONGITUDE,city.getLocalization().get(CityContract.City.COLUMN_NAME_LONGITUDE));
        return values;
    }

    private static ContentValues toContentValues(Itinerary itinerary){
        ContentValues values = new ContentValues();
        values.put(ItineraryContract.Itinerary._ID,itinerary.getId());
        values.put(ItineraryContract.Itinerary.COLUMN_NAME_NAME,itinerary.getName());
        values.put(ItineraryContract.Itinerary.COLUMN_NAME_WAYS, StringUtils.getSentidosinString(itinerary.getWays()));
        return values;
    }

    private static ContentValues toContentValues(Code code){
        ContentValues values = new ContentValues();
        values.put(CodeContract.Code._ID, code.getId());
        values.put(CodeContract.Code.COLUMN_NAME_NAME, code.getName());
        values.put(CodeContract.Code.COLUMN_NAME_DESCRIPTION, code.getDescrition());
        return values;
    }

    private static ContentValues toContentValues(Bus bus){
        ContentValues values = new ContentValues();
        values.put(BusContract.Bus._ID,bus.getId());
        values.put(BusContract.Bus.COLUMN_NAME_TIME,bus.getTime());
        values.put(BusContract.Bus.COLUMN_NAME_CODE, bus.getCode());
        return values;
    }

    public static void insert(SQLiteDatabase db,City city){
        db.insert(
                CityContract.City.TABLE_NAME,
                null,
                toContentValues(city));
    }

    public static void insert(SQLiteDatabase db,Itinerary itinerary){
        db.insert(
                ItineraryContract.Itinerary.TABLE_NAME,
                null,
                toContentValues(itinerary));
    }

    public static void insert(SQLiteDatabase db,Code code){
        db.insert(
                CodeContract.Code.TABLE_NAME,
                null,
                toContentValues(code));
    }

    public static void insert(SQLiteDatabase db,Bus bus){
        db.insert(
                BusContract.Bus.TABLE_NAME,
                null,
                toContentValues(bus));
    }

    public static void update(SQLiteDatabase db,City city){
        db.update(
                CityContract.City.TABLE_NAME,
                toContentValues(city),
                CityContract.City._ID+" = ?",
                new String[]{city.getId()});
    }

    public static void update(SQLiteDatabase db,Itinerary itinerary){
        db.update(
                ItineraryContract.Itinerary.TABLE_NAME,
                toContentValues(itinerary),
                ItineraryContract.Itinerary._ID+" = ?",
                new String[]{itinerary.getId()});
    }

    public static void update(SQLiteDatabase db,Code code){
        db.update(
                CodeContract.Code.TABLE_NAME,
                toContentValues(code),
                CodeContract.Code._ID+" = ?",
                new String[]{code.getId()});
    }

    public static void update(SQLiteDatabase db,Bus bus){
        db.update(
                BusContract.Bus.TABLE_NAME,
                toContentValues(bus),
                BusContract.Bus._ID+" = ?",
                new String[]{bus.getId()});
    }

    protected static City cursorToCity(Cursor c){
        City city = new City();
        city.setId(c.getString(c.getColumnIndexOrThrow(CityContract.City._ID)));
        city.setName(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_NAME)));
        city.setCountry(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_COUNTRY)));
        Double latitude = c.getDouble(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_LATITUDE));
        Double longitude = c.getDouble(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_LONGITUDE));
        Map<String,Double> localization = new HashMap<>();
        localization.put(CityContract.City.COLUMN_NAME_LATITUDE,latitude);
        localization.put(CityContract.City.COLUMN_NAME_LONGITUDE,longitude);
        city.setLocalization(localization);
        return city;
    }

    protected static Itinerary cursorToItinerary(Cursor c){
        Itinerary itinerary = new Itinerary();
        itinerary.setId(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary._ID)));
        itinerary.setName(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_NAME)));
        itinerary.setWays(new ArrayList<>(Arrays.asList(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_WAYS)).split(COMMA_SEP))));
        return itinerary;
    }

    protected static Code cursorToCode(Cursor c){
        Code code = new Code();
        code.setId(c.getString(c.getColumnIndexOrThrow(CodeContract.Code._ID)));
        code.setName(c.getString(c.getColumnIndexOrThrow(CodeContract.Code.COLUMN_NAME_NAME)));
        code.setDescrition(c.getString(c.getColumnIndexOrThrow(CodeContract.Code.COLUMN_NAME_DESCRIPTION)));
        return code;
    }

    protected static Bus cursorToBus(Cursor c){
        Bus bus = new Bus();
        bus.setId(c.getString(c.getColumnIndexOrThrow(BusContract.Bus._ID)));
        bus.setTime(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_TIME));
        bus.setCode(c.getString(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_CODE)));
        return bus;
    }

    public static City getCity(SQLiteDatabase db,String id){
        String where = CityContract.City._ID+" = ?";
        String arguments[] = {id};
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

    public static Itinerary getItinerary(SQLiteDatabase db,String id){
        String where = ItineraryContract.Itinerary._ID+" = ?";
        String arguments[] = {id};
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

    public static Code getCode(SQLiteDatabase db,String id){
        String where = CodeContract.Code._ID+" = ?";
        String arguments[] = {id};
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

    public static Code getCode(SQLiteDatabase db,City city,String codeName){
        String where = CodeContract.Code._ID+" = ?";
        String arguments[] = {city.getCountry()+BARS+city.getName()+BARS+codeName};
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

    public static Code getCode(SQLiteDatabase db,String name,Long cityId){
        String where = CodeContract.Code.COLUMN_NAME_NAME+" = ? AND "+
                CodeContract.Code.COLUMN_NAME_DESCRIPTION+" = ?";
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

    public static Bus getBus(SQLiteDatabase db,String id){
        String where = BusContract.Bus._ID+" = ?";
        String arguments[] = {id};
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

    //GET LIST OBJCTS
    public static List<City> getCities(SQLiteDatabase db){
        ArrayList<City> cities = new ArrayList<>();
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
        ArrayList<Itinerary> itineraries = new ArrayList<>();
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

    public static List<Itinerary> getItineraries(SQLiteDatabase db,City city){
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        String where = ItineraryContract.Itinerary._ID+" LIKE ?";
        String arguments[] = {BARS+city.getCountry()+BARS+city.getName()+"%"};
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

    public static List<Code> getCodes(SQLiteDatabase db,City city){
        ArrayList<Code> codes = new ArrayList<>();
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

    public static List<Bus> getBuses(SQLiteDatabase db,City city,Itinerary itinerary,String way,TypeDay typeDay){
        ArrayList<Bus> buses = new ArrayList<>();
        Cursor c;
        String where = BusContract.Bus._ID+" LIKE ?";
        Log.e("TEST",city.getCountry()+BARS+city.getName()+BARS+
                itinerary.getName()+BARS+way+BARS+typeDay.toString()+"%");
        String arguments[] = {city.getCountry()+BARS+city.getName()+BARS+
                itinerary.getName()+BARS+way+BARS+typeDay.toString()+"%"};
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

    public static List<Bus> getBuses(SQLiteDatabase db,City city,Itinerary itinerary){
        ArrayList<Bus> buses = new ArrayList<>();
        Cursor c;
        String where = BusContract.Bus._ID+" LIKE ?";
        Log.e("TEST",city.getCountry()+BARS+city.getName()+BARS+
                itinerary.getName()+"%");
        String arguments[] = {city.getCountry()+BARS+city.getName()+BARS+
                itinerary.getName()+"%"};
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

    ///DELETES

    public static int deleteItineraries(SQLiteDatabase db,City city){
        String where = ItineraryContract.Itinerary._ID+" LIKE ?";
        String arguments[] = {city.getCountry()+BARS+city.getName()+"%"};
        return db.delete(
                ItineraryContract.Itinerary.TABLE_NAME,
                where,
                arguments
        );
    }

    public static int deleteCodes(SQLiteDatabase db,City city){
        String where = CodeContract.Code._ID+" LIKE ?";
        String arguments[] = {city.getCountry()+BARS+city.getName()+"%"};
        return db.delete(
                CodeContract.Code.TABLE_NAME,
                where,
                arguments
        );
    }


    public static int deleteBuses(SQLiteDatabase db,City city,Itinerary itinerary){
        String where = BusContract.Bus._ID+" LIKE ?";
        String arguments[] = {city.getCountry()+BARS+city.getName()+BARS+
                itinerary.getName()+"%"};
        return db.delete(
                    BusContract.Bus.TABLE_NAME,
                    where,
                    arguments
            );
    }

    public static List<Bus> getNextBuses(SQLiteDatabase db,City city,Itinerary itinerary){

        ArrayList<Bus> next = new ArrayList<>();
        if(itinerary.getWays()!=null){
            for(int j = 0;j< itinerary.getWays().size();j++) {
                next.add(BusUtils.getNextBusforList(
                        getBuses(db,
                                city,
                                itinerary,
                                itinerary.getWays().get(j),
                                TimeUtils.getTypedayinCalendar(Calendar.getInstance()))));
            }
        }

        return next;
    }


    //
    //
    //
    //CREATE TABLES
    public static void createTableCities(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_CITIES);
    }

    public static void createTableItineraries(SQLiteDatabase db){
        db.execSQL(ItineraryContract.SQL_CREATE_TABLE);
    }

    public static void createTableCodes(SQLiteDatabase db){
        db.execSQL(CodeContract.SQL_CREATE_TABLE);
    }
    public static void createTableBuses(SQLiteDatabase db){
        db.execSQL(BusContract.SQL_CREATE_TABLE);
    }


    //DELETER ALL DATA
    public static void deleteAllcities(SQLiteDatabase db){
        db.execSQL(CityContract.SQL_DELETE_ALL);
    }

    public static void deleteAllItineraries(SQLiteDatabase db){
        db.execSQL(ItineraryContract.SQL_DELETE_ALL);
    }

    public static void deleteAllCodes(SQLiteDatabase db){
        db.execSQL(CodeContract.SQL_DELETE_ALL);
    }

    public static void deleteAllBuses(SQLiteDatabase db){
        db.execSQL(BusContract.SQL_DELETE_ALL);
    }


}
