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
                    Alarm.COLUMN_NAME_DAYS_SUNDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_MONDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_TUESDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_WEDNESDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_THURSDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_FRIDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_SATURDAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_TIME_DELAY + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_NAME + BusHelper.TEXT_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_ACTIVED + BusHelper.INTEGER_TYPE + BusHelper.COMMA_SEP +
                    Alarm.COLUMN_NAME_CODE + BusHelper.TEXT_TYPE +
                    BusHelper.PARENTES+BusHelper.POINTCOMMA;

    public static final String[] COLS = {
            Alarm._ID,
            Alarm.COLUMN_NAME_TIME,
            Alarm.COLUMN_NAME_DAYS_SUNDAY,
            Alarm.COLUMN_NAME_DAYS_MONDAY,
            Alarm.COLUMN_NAME_DAYS_TUESDAY,
            Alarm.COLUMN_NAME_DAYS_WEDNESDAY,
            Alarm.COLUMN_NAME_DAYS_THURSDAY,
            Alarm.COLUMN_NAME_DAYS_FRIDAY,
            Alarm.COLUMN_NAME_DAYS_SATURDAY,
            Alarm.COLUMN_NAME_TIME_DELAY,
            Alarm.COLUMN_NAME_NAME,
            Alarm.COLUMN_NAME_ACTIVED,
            Alarm.COLUMN_NAME_CODE

    };

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + Alarm.TABLE_NAME;

    public static abstract class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DAYS_SUNDAY = "sunday";
        public static final String COLUMN_NAME_DAYS_MONDAY = "monday";
        public static final String COLUMN_NAME_DAYS_TUESDAY = "tuesday";
        public static final String COLUMN_NAME_DAYS_WEDNESDAY = "wednesday";
        public static final String COLUMN_NAME_DAYS_THURSDAY = "thursday";
        public static final String COLUMN_NAME_DAYS_FRIDAY = "friday";
        public static final String COLUMN_NAME_DAYS_SATURDAY = "saturday";
        public static final String COLUMN_NAME_TIME_DELAY = "timedelay";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ACTIVED = "actived";
        public static final String COLUMN_NAME_CODE = "code";
    }
}
