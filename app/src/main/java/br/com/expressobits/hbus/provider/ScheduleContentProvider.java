package br.com.expressobits.hbus.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 *
 * @author Rafael Correa
 * @since 21/03/17
 */

public class ScheduleContentProvider extends ContentProvider {

    private String country;
    private String city;

    private SQLiteOpenHelper mSqliteOpenHelper;

    private static final String TAG = "ScheduleContentProvider";

    public static final String AUTHORITY = "br.com.expressobits.hbus.provider";
    public static final Uri CONTENT_URI_COMPANY =  Uri.parse("content://br.com.expressobits.hbus.provider/"+CompanyContract.Company.TABLE_NAME);
    public static final int COMPANY = 1;
    public static final int COMPANY_OFFSET = 2;
    public static final int COMPANY_ID = 3;


    private static UriMatcher uriCompanies;

    static {
        uriCompanies = new UriMatcher(UriMatcher.NO_MATCH);
        uriCompanies.addURI(AUTHORITY,CompanyContract.Company.TABLE_NAME+"/",COMPANY);
        uriCompanies.addURI(AUTHORITY,CompanyContract.Company.TABLE_NAME+"/offset/#",COMPANY_OFFSET);
        uriCompanies.addURI(AUTHORITY,CompanyContract.Company.TABLE_NAME+"/*",COMPANY_ID);
    }

    public static Uri urlForItems(int limit) {
        return Uri.parse("content://" + AUTHORITY + "/" + CompanyContract.Company.TABLE_NAME + "/offset/" + limit);
    }

    public static Uri urlForAllItems() {
        return Uri.parse("content://" + AUTHORITY + "/" + CompanyContract.Company.TABLE_NAME + "/");
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    synchronized public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String cityId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        this.country = FirebaseUtils.getCountry(cityId);
        this.city = FirebaseUtils.getCityName(cityId);
        mSqliteOpenHelper = new ScheduleDAO(getContext(),country,city);
        SQLiteDatabase db = mSqliteOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        Cursor c = null;

        switch (uriCompanies.match(uri)){
            case COMPANY:
                sortOrder = sortOrder == null ? CompanyContract.Company.COLUMN_NAME_NAME+" ASC": sortOrder;
                sqb.setTables(CompanyContract.Company.TABLE_NAME);
                c = sqb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COMPANY_OFFSET:
                sortOrder = sortOrder == null ? CompanyContract.Company.COLUMN_NAME_NAME+" ASC": sortOrder;
                sqb.setTables(CompanyContract.Company.TABLE_NAME);
                String offset = uri.getLastPathSegment();

                int intOffset = Integer.parseInt(offset);

                String limitArg = intOffset + ", " + 1;
                Log.d(TAG, "query: " + limitArg);
                c = sqb.query(db, projection, selection, selectionArgs, null, null, sortOrder, limitArg);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            case COMPANY_ID:
                String aux = CompanyContract.Company._ID+" = "+uri.getLastPathSegment();
                Log.d(TAG,aux+" id company");

                selection = selection == null ? aux : selection+" AND "+aux;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: "+uri);
        }



        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //30 minutos
        switch (uriCompanies.match(uri)){
            case COMPANY:
                return "vnd.android.cursor.dir/vnd.br.com.expressobits.hbus.company";
            case COMPANY_ID:
                return "vnd.android.cursor.item/vnd.br.com.expressobits.hbus.company";
            default:
                throw new IllegalArgumentException("Invalid Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
