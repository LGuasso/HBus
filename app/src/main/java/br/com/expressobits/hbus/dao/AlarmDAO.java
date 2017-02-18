package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.model.City;

/**
 * Class manager a database of alarms
 * @author Rafael Correa
 * @since 30/03/16
 */
public class AlarmDAO extends SQLiteOpenHelper {

    private static final String TAG = "AlarmDAO";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarms.db";
    public AlarmDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AlarmContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *
     * @return false if old alarm and true to new alarm
     */
    public boolean insert(Alarm alarm){
        if((getAlarm(alarm.getId()))==null){
            Log.d(TAG,"insert new alarm!\t"+alarm.getId());
            AlarmHelper.insert(getWritableDatabase(), alarm);
            return true;
        }else {
            Log.d(TAG,"update alarm!\t"+alarm.getId());
            AlarmHelper.update(getWritableDatabase(), alarm);
            return false;
        }

    }

    public Alarm getAlarm(String id){
        return AlarmHelper.getAlarm(getReadableDatabase(), id);
    }

    public List<Alarm> getAlarms(){
        return AlarmHelper.getAlarms(getReadableDatabase());
    }

    public List<Alarm> getAlarms(City city){
        return AlarmHelper.getAlarms(getReadableDatabase(), city);
    }

    public void delete(String id){
        AlarmHelper.delete(getWritableDatabase(), id);
    }




}
