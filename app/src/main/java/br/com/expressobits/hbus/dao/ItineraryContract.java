package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * Seguindo regras especificadas no treino de android
 * http://developer.android.com/intl/pt-br/training/basics/data-storage/databases.html
 *
 * Especifíca um contrato para nomes de colunas da tabela de itinerários
 *
 * @author Rafael
 * @since 30/11/15.
 */
public final class ItineraryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ItineraryContract(){}

    /* Inner class that defines the table contents */
    public static abstract class Itinerary implements BaseColumns {
        public static final String TABLE_NAME = "Itinerary";
        public static final String COLUMN_NAME_ENTRY_ID = "itineraryid";
        public static final String COLUMN_NAME_WAYS = "ways";
        public static final String COLUMN_NAME_CITY_ID= "cityid";

    }
}
