package br.com.expressobits.hbus.dao;

import android.provider.BaseColumns;

/**
 * @author Rafael Correa
 * @since 30/11/15.
 */
final class CodeContract {

    static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CodeContract.Code.TABLE_NAME + " (" +
                    CodeContract.Code._ID + BusHelper.TEXT_PRIMARY_KEY + BusHelper.COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_NAME + BusHelper.TEXT_TYPE + BusHelper.COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_DESCRIPTION + BusHelper.TEXT_TYPE +
                    BusHelper.PARENTES+BusHelper.POINTCOMMA;


    static final String[] COLS = {
            Code._ID,
            Code.COLUMN_NAME_NAME,
            Code.COLUMN_NAME_DESCRIPTION
    };

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + Code.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class Code implements BaseColumns{
        public static final String TABLE_NAME = "code";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
