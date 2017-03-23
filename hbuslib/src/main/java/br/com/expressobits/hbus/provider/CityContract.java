package br.com.expressobits.hbus.provider;

/**
 * Following specified rules in android training
 * http://developer.android.com/intl/pt-br/training/basics/data-storage/databases.html
 *
 * @author Rafael Correa
 * @since 30/11/15.
 */
public final class CityContract {

    public static final String[] COLS= {
            City._ID,
            City.COLUMN_NAME_NAME,
            City.COLUMN_NAME_COUNTRY,
            City.COLUMN_NAME_LATITUDE,
            City.COLUMN_NAME_LONGITUDE
    };

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + City.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class City {
        public static final String TABLE_NAME = "city";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COUNTRY= "country";
        public static final String COLUMN_NAME_LATITUDE= "latitude";
        public static final String COLUMN_NAME_LONGITUDE= "longitude";
    }
}
