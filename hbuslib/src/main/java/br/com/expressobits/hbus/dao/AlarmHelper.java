package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.utils.BooleanConvert;

/**
 * @author Rafael Correa
 * @since 30/03/16
 */
public class AlarmHelper {

    private static final String TAG = "AlarmHelper";

    protected static ContentValues toContentValues(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(AlarmContract.Alarm._ID,alarm.getId());
        values.put(AlarmContract.Alarm.COLUMN_NAME_TIME,alarm.getTimeAlarm());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_SUNDAY,alarm.isSunday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_MONDAY,alarm.isMonday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_TUESDAY,alarm.isTuesday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_WEDNESDAY,alarm.isWednesday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_THURSDAY,alarm.isThursday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_FRIDAY,alarm.isFriday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS_SATURDAY,alarm.isSaturday());
        values.put(AlarmContract.Alarm.COLUMN_NAME_TIME_DELAY,alarm.getMinuteDelay());
        values.put(AlarmContract.Alarm.COLUMN_NAME_NAME,alarm.getName());
        values.put(AlarmContract.Alarm.COLUMN_NAME_ACTIVED,alarm.isActived());
        values.put(AlarmContract.Alarm.COLUMN_NAME_CODE,alarm.getCode());
        return values;
    }

    public static void insert(SQLiteDatabase db,Alarm alarm){
        db.insert(
                AlarmContract.Alarm.TABLE_NAME,
                null,
                toContentValues(alarm));
    }

    public static void update(SQLiteDatabase db,Alarm alarm){
        db.update(
                AlarmContract.Alarm.TABLE_NAME,
                toContentValues(alarm),
                AlarmContract.Alarm._ID+" = ?",
                new String[]{alarm.getId()});
    }

    protected static Alarm cursorToAlarm(Cursor c){
        Alarm alarm = new Alarm();
        alarm.setId(c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm._ID)));
        alarm.setTimeAlarm(Long.valueOf(
                c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_TIME))));
        alarm.setSunday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_SUNDAY))));
        alarm.setMonday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_MONDAY))));
        alarm.setTuesday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_TUESDAY))));
        alarm.setWednesday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_WEDNESDAY))));
        alarm.setThursday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_THURSDAY))));
        alarm.setFriday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_FRIDAY))));
        alarm.setSaturday(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS_SATURDAY))));
        alarm.setMinuteDelay(c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_TIME_DELAY)));
        alarm.setName(c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_NAME)));
        alarm.setActived(BooleanConvert.IntegerToBoolean(
                c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_ACTIVED))));
        alarm.setCode(c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_CODE)));
        return alarm;
    }

    public static Alarm getAlarm(SQLiteDatabase db,String id){
        String where = AlarmContract.Alarm._ID+" = ?";
        String arguments[] = {id};
        Cursor cursor = db.query(
                AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            return cursorToAlarm(cursor);
        }
        return null;
    }

    public static List<Alarm> getAlarms(SQLiteDatabase db){
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        Cursor c;
        c = db.query(
                AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.COLS,
                null,
                null,
                null,
                null,
                null
        );
        while (c.moveToNext()){
            alarms.add(cursorToAlarm(c));
        }
        c.close();
        return alarms;
    }

    public static List<Alarm> getAlarms(SQLiteDatabase db,City city){
        Log.e("TESTE","TESTE");
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        String where = AlarmContract.Alarm._ID+" LIKE ?";
        String arguments[] = {BusHelper.BARS+city.getCountry()+BusHelper.BARS+city.getName()+"%"};
        Cursor c;
        c = db.query(
                AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        while (c.moveToNext()){
            alarms.add(cursorToAlarm(c));
        }
        c.close();
        return alarms;
    }

    public static List<Alarm> getAlarms(SQLiteDatabase db,City city,String time){
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        //TODO DELAY implementar
        String where = AlarmContract.Alarm._ID+" LIKE ? AND "+ AlarmContract.Alarm.COLUMN_NAME_TIME+" = ?";
        String arguments[] = {BusHelper.BARS+city.getCountry()+BusHelper.BARS+city.getName()+"%",time};
        Cursor c;
        c = db.query(
                AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        while (c.moveToNext()){
            alarms.add(cursorToAlarm(c));
        }
        c.close();
        return alarms;
    }

    public static int delete(SQLiteDatabase db,String id){
        String where = AlarmContract.Alarm._ID+" = ?";
        String arguments[] = {id};
        return db.delete(
                AlarmContract.Alarm.TABLE_NAME,
                where,
                arguments
        );
    }


}
