package br.com.expressobits.hbus.application;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.tour.TourActivity;

/**
 * @author Rafael Correa
 * @since 02/11/15.
 */
public class ManagerInit {

    public static void manager(final Context context){

        boolean isTour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TourActivity.TAG, true);
        Log.e("TESTE",isTour+" isTour");
        if(isTour){
            Intent tourIntent = new Intent(context,TourActivity.class);
            tourIntent.putExtra(TourActivity.STARTER_MODE,true);
            context.startActivity(tourIntent);

        }else if(PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY).equals(SelectCityActivity.NOT_CITY)){
            Intent selectCityIntent = new Intent(context,SelectCityActivity.class);
            selectCityIntent.putExtra(SelectCityActivity.STARTER_MODE,true);
            context.startActivity(selectCityIntent);
        }else {
            Intent mainIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainIntent);
        }

    }
}
