package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Arrays;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.tour.TourActivity;

/**
 * @author Rafael Correa
 * @since 02/11/15.
 */
public class ManagerInit {


    public static void manager(Context context){
        Class cl;
        Intent mainIntent;
        boolean isTour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TourActivity.TAG, true);
        Log.e("TESTE",isTour+" isTour");

        if(isTour){
            cl=TourActivity.class;
            mainIntent = new Intent(context, cl);
            mainIntent.putExtra(TourActivity.STARTER_MODE,true);
        }else{
            if(PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY).equals(SelectCityActivity.NOT_CITY)){

                cl=SelectCityActivity.class;
                mainIntent = new Intent(context, cl);
                mainIntent.putExtra(SelectCityActivity.STARTER_MODE,true);
            }else{
                cl = MainActivity.class;
                mainIntent = new Intent(context, cl);
            }
        }
        /* Create an Intent that will start the Menu-Activity. */

        context.startActivity(mainIntent);
    }
}
