package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rafael on 02/11/15.
 * TODO criar metodo que salva por id
 */
public class FavoriteDAO extends SQLiteOpenHelper{
    private static final String[] COLS_ITINERARY = {"_id","name","favorite","sentidos"};
    private static final String DATABASE_NAME = "santa_maria_rs_favorite_data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG= "FavoriteDAO";
    private static final String TABLE_ITINERARY = "Itinerary";


    public FavoriteDAO(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableItinerary(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
}
