package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * @author Rafael Correa
 * @since 30/11/15.
 */
public final class CodeContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public CodeContract(){}

    public static final String[] COLS = {
            Code._ID,
            Code.COLUMN_NAME_NAME,
            Code.COLUMN_NAME_DESCRIPTION,
            Code.COLUMN_NAME_CITY_ID
    };

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + Code.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class Code implements BaseColumns{
        public static final String TABLE_NAME = "code";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CITY_ID = "cityid";
    }

    public static String NOT_CODE = "0000";
}
