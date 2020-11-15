package br.com.expressobits.hbus.dao;

import br.com.expressobits.hbus.utils.StringUtils;

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

    public static final String SQL_CREATE_TABLE =
            SQLConstants.CREATE_TABLE+Itinerary.TABLE_NAME+" "+
                    "("+Itinerary._ID+ SQLConstants.TEXT_PRIMARY_KEY+SQLConstants.COMMA_SEP+
                    " "+Itinerary.COLUMN_NAME_NAME+SQLConstants.TEXT_TYPE+SQLConstants.COMMA_SEP+
                    " "+Itinerary.COLUMN_NAME_WAYS+SQLConstants.TEXT_TYPE+
                    SQLConstants.PARENTES+SQLConstants.POINTCOMMA;


    static final String[] COLS = {
            Itinerary._ID,
            Itinerary.COLUMN_NAME_NAME,
            Itinerary.COLUMN_NAME_WAYS
    };

    public static String getInsertSQL(br.com.expressobits.hbus.model.Itinerary itinerary){
        return "INSERT INTO "+Itinerary.TABLE_NAME+" ("+
                Itinerary._ID+","+
                Itinerary.COLUMN_NAME_NAME+","+
                Itinerary.COLUMN_NAME_WAYS+") "+
                "VALUES ("+
                "'"+itinerary.getId()+"'"+","+
                "'"+itinerary.getName()+"'"+","+
                "'"+ StringUtils.getSentidosinString(itinerary.getWays())+"'"+");";

    }

    static final String SQL_DELETE_ALL =
            "DELETE FROM " + Itinerary.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class Itinerary {
        public static final String TABLE_NAME = "itinerary";
        static final String _ID = "_id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_WAYS = "ways";

    }
}
