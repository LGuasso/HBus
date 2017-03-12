package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.expressobits.hbus.model.News;

/**
 * @author Rafael Correa
 * @since 07/03/17
 */

public class NewsReadDAO extends SQLiteOpenHelper {

    private static final String TAG = "NewsReadDAO";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsread.db";
    public NewsReadDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(News news){
        if(!isExist(news)){
            NewsHelper.insert(getWritableDatabase(), news);
            return true;
        }else{
            return false;
        }
    }

    public boolean isExist(News news){
        return NewsHelper.isExist(getReadableDatabase(),news.getId());
    }
}
