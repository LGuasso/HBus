package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * Seguindo regras especificadas no treino de android
 * http://developer.android.com/intl/pt-br/training/basics/data-storage/databases.html
 *
 * @author Rafael Correa
 * @since 30/11/15.
 */
public final class CityContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public CityContract(){}

    public static final String[] COLS= {
            City._ID,
            City.COLUMN_NAME_NAME,
            City.COLUMN_NAME_COUNTRY,
            City.COLUMN_NAME_POSITION
    };

    private static final String SQL_DELETE_CITIES =
            "DROP TABLE IF EXISTS " + CityContract.City.TABLE_NAME;

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + City.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class City implements BaseColumns {
        public static final String TABLE_NAME = "city";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COUNTRY= "country";
        public static final String COLUMN_NAME_POSITION= "position";
    }
}
