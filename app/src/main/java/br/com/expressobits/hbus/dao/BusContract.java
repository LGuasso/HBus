package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * @author Rafael Correa
 * @since 30/11/15
 */
public final class BusContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BusContract(){}

    protected static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Bus.TABLE_NAME + " (" +
                    Bus._ID + BusHelper.TEXT_PRIMARY_KEY + BusHelper.COMMA_SEP +
                    Bus.COLUMN_NAME_TIME + BusHelper.TEXT_TYPE + BusHelper.COMMA_SEP +
                    Bus.COLUMN_NAME_CODE + BusHelper.TEXT_TYPE +
                    BusHelper.PARENTES+BusHelper.POINTCOMMA;

    public static final String[] COLS = {
            Bus._ID,
            Bus.COLUMN_NAME_TIME,
            Bus.COLUMN_NAME_CODE,
    };

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + Bus.TABLE_NAME;

    public static abstract class Bus implements BaseColumns{
        public static final String TABLE_NAME = "bus";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CODE = "code";

    }

}
