package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.StringUtils;

/**
 * @author Rafael
 * @since 01/03/17
 */
public class ScheduleDAO extends SQLiteOpenHelper {

    private static final String TAG = "ScheduleDAO";
    public static final int SCHEDULE_VERSION = 1;
    private static final int DATABASE_VERSION = 1;

    private String country;
    private String city;

    public ScheduleDAO(Context context,String country,String cityName){
        super(context, StringUtils.getNameDatabase(country,cityName, SCHEDULE_VERSION),null, DATABASE_VERSION);
        this.city = cityName;
        this.country = country;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Company> getCompanies(){
        return ScheduleHelper.getCompanies(getReadableDatabase());
    }

    public List<Itinerary> getItineraries(){
        return ScheduleHelper.getItineraries(getReadableDatabase(),country,city);
    }

    public List<Itinerary> getItineraries(String company){
        return ScheduleHelper.getItineraries(getReadableDatabase(),country,city,company);
    }

    public List<Code> getCodes(String company){
        return ScheduleHelper.getCodes(getReadableDatabase(),country,city,company);
    }

    public List<Bus> getBuses(String company, String itinerary, String way, String typeday) {
        return ScheduleHelper.getBuses(getReadableDatabase(),country,city,company,itinerary,way,typeday);
    }

    public Company getCompany(String company) {
        return ScheduleHelper.getCompany(getReadableDatabase(),company);
    }

    public Code getCode(String company,String code){
        //TODO fazer try em todos
        try{
            return ScheduleHelper.getCode(getReadableDatabase(),country,city,company,code);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    //SEARCH
    public List<Itinerary> getSearchableItineraries(String searchName){
        return ScheduleHelper.getSearchableItineraries(getReadableDatabase(),searchName);
    }


}
