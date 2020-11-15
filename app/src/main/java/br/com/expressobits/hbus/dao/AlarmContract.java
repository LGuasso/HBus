package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * Contains information about constants of data alarm
 * @author Rafael Correa
 * @since 30/03/16
 */
class AlarmContract {

    static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Alarm.TABLE_NAME + " (" +
                    Alarm._ID + SQLConstants.TEXT_PRIMARY_KEY + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_TIME + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_SUNDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_MONDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_TUESDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_WEDNESDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_THURSDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_FRIDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_DAYS_SATURDAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_TIME_DELAY + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_NAME + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_ACTIVED + SQLConstants.INTEGER_TYPE + SQLConstants.COMMA_SEP +
                    Alarm.COLUMN_NAME_CODE + SQLConstants.TEXT_TYPE +
                    SQLConstants.PARENTES+SQLConstants.POINTCOMMA;

    static final String[] COLS = {
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

    public static abstract class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        static final String COLUMN_NAME_TIME = "time";
        static final String COLUMN_NAME_DAYS_SUNDAY = "sunday";
        static final String COLUMN_NAME_DAYS_MONDAY = "monday";
        static final String COLUMN_NAME_DAYS_TUESDAY = "tuesday";
        static final String COLUMN_NAME_DAYS_WEDNESDAY = "wednesday";
        static final String COLUMN_NAME_DAYS_THURSDAY = "thursday";
        static final String COLUMN_NAME_DAYS_FRIDAY = "friday";
        static final String COLUMN_NAME_DAYS_SATURDAY = "saturday";
        static final String COLUMN_NAME_TIME_DELAY = "timedelay";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_ACTIVED = "actived";
        static final String COLUMN_NAME_CODE = "code";
    }
}
