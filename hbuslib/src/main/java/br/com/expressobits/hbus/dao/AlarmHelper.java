package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.backend.Alarm;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.utils.TextUtils;

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
        values.put(AlarmContract.Alarm.COLUMN_NAME_DAYS, TextUtils.getDaysinString(alarm.getDaysOfWeek()));
        values.put(AlarmContract.Alarm.COLUMN_NAME_TIME_DELAY,alarm.getMinuteDelay());
        values.put(AlarmContract.Alarm.COLUMN_NAME_NAME,alarm.getName());
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
        alarm.setTimeAlarm(c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_TIME)));
        //TODO arrumar algo de errado boolean = string
        alarm.setDaysOfWeek(new ArrayList<String>(
                (Arrays.asList(
                        c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_DAYS)).split(BusHelper.COMMA_SEP))
                )));
        alarm.setMinuteDelay(c.getInt(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_TIME_DELAY)));
        alarm.setName(c.getString(c.getColumnIndexOrThrow(AlarmContract.Alarm.COLUMN_NAME_NAME)));
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

    public static List<Alarm> getAlarms(SQLiteDatabase db,City city){
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        String where = AlarmContract.Alarm._ID+" LIKE ?";
        String arguments[] = {city.getCountry()+BusHelper.BARS+city.getName()+"%"};
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
        String arguments[] = {city.getCountry()+BusHelper.BARS+city.getName()+"%",time};
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
