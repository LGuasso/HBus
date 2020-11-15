package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 *  Class that determines database for {@link br.com.expressobits.hbus.model.Itinerary} favorites,
 * Here and stored in the same way as data, but only the class Itinerary
 * @author Rafael Correa
 * @since 28/11/15.
 */
public class BookmarkItineraryDAO extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "itineraries_favorites.db";
    public BookmarkItineraryDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItineraryContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Itinerary itinerary){
        BusHelper.insert(getWritableDatabase(),itinerary);
    }

    public void removeFavorite(Itinerary itinerary){
        getWritableDatabase().delete(ItineraryContract.Itinerary.TABLE_NAME, ItineraryContract.Itinerary._ID + " = ?", new String[]{itinerary.getId()});
    }

    public List<Itinerary> getItineraries(String cityId){
        City city = new City();
        city.setCountry(FirebaseUtils.getCountry(cityId));
        city.setName(FirebaseUtils.getCityName(cityId));
        return BusHelper.getItineraries(getReadableDatabase(),city);
    }

    public Itinerary getItinerary(String itineraryId) {
        return BusHelper.getItinerary(getReadableDatabase(),itineraryId);
    }

}
