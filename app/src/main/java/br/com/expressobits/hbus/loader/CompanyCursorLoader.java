package br.com.expressobits.hbus.loader;


import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * @author Rafael
 * @since 22/03/17
 */

public class CompanyCursorLoader extends CursorLoader{

    public CompanyCursorLoader(Context context) {
        super(context);
    }

    public CompanyCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

}
