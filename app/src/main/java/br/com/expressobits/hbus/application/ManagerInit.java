package br.com.expressobits.hbus.application;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.io.File;

import br.com.expressobits.hbus.ui.DownloadScheduleActivity;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.tour.TourActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.StringUtils;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static br.com.expressobits.hbus.ui.DownloadScheduleActivity.DATABASE_VERSION;

/**
 * @author Rafael Correa
 * @since 02/11/15.
 */
public class ManagerInit {

    public static void manager(final Context context){


        boolean isTour = getDefaultSharedPreferences(context).getBoolean(TourActivity.TAG, true);

        if(isTour){
            Intent tourIntent = new Intent(context,TourActivity.class);
            tourIntent.putExtra(TourActivity.STARTER_MODE,true);
            context.startActivity(tourIntent);
        }else{
            String cityId = PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);

            if(cityId.equals(SelectCityActivity.NOT_CITY)){
                Intent selectCityIntent = new Intent(context,SelectCityActivity.class);
                selectCityIntent.putExtra(SelectCityActivity.STARTER_MODE,true);
                context.startActivity(selectCityIntent);
            }else{
                String country = FirebaseUtils.getCountry(cityId);
                String cityName = FirebaseUtils.getCityName(cityId);
                if(!isDatabaseFileFound(context,country,cityName)){
                    Intent downloadIntent = new Intent(context, DownloadScheduleActivity.class);
                    downloadIntent.putExtra(DownloadScheduleActivity.STARTER_MODE,true);
                    downloadIntent.putExtra(DownloadScheduleActivity.UPDATE_MODE,false);
                    context.startActivity(downloadIntent);
                }else {
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    context.startActivity(mainIntent);
                }
            }
        }

    }
    public static boolean isDatabaseFileFound(Context context,String country,String cityName){
        File localFile = context.getDatabasePath(
                StringUtils.getNameDatabase(country,cityName,DATABASE_VERSION));
        return localFile.exists();
    }


}
