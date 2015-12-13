package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * Seguindo regras especificadas no treino de android
 * http://developer.android.com/intl/pt-br/training/basics/data-storage/databases.html
 *
 * Especifica um contrato para nomes de colunas da tabela de itinerários
 *
 * @author Rafael
 * @since 30/11/15.
 */
public final class ItineraryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ItineraryContract(){}

    public static final String[] COLS = {
            Itinerary._ID,
            Itinerary.COLUMN_NAME_NAME,
            Itinerary.COLUMN_NAME_WAYS,
            Itinerary.COLUMN_NAME_CITY_ID
    };

    private static final String SQL_DELETE_ITINERARIES =
            "DROP TABLE IF EXISTS " + ItineraryContract.Itinerary.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class Itinerary implements BaseColumns {
        public static final String TABLE_NAME = "itinerary";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_WAYS = "ways";
        public static final String COLUMN_NAME_CITY_ID= "cityid";

    }
}
