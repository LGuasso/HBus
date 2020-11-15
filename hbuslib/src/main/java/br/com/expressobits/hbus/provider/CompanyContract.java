package br.com.expressobits.hbus.provider;

import br.com.expressobits.hbus.dao.SQLConstants;

/**
 * @author Rafael Correa
 * @since 01/03/17
 */
public class CompanyContract {

    public static final String SQL_CREATE_TABLE =
            SQLConstants.CREATE_TABLE + Company.TABLE_NAME + " (" +
                    Company._ID + SQLConstants.TEXT_PRIMARY_KEY + SQLConstants.COMMA_SEP +
                    Company.COLUMN_NAME_NAME + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Company.COLUMN_NAME_EMAIL + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Company.COLUMN_NAME_WEBSITE + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Company.COLUMN_NAME_PHONENUMBER + SQLConstants.TEXT_TYPE + SQLConstants.COMMA_SEP +
                    Company.COLUMN_NAME_ADDRESS + SQLConstants.TEXT_TYPE +
                    SQLConstants.PARENTES+SQLConstants.POINTCOMMA;

    public static final String[] COLS = {
            Company._ID,
            Company.COLUMN_NAME_NAME,
            Company.COLUMN_NAME_EMAIL,
            Company.COLUMN_NAME_WEBSITE,
            Company.COLUMN_NAME_PHONENUMBER,
            Company.COLUMN_NAME_ADDRESS,
    };

    public static final String SQL_DELETE_ALL =
            "DELETE FROM " + Company.TABLE_NAME;

    public static String getInsertSQL(br.com.expressobits.hbus.model.Company company) {
        return "INSERT INTO "+ Company.TABLE_NAME+" ("+
                Company._ID+","+
                Company.COLUMN_NAME_NAME+","+
                Company.COLUMN_NAME_EMAIL+","+
                Company.COLUMN_NAME_WEBSITE+","+
                Company.COLUMN_NAME_PHONENUMBER+","+
                Company.COLUMN_NAME_ADDRESS+") "+
                "VALUES ("+
                "'"+company.getId()+"'"+","+
                "'"+company.getName()+"'"+","+
                "'"+company.getEmail()+"'"+","+
                "'"+company.getWebsite()+"'"+","+
                "'"+company.getPhoneNumber()+"'"+","+
                "'"+company.getAddress()+"'"+");";
    }

    public static abstract class Company{
        public static final String TABLE_NAME = "company";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_WEBSITE = "website";
        public static final String COLUMN_NAME_PHONENUMBER = "phoneNumber";
        public static final String COLUMN_NAME_ADDRESS = "address";

    }
}
