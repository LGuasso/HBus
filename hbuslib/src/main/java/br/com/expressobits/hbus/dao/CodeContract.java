package br.com.expressobits.hbus.dao;

import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;

/**
 * @author Rafael Correa
 * @since 30/11/15.
 */
public final class CodeContract {

    public static final String SQL_CREATE_TABLE =
            SQLConstants.CREATE_TABLE + CodeContract.Code.TABLE_NAME + " (" +
                    CodeContract.Code._ID + SQLConstants.TEXT_PRIMARY_KEY + SQLConstants.COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_NAME + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    CodeContract.Code.COLUMN_NAME_DESCRIPTION + SQLConstants.TEXT_TYPE +
                    SQLConstants.PARENTES+SQLConstants.POINTCOMMA;


    static final String[] COLS = {
            Code._ID,
            Code.COLUMN_NAME_NAME,
            Code.COLUMN_NAME_DESCRIPTION
    };

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + Code.TABLE_NAME;

    public static String getInsertSQL(br.com.expressobits.hbus.model.Code code) {
        return "INSERT INTO "+ Code.TABLE_NAME+" ("+
                Code._ID+","+
                Code.COLUMN_NAME_NAME+","+
                Code.COLUMN_NAME_DESCRIPTION+") "+
                "VALUES ("+
                "'"+code.getId()+"'"+","+
                "'"+code.getName()+"'"+","+
                "'"+code.getDescrition()+"'"+");";
    }

    /* Inner class that defines the table contents */
    public static abstract class Code{
        public static final String TABLE_NAME = "code";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
