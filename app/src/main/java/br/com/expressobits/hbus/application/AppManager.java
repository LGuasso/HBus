package br.com.expressobits.hbus.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.analytics.FirebaseAnalyticsManager;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.ui.DownloadScheduleActivity;
import br.com.expressobits.hbus.ui.TimesActivity;
import br.com.expressobits.hbus.utils.StringUtils;

import static br.com.expressobits.hbus.ui.DownloadScheduleActivity.DATABASE_LAST_UPDATE_PREFERENCE_KEY;
import static br.com.expressobits.hbus.ui.DownloadScheduleActivity.DATABASE_VERSION;

/**
 * @author Rafael Correa
 * @since 25/03/16
 * Gerenciador do aplicativo.
 */
public class AppManager {

    private static int countCloseTimesActivity = 0;

    static boolean countTimesActivity(Context context){
        if(countCloseTimesActivity>=context.getResources().getInteger(R.integer.counts_close_times_activity)){
            countCloseTimesActivity = 0;
            return true;
        }else {
            countCloseTimesActivity++;
            return false;
        }
    }

    public static void onSettingsDone(Context context,String country, String city, String company, String itinerary, String way) {
        AdManager.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                openTimes(context,country,city,company,itinerary,way);
                AdManager.initAdInterstitial(context);
            }

        });
        if(!AdManager.showAdIntersticial(context)){
            openTimes(context,country, city, company, itinerary, way);
        }
    }

    private static void openTimes(Context context, String country, String city, String company, String itinerary, String way) {
        FirebaseAnalyticsManager.registerEventItinerary(context,country,city,company,itinerary);
        Intent intent = new Intent(context, TimesActivity.class);
        intent.putExtra(TimesActivity.ARGS_COUNTRY, country);
        intent.putExtra(TimesActivity.ARGS_CITY, city);
        intent.putExtra(TimesActivity.ARGS_COMPANY, company);
        intent.putExtra(TimesActivity.ARGS_ITINERARY, itinerary);
        intent.putExtra(TimesActivity.ARGS_WAY, way);
        context.startActivity(intent);
    }


    /**
     * Verifica se banco atual está atualizado se não estiver atualizado abre tela para baixar
     * @param context Context of application
     * @param country String id of country city
     * @param city City name
     */
    public static void verifyUpdatedDatabase(Context context,String country, String city){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child(SQLConstants.DATABASE_PATTERN_NAME).
                child(country).child(city).child(
                StringUtils.getNameDatabase(country,city,DATABASE_VERSION));
        final SharedPreferences sharedPref = context.getSharedPreferences(
                DATABASE_LAST_UPDATE_PREFERENCE_KEY,Context.MODE_PRIVATE);
        fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                if(sharedPref.getLong(SQLConstants.getIdCityDefault(country,city), 0L)<storageMetadata.getUpdatedTimeMillis()){
                    Log.i("test","NO UPDATED "+storageMetadata.getUpdatedTimeMillis());
                    Intent downloadIntent = new Intent(context, DownloadScheduleActivity.class);
                    downloadIntent.putExtra(DownloadScheduleActivity.STARTER_MODE,false);
                    downloadIntent.putExtra(DownloadScheduleActivity.UPDATE_MODE,true);
                    context.startActivity(downloadIntent);
                }
            }
        });
    }

}
