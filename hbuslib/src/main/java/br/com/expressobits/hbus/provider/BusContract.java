package br.com.expressobits.hbus.provider;

import br.com.expressobits.hbus.dao.SQLConstants;

/**
 * @author Rafael Correa
 * @since 30/11/15
 */
public final class BusContract {

    public static final String SQL_CREATE_TABLE =
            SQLConstants.CREATE_TABLE + Bus.TABLE_NAME + " (" +
                    Bus._ID + SQLConstants.TEXT_PRIMARY_KEY + SQLConstants.COMMA_SEP +
                    Bus.COLUMN_NAME_TIME + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Bus.COLUMN_NAME_CODE + SQLConstants.TEXT_TYPE +
                    SQLConstants.PARENTES+SQLConstants.POINTCOMMA;

    public static final String[] COLS = {
            Bus._ID,
            Bus.COLUMN_NAME_TIME,
            Bus.COLUMN_NAME_CODE,
    };

    public static String getInsertSQL(br.com.expressobits.hbus.model.Bus bus){
        return "INSERT INTO "+ Bus.TABLE_NAME+" ("+
                Bus._ID+","+
                Bus.COLUMN_NAME_TIME+","+
                Bus.COLUMN_NAME_CODE+") "+
                "VALUES ("+
                "'"+bus.getId()+"'"+","+
                   +bus.getTime()+","+
                "'"+bus.getCode()+"'"+");";

    }

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + Bus.TABLE_NAME;

    public static abstract class Bus{
        public static final String TABLE_NAME = "bus";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CODE = "code";

    }

}
