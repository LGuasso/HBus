package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * @author Rafael
 * @since 19/01/16
 */
public class BusHelper {
    private static final String TAG = "TimesHelper";
    protected static final String DATABASE_NAME = "bus_database.db";
    protected static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";
    public static final String TEXT_PRIMARY_KEY = " TEXT PRIMARY KEY";


    protected static final String SQL_CREATE_CITIES =
            "CREATE TABLE " + CityContract.City.TABLE_NAME + " (" +
                    CityContract.City._ID + TEXT_PRIMARY_KEY + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                    CityContract.City.COLUMN_NAME_POSITION + TEXT_TYPE +
                    " )";

    protected static final String SQL_CREATE_ITINERARIES =
            "CREATE TABLE " + ItineraryContract.Itinerary.TABLE_NAME + " (" +
                    ItineraryContract.Itinerary._ID + TEXT_PRIMARY_KEY + COMMA_SEP +
                    ItineraryContract.Itinerary.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ItineraryContract.Itinerary.COLUMN_NAME_WAYS + TEXT_TYPE + COMMA_SEP +
                    ItineraryContract.Itinerary.COLUMN_NAME_CITY_ID + TEXT_TYPE +
                    " )";


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

    protected static City cursorToCity(Cursor c){
        City city = new City();
        city.setId(c.getString(c.getColumnIndexOrThrow(CityContract.City._ID)));
        city.setName(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_NAME)));
        city.setCountry(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_COUNTRY)));
        city.setPosition(c.getString(c.getColumnIndexOrThrow(CityContract.City.COLUMN_NAME_POSITION)));
        return city;
    }

    protected static Itinerary cursorToItinerary(Cursor c){
        Itinerary itinerary = new Itinerary();
        itinerary.setId(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary._ID)));
        itinerary.setName(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_NAME)));
        itinerary.setWays(new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_WAYS)).split(COMMA_SEP))));
        itinerary.setCityid(c.getLong(c.getColumnIndexOrThrow(ItineraryContract.Itinerary.COLUMN_NAME_CITY_ID)));
        return itinerary;
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

    public static List<Itinerary> getItineraries(SQLiteDatabase db,String cityId){
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

}
