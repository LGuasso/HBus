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
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.BusUtils;
import br.com.expressobits.hbus.utils.StringUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

import static br.com.expressobits.hbus.dao.SQLConstants.BARS;
import static br.com.expressobits.hbus.dao.SQLConstants.COMMA_SEP;

/**
 * @author Rafael
 * @since 19/01/16
 */
class ScheduleHelper {

    private static ContentValues toContentValues(Company company){
        ContentValues values = new ContentValues();
        values.put(CompanyContract.Company._ID,company.getId());
        values.put(CompanyContract.Company.COLUMN_NAME_NAME,company.getName());
        values.put(CompanyContract.Company.COLUMN_NAME_EMAIL,company.getEmail());
        values.put(CompanyContract.Company.COLUMN_NAME_PHONENUMBER,company.getPhoneNumber());
        values.put(CompanyContract.Company.COLUMN_NAME_WEBSITE,company.getWebsite());
        values.put(CompanyContract.Company.COLUMN_NAME_ADDRESS,company.getAddress());
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

    //INSERTS
    /*
    public static void insert(SQLiteDatabase db,Company company){
        db.insert(
                CompanyContract.Company.TABLE_NAME,
                null,
                toContentValues(company));
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
    */
    //UPDATES
    /*
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
    */

    //CURSOR TO OBJECT

    protected static Company cursorToCompany(Cursor c){
        Company company = new Company();
        company.setId(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company._ID)));
        company.setName(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_NAME)));
        company.setEmail(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_EMAIL)));
        company.setWebsite(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_WEBSITE)));
        company.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_PHONENUMBER)));
        company.setAddress(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_ADDRESS)));
        return company;
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
        bus.setTime(c.getLong(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_TIME)));
        bus.setCode(c.getString(c.getColumnIndexOrThrow(BusContract.Bus.COLUMN_NAME_CODE)));
        return bus;
    }

    //GET UNIQUE DATA

    public static Company getCompany(SQLiteDatabase db,String country,String city,String company){
        String where = CompanyContract.Company._ID+" = ?";
        String arguments[] = {BARS+country+BARS+city+BARS+company};
        Cursor cursor = db.query(
                CompanyContract.Company.TABLE_NAME,
                CompanyContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToCompany(cursor);
        }
        return null;
    }

    public static Itinerary getItinerary(SQLiteDatabase db,String country,String city,String company,String itinerary){
        String where = ItineraryContract.Itinerary._ID+" = ?";
        String arguments[] = {BARS+country+BARS+city+BARS+company+BARS+itinerary};
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

    public static Code getCode(SQLiteDatabase db,String country,String city,String company,String codeName){
        String where = CodeContract.Code._ID+" = ?";
        String arguments[] = {BARS+country+BARS+city+BARS+company+BARS+codeName};
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

    public static List<Company> getCompanies(SQLiteDatabase db){
        ArrayList<Company> companies = new ArrayList<>();
        Cursor c;
        c = db.query(
                CompanyContract.Company.TABLE_NAME,
                CompanyContract.COLS,
                null,
                null,
                null,
                null,
                null);
        while(c.moveToNext()){
            companies.add(cursorToCompany(c));
        }
        c.close();
        return companies;
    }

    public static List<Itinerary> getSearchableItineraries(SQLiteDatabase db,String searchName){
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        String where = ItineraryContract.Itinerary.COLUMN_NAME_NAME+" LIKE ?";
        String arguments[] = {searchName};
        String orderBy = ItineraryContract.Itinerary.COLUMN_NAME_NAME+" COLLATE NOCASE ASC";
        Cursor c;
        c = db.query(
                ItineraryContract.Itinerary.TABLE_NAME,
                ItineraryContract.COLS,
                where,
                arguments,
                null,
                null,
                orderBy);
        while(c.moveToNext()){
            itineraries.add(cursorToItinerary(c));
        }
        c.close();
        return itineraries;
    }

    public static List<Itinerary> getItineraries(SQLiteDatabase db,String country,String city,String company){
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        String where = ItineraryContract.Itinerary._ID+" LIKE ?";
        String arguments[] = {BARS+country+BARS+city+BARS+company+"%"};
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

    public static List<Itinerary> getItineraries(SQLiteDatabase db,String country,String city){
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        String where = ItineraryContract.Itinerary._ID+" LIKE ?";
        String arguments[] = {BARS+country+BARS+city+BARS+"%"};
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

    public static List<Code> getCodes(SQLiteDatabase db,String country,String city,String company){
        ArrayList<Code> codes = new ArrayList<>();
        Cursor c;
        String where = CodeContract.Code._ID+" LIKE ?";
        String arguments[] = {BARS+country+BARS+city+BARS+company+"%"};
        c = db.query(
                CodeContract.Code.TABLE_NAME,
                CodeContract.COLS,
                where,
                arguments,
                null,
                null,
                null);
        while(c.moveToNext()){
            codes.add(cursorToCode(c));
        }
        c.close();
        return codes;
    }

    public static List<Bus> getBuses(SQLiteDatabase db,String country,String city,String company,String itinerary,String way,String typeDay){
        ArrayList<Bus> buses = new ArrayList<>();
        Cursor c;
        String where = BusContract.Bus._ID+" LIKE ?";
        String arguments[] = {BARS+country+BARS+city+BARS+company+BARS+
                itinerary+BARS+way+BARS+typeDay+"%"};
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
            Bus bus = cursorToBus(c);
            bus.setTypeday(typeDay);
            bus.setWay(way);
            buses.add(bus);
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

    public static List<Bus> getNextBuses(SQLiteDatabase db, String country, String city,String company,Itinerary itinerary){

        ArrayList<Bus> next = new ArrayList<>();
        if(itinerary.getWays()!=null){
            for(int j = 0;j< itinerary.getWays().size();j++) {
                next.add(BusUtils.getNextBusforList(
                        getBuses(db,
                                country,
                                city,
                                company,
                                itinerary.getName(),
                                itinerary.getWays().get(j),
                                TimeUtils.getTypedayinCalendar(Calendar.getInstance()).toString())));
            }
        }

        return next;
    }

}
