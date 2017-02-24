package br.com.expressobits.hbus.application;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdListener;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.analytics.FirebaseAnalyticsManager;
import br.com.expressobits.hbus.ui.TimesActivity;

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

}
