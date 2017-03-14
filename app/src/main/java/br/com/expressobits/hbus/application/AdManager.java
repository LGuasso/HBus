package br.com.expressobits.hbus.application;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import br.com.expressobits.hbus.R;

/**
 * Class Manager Ads
 * @author Rafael Correa
 * @since 24/02/17
 */

public class AdManager {

    private static InterstitialAd mInterstitialAd;


    static void setAdListener(AdListener adListener){
        mInterstitialAd.setAdListener(adListener);
    }

    /**
     * Init settings of Interstitial
     * @param context Context of activity or fragment
     */
    public static void initAdInterstitial(Context context){
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.intersticial_ad_unit_id));
        requestNewInterstitial(context);
    }


    /**
     * Request new intertistial information
     * @param context Context of activity or fragment
     */
    private static void requestNewInterstitial(Context context) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(context.getString(R.string.tablet_test_device_id))
                .addTestDevice(context.getString(R.string.cel_motorola_xt_1089_test_device_id))
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    /**
     * Show interstitial ad
     * @param context Context of activity or fragment
     * @return return true if ad is loaded and count time manager is true
     */
    static boolean showAdIntersticial(Context context) {
        if (mInterstitialAd.isLoaded() && AppManager.countTimesActivity(context)) {
            mInterstitialAd.show();
            return true;
        } else {
            return false;
        }
    }


}
