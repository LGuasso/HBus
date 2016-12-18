package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * @author Rafael
 * @since 14/01/16.
 */
public final class FeedbackContract {

    /* Inner class that defines the table contents */
    public static abstract class Feedback implements BaseColumns {
        public static final String TABLE_NAME = "feedback";
        //public static final String COLUMN_NAME_NAME = "name";
        //public static final String COLUMN_NAME_DESCRIPTION = "description";
        //public static final String COLUMN_NAME_CITY_ID = "cityid";
    }
}
