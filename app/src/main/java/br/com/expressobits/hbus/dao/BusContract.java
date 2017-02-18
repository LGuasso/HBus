package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * @author Rafael Correa
 * @since 30/11/15
 */
final class BusContract {

    static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Bus.TABLE_NAME + " (" +
                    Bus._ID + BusHelper.TEXT_PRIMARY_KEY + BusHelper.COMMA_SEP +
                    Bus.COLUMN_NAME_TIME + BusHelper.TEXT_TYPE + BusHelper.COMMA_SEP +
                    Bus.COLUMN_NAME_CODE + BusHelper.TEXT_TYPE +
                    BusHelper.PARENTES+BusHelper.POINTCOMMA;

    static final String[] COLS = {
            Bus._ID,
            Bus.COLUMN_NAME_TIME,
            Bus.COLUMN_NAME_CODE,
    };

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + Bus.TABLE_NAME;

    public static abstract class Bus implements BaseColumns{
        public static final String TABLE_NAME = "bus";
        static final String COLUMN_NAME_TIME = "time";
        static final String COLUMN_NAME_CODE = "code";

    }

}
