package br.com.expressobits.hbus.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Rafael on 05/07/2015.
 */
public class Preferences {

    public static Context context;
    public static String FAVORITOS = "Favoritos";
    public static String GRUPO = "Dados";

    public static ArrayList<String> getFavoritos(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(GRUPO,context.MODE_PRIVATE);

        ArrayList<String> favoritos = new ArrayList<String>();
        String favoritosinString = context.getSharedPreferences(Preferences.GRUPO, context.MODE_PRIVATE)
                .getString(Preferences.FAVORITOS,"Carlos Gomes - 155B:144:45");

        String[] favoritosInSet = favoritosinString.split("=");

        favoritos = new ArrayList<String>(Arrays.asList(favoritosInSet));


        return favoritos;
    }


    public static void addFavoritos(Context context,String nomeDaLinha){

        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(GRUPO,context.MODE_PRIVATE);

        String favoritosinString = context.getSharedPreferences(Preferences.GRUPO, context.MODE_PRIVATE)
                .getString(Preferences.FAVORITOS, "");

        favoritosinString+=("="+nomeDaLinha);


        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(FAVORITOS, favoritosinString);
        editor.commit();
    }

    public static void removeFavoritos(Context context,String nomeDaLinha){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(GRUPO,context.MODE_PRIVATE);

        String favoritosinString = context.getSharedPreferences(Preferences.GRUPO, context.MODE_PRIVATE)
                .getString(Preferences.FAVORITOS, "");

        favoritosinString+=("="+nomeDaLinha);

        String removedStrings = new String();
        for(String txt:favoritosinString.split("=")){
            if(txt.equals(nomeDaLinha)){

            }else{
                removedStrings+=("="+txt);
            }
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(FAVORITOS, favoritosinString);
        editor.commit();
    }


}
