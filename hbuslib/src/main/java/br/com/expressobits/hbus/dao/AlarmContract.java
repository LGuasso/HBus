package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * Contains informaçãoes sobre constantes de data alarm
 * @author Rafael Correa
 * @since 30/03/16
 */
public class AlarmContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public AlarmContract(){}

    protected static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Alarm.TABLE_NAME + " (" +
                    Alarm._ID + BusHelper.TEXT_PRIMARY_KEY + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_TIME + BusHelper.TEXT_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS + BusHelper.TEXT_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_TIME_DELAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_NAME + BusHelper.TEXT_TYPE +
                    BusHelper.PARENTES+BusHelper.POINTCOMMA;

    public static final String[] COLS = {
            Alarm._ID,
            Alarm.COLUMN_NAME_TIME,
            Alarm.COLUMN_NAME_DAYS,
            Alarm.COLUMN_NAME_TIME_DELAY,
            Alarm.COLUMN_NAME_NAME
    };

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + Alarm.TABLE_NAME;

    public static abstract class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DAYS = "days";
        public static final String COLUMN_NAME_TIME_DELAY = "timedelay";
        public static final String COLUMN_NAME_NAME = "name";

    }
}
