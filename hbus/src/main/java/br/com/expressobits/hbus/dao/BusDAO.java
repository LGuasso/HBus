package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * Created by rafael on 19/01/16.
 */
public class BusDAO extends SQLiteOpenHelper{


    public BusDAO(Context context) {
        super(context, BusHelper.DATABASE_NAME, null, BusHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(BusHelper.SQL_CREATE_CITIES);
            //setForcedUpgrade(DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(City city){
        BusHelper.insert(getWritableDatabase(),city);
    }

    public List<City> getCities(){
        return BusHelper.getCities(getReadableDatabase());
    }
}
