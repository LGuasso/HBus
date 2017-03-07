package br.com.expressobits.hbus.dao;

/**
 * @author Rafael Correa
 * @since 07/03/17
 */

public class NewsContract {

    public static final String SQL_CREATE_TABLE =
            SQLConstants.CREATE_TABLE + News.TABLE_NAME + " (" +
                    News._ID + SQLConstants.TEXT_PRIMARY_KEY +
                    SQLConstants.PARENTES+SQLConstants.POINTCOMMA;

    static final String[] COLS = {
            News._ID
    };

    public static String getInsertSQL(br.com.expressobits.hbus.model.Bus bus){
        return "INSERT INTO "+ News.TABLE_NAME+" ("+
                News._ID+") "+
                "VALUES ("+
                "'"+bus.getId()+"'"+");";

    }

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + News.TABLE_NAME;

    public static abstract class News{
        public static final String TABLE_NAME = "news";
        static final String _ID = "_id";

    }

}
