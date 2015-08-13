package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Rafael on 05/07/2015.
 * @deprecated
 */
public class FavoritosDAO extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String TABLE = "Favoritos";
    private static String[] COLS = {"id","name","codigo"};

    public FavoritosDAO(Context context){
        super(context, TABLE, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE + " ");
        sb.append("(id INTEGER PRIMARY KEY, ");
        sb.append(" name TEXT,");
        sb.append(" code TEXT);");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS "+ TABLE);
        db.execSQL(sb.toString());
        onCreate(db);
    }

    public ArrayList<String> getLista(){
        ArrayList<String> lista = new ArrayList<String>();
        Cursor c;
        c = getWritableDatabase().query(TABLE,COLS,null,null,null,null,null);
        while(c.moveToNext()){
            String txt = c.getString(1);
            txt+=" - ";
            txt+=c.getString(2);
            lista.add(txt);
        }
        c.close();
        return lista;
    }

    public void inserirOuAlterar(String linha){
        //TODO verificar se ja existe
        ContentValues c = new ContentValues();
        c.put("name",linha.split(" - ")[0]);
        if(!linha.contains(" - ")){
            c.put("codigo"," NO ");
        }else {
            c.put("codigo", linha.split(" - ")[1]);
        }
        getWritableDatabase().insert(TABLE, null,c);
    }


    public void deletar(String linha){

       getWritableDatabase().delete(TABLE, "name=?",new String[]{linha});
    }



}
