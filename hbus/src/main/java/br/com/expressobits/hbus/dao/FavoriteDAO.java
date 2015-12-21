package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * Classe que determina database para {@link br.com.expressobits.hbus.model.Itinerary} favoritos,
 * aqui e armazenado da mesma forma que BusDAO , mas somente a classe Itineray
 * @author Rafael Correa
 * @since 28/11/15.
 */
public class FavoriteDAO extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASENAME = "itineraries_favorites.db";

    public FavoriteDAO(Context context){
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItineraryContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Itinerary itinerary){
        TimesHelper.insert(getWritableDatabase(),itinerary);
    }

    public void removeFavorite(Itinerary itinerary){
        getWritableDatabase().delete(ItineraryContract.Itinerary.TABLE_NAME, ItineraryContract.Itinerary._ID + " = ?", new String[]{itinerary.getId().toString()});
    }

    public List<Itinerary> getItineraries(Long cityId){
        return TimesHelper.getItineraries(getReadableDatabase(),cityId);
    }

    public Itinerary getItinerary(Long itineraryId) {
        return TimesHelper.getItinerary(getReadableDatabase(),itineraryId);
    }

}
