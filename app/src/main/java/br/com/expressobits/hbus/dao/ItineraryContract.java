package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * Following specified rules in android training
 * http://developer.android.com/intl/pt-br/training/basics/data-storage/databases.html
 *
 * Specifies a contract for column names in the itinerary table
 *
 * @author Rafael
 * @since 30/11/15.
 */
public final class ItineraryContract {

    static final String SQL_CREATE_TABLE =
            "CREATE TABLE "+Itinerary.TABLE_NAME+" "+
                    "("+Itinerary._ID+ BusHelper.TEXT_PRIMARY_KEY+BusHelper.COMMA_SEP+
                    " "+Itinerary.COLUMN_NAME_NAME+BusHelper.TEXT_TYPE+BusHelper.COMMA_SEP+
                    " "+Itinerary.COLUMN_NAME_WAYS+BusHelper.TEXT_TYPE+
                    BusHelper.PARENTES+BusHelper.POINTCOMMA;


    static final String[] COLS = {
            Itinerary._ID,
            Itinerary.COLUMN_NAME_NAME,
            Itinerary.COLUMN_NAME_WAYS
    };

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + Itinerary.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class Itinerary implements BaseColumns {
        public static final String TABLE_NAME = "itinerary";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_WAYS = "ways";

    }
}
