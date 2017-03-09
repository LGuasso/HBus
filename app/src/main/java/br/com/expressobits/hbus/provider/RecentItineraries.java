package br.com.expressobits.hbus.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.ui.settings.PrivacyPreferenceFragment;

/**
 * @author Rafael
 * @since 08/03/17
 */

public class RecentItineraries {

    private static final String RECENT_ITINERARIES_KEY = "br.com.expressobits.hbus.provider.RecentItinerariesKey";
    private static final String DEFAULT_VALUE = "not_recent_itinerary";

    private static final int LIMIT_RECENTS = 3;

    //TODO create setting then remove recent history itinerary

    private static void saveRecentItinerary(Context context,String cityId,String number,String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECENT_ITINERARIES_KEY+
                cityId.replace(SQLConstants.BARS,"."),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(number,id);
        editor.apply();
    }

    private static void clearRecentItinerary(Context context,String cityId,String number){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECENT_ITINERARIES_KEY+
                cityId.replace(SQLConstants.BARS,"."),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(number);
        editor.apply();
    }

    private static String getRecentIdItinerary(Context context,String cityId,String number){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECENT_ITINERARIES_KEY+
                cityId.replace(SQLConstants.BARS,"."),Context.MODE_PRIVATE);
        return sharedPreferences.getString(number,DEFAULT_VALUE);
    }

    public static List<String> getListRecentIdItineraries(Context context,String cityId){
        List<String> recents = new ArrayList<>();
        for (int i = 0; i < LIMIT_RECENTS; i++) {
            String id = getRecentIdItinerary(context,cityId,String.valueOf(i));
            if(!id.equals(DEFAULT_VALUE)){
                recents.add(id);
            }
        }
        return recents;
    }

    public static void saveRecentItineraries(Context context,String id,String cityId){
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PrivacyPreferenceFragment.PREFERENCE_PAUSE_VIEWING_HISTORY,false)){
            List<String> recents = getListRecentIdItineraries(context,cityId);
            if(recents.contains(id)){
                recents.remove(id);
            }
            recents.add(0,id);
            for (int i = 0; i < LIMIT_RECENTS; i++) {
                if(recents.size()>i){
                    saveRecentItinerary(context,cityId,String.valueOf(i),recents.get(i));
                }
            }
        }
    }

    public static void clearRecentViewItinerary(Context context,String cityId){
        for (int i = 0; i < LIMIT_RECENTS; i++) {
            clearRecentItinerary(context,cityId,String.valueOf(i));
        }
    }
}
