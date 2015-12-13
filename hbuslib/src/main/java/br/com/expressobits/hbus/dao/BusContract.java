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

    public static final String[] COLS = {
            Bus._ID,
            Bus.COLUMN_NAME_TIME,
            Bus.COLUMN_NAME_CODE_ID,
            Bus.COLUMN_NAME_ITINERARY_ID,
            Bus.COLUMN_NAME_WAY,
            Bus.COLUMN_NAME_TYPEDAY,
            Bus.COLUMN_NAME_CITY_ID
    };

    public static abstract class Bus implements BaseColumns{
        public static final String TABLE_NAME = "bus";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CODE_ID = "codeid";
        public static final String COLUMN_NAME_ITINERARY_ID = "itineraryid";
        public static final String COLUMN_NAME_WAY = "way";
        public static final String COLUMN_NAME_TYPEDAY = "typeday";
        public static final String COLUMN_NAME_CITY_ID = "cityid";

    }

}
