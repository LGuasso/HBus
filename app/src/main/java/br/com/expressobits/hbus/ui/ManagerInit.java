package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.util.Arrays;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;

/**
 * @author Rafael Correa
 * @since 02/11/15.
 */
public class ManagerInit {


    public static void manager(Context context){
        Class cl;
        Intent mainIntent;
        //if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TourActivity.TAG, false)){
        //    cl=TourActivity.class;
        //}else
        if(PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY).equals(SelectCityActivity.NOT_CITY)){

            cl=SelectCityActivity.class;
            mainIntent = new Intent(context, cl);
            mainIntent.putExtra(SelectCityActivity.STARTER_MODE,true);
        }else{
            cl = MainActivity.class;
            mainIntent = new Intent(context, cl);
        }

                /* Create an Intent that will start the Menu-Activity. */

        context.startActivity(mainIntent);
    }
}
