package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import br.com.expressobits.hbus.ui.settings.SelectCityActivity;

/**
 * @author Rafael Correa
 * @since 02/11/15.
 */
public class ManagerInit {


    public static void manager(Context context){
        Class cl;
        //if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TourActivity.TAG, false)){
        //    cl=TourActivity.class;
        //}else
        if(PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG,null)==null){
            cl=SelectCityActivity.class;
        }else{
            cl = MainActivity.class;
        }

                /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(context, cl);
        context.startActivity(mainIntent);
    }
}
