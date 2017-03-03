package br.com.expressobits.hbus.dao;

/**
 * Following specified rules in android training
 * http://developer.android.com/intl/pt-br/training/basics/data-storage/databases.html
 *
 * @author Rafael Correa
 * @since 30/11/15.
 */
public final class CityContract {

    static final String[] COLS= {
            City._ID,
            City.COLUMN_NAME_NAME,
            City.COLUMN_NAME_COUNTRY,
            City.COLUMN_NAME_LATITUDE,
            City.COLUMN_NAME_LONGITUDE
    };

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + City.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class City {
        public static final String TABLE_NAME = "city";
        static final String _ID = "_id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_COUNTRY= "country";
        static final String COLUMN_NAME_LATITUDE= "latitude";
        static final String COLUMN_NAME_LONGITUDE= "longitude";
    }
}
