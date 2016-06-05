package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * Classe que determina database para {@link br.com.expressobits.hbus.model.Itinerary} favoritos,
 * aqui e armazenado da mesma forma que BusDAO , mas somente a classe Itineray
 * @author Rafael Correa
 * @since 28/11/15.
 */
public class FavoriteDAO extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "itineraries_favorites.db";
    private  Context context;
    public FavoriteDAO(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
        BusDAO dao = new BusDAO(context);
        City city = dao.getCity(cityId);
        dao.close();
        return BusHelper.getItineraries(getReadableDatabase(),city);
    }

    public Itinerary getItinerary(String itineraryId) {
        return BusHelper.getItinerary(getReadableDatabase(),itineraryId);
    }

}
