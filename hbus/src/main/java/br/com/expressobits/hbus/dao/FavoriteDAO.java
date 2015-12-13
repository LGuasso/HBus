package br.com.expressobits.hbus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * Classe que determina database para {@link br.com.expressobits.hbus.model.Itinerary} favoritos,
 * aqui é armazenado da mesma forma que BusDAO , mas somente a classe Itineray
 * @author Rafael Correa
 * @since 28/11/15.
 */
public class FavoriteDAO extends SQLiteOpenHelper{

    private static final String[] COLS_ITINERARY = {"_id","name","ways","city"};
    private static final String TABLE_ITINERARY = "Itinerary";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASENAME = "Favorites";

    public FavoriteDAO(Context context){
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_ITINERARY + " ");
        sb.append("(_id INTEGER PRIMARY KEY, ");
        sb.append(" name TEXT,");
        sb.append(" ways TEXT,");
        sb.append(" city TEXT);");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Método <b>auxiliar</b> que converte {@link Itinerary} em um {@link ContentValues}
     *
     * <p>Note: conversão de ways em lista está para o método toString()!</p>
     * @param itinerary Itinerário que será convertido
     * @return {@link ContentValues} com valores lidos pelos métodos
     * <li>getWritableDatabase
     * <li>getRedableDatabase
     */
    private ContentValues itinerarytoContentValues(Itinerary itinerary){
        ContentValues values = new ContentValues();
        values.put(COLS_ITINERARY[1],itinerary.getName());
        values.put(COLS_ITINERARY[2], TextUtils.getSentidosinString(itinerary.getWays()));
        values.put(COLS_ITINERARY[3],"Santa Maria - RS");
        return values;
    }

    /**
     * Adiciona um novo {@link Itinerary} ao banco de dados.
     * @param itinerary Itinerário que será adicionado no banco de dados
     */
    public void addFavorite(Itinerary itinerary){
        getWritableDatabase().insert(TABLE_ITINERARY, null, itinerarytoContentValues(itinerary));
    }

    /**
     * Remove {@link Itinerary} do banco de dados.
     * TODO problema ao ler lista toda de itinerário me da id errada do itinerário para deletar aqui.
     * @param itinerary
     */
    public void removeFavorite(Itinerary itinerary){
        getWritableDatabase().delete(TABLE_ITINERARY, "_id = ?", new String[]{itinerary.getId().toString()});
    }

    public List<Itinerary> getItineraries(){
        ArrayList<Itinerary> itinerariess = new ArrayList<Itinerary>();
        Cursor c;

        c = getWritableDatabase().query(TABLE_ITINERARY, COLS_ITINERARY, null, null, null, null, null);
        while(c.moveToNext()){
            Itinerary itinerary = new Itinerary();
            itinerary.setId(c.getLong(0));
            itinerary.setName(c.getString(1));
            itinerary.setWays(new ArrayList<String>(Arrays.asList(c.getString(2).split(","))));
            //TODO implementar sem setar cidade
            //itinerary.setCity(";
            itinerariess.add(itinerary);

        }
        c.close();
        return itinerariess;
    }

    public Itinerary getItinerary(String itineraryName) {
        //ArrayList<Code> codes = new ArrayList<Code>();
        Itinerary itinerary = new Itinerary();
        Cursor c;
        String where = "name = ?";
        String arguments[] = {itineraryName};

        c = getWritableDatabase().query(TABLE_ITINERARY, COLS_ITINERARY, where, arguments, null, null, null);
        while (c.moveToNext()) {
            itinerary = new Itinerary();
            itinerary.setId(c.getLong(0));
            itinerary.setName(c.getString(1));
            itinerary.setWays(new ArrayList<String>(Arrays.asList(c.getString(3).split(","))));
            //itinerary.setCity(getCity(c.getString(3)));
        }
        c.close();
        return itinerary;
    }

}
