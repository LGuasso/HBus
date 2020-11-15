package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.expressobits.hbus.model.News;

/**
 * @author Rafael Correa
 * @since 07/03/17
 */

public class NewsHelper {

    private static ContentValues toContentValues(News news){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.News._ID,news.getId());
        return contentValues;
    }

    public static void insert(SQLiteDatabase db, News news){
        db.insert(
                NewsContract.News.TABLE_NAME,
                null,
                toContentValues(news));
    }

    static void update(SQLiteDatabase db, News news){
        db.update(
                NewsContract.News.TABLE_NAME,
                toContentValues(news),
                AlarmContract.Alarm._ID+" = ?",
                new String[]{news.getId()});
    }

    static News cursorToNews(Cursor c){
        News news = new News();
        news.setId(c.getString(c.getColumnIndexOrThrow(NewsContract.News._ID)));
        return news;
    }

    static boolean isExist(SQLiteDatabase db, String id){
        String where = NewsContract.News._ID+" = ?";
        String arguments[] = {id};
        Cursor cursor = db.query(
                NewsContract.News.TABLE_NAME,
                NewsContract.COLS,
                where,
                arguments,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
