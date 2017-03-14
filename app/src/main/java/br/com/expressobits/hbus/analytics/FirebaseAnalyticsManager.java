package br.com.expressobits.hbus.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.expressobits.hbus.BuildConfig;
import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Class manager firebase analytics events logs and user propriets
 * @author Rafael Correa
 * @since 18/02/17
 */

public class FirebaseAnalyticsManager {
    /*
     * https://support.google.com/firebase/answer/6317508?hl=en&ref_topic=6317484
     */
    public static void registerEventItinerary(Context context, String country, String city, String company, String itinerary) {
        if(!BuildConfig.DEBUG){
            // Obtain the FirebaseAnalytics instance.
            com.google.firebase.analytics.FirebaseAnalytics mFirebaseAnalytics;
            mFirebaseAnalytics = com.google.firebase.analytics.FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString(com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID, country+"/"+city+"/"+company+"/"+itinerary);
            bundle.putString(com.google.firebase.analytics.FirebaseAnalytics.Param.CONTENT_TYPE, ItineraryContract.Itinerary.TABLE_NAME);
            mFirebaseAnalytics.logEvent(com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    public static void registerEventSearchItinerary(Context context, String country, String city, String searchTerm) {
        if(!BuildConfig.DEBUG){
            // Obtain the FirebaseAnalytics instance.
            FirebaseAnalytics mFirebaseAnalytics;
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, country+"/"+city+"/"+searchTerm);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
        }
    }



    /**
     * Register select content type event with params country and itinerary
     * @param country Region with country
     * @param city  Name of city
     */
    public static void registerEventCity(Context context,String country, String city) {
        if(!BuildConfig.DEBUG){
            // Obtain the FirebaseAnalytics instance.
            FirebaseAnalytics mFirebaseAnalytics;
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FirebaseUtils.getIdCity(country,city));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, city);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CityContract.City.TABLE_NAME);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }


    public static void registerEventTutorialBegin(Context context) {
        if(!BuildConfig.DEBUG){
            // Obtain the FirebaseAnalytics instance.
            FirebaseAnalytics mFirebaseAnalytics;
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN,bundle);
        }
    }

    public static void registerEventTutorialComplete(Context context) {
        if(!BuildConfig.DEBUG){
            // Obtain the FirebaseAnalytics instance.
            FirebaseAnalytics mFirebaseAnalytics;
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE,bundle);
        }
    }
}
