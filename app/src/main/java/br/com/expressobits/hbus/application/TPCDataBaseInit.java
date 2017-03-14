package br.com.expressobits.hbus.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;

import br.com.expressobits.hbus.BuildConfig;
import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;

/**
 * @author  Rafael Correa
 * @since  15/09/15.
 * Classe que gerencia processos inicias do sistema
 */
public class TPCDataBaseInit extends Application{

    public static final String TAG = "TPCDataBaseInit";
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(100> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0) && 0<PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            Log.d(TAG,"NAO DEVE APARECER ISSO!");
            clearApplicationData(this);
        }
        if(59> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            this.deleteDatabase("santa_maria_rs_bus_data.db");
        }
        if(83> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            this.deleteDatabase("bus_database.db");
            Log.e(TAG, "DELETE bus_database");
        }
        if(86> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            this.deleteDatabase("bus_data.db");
            Log.e(TAG, "DELETE BUS DATA");
        }
        if(94> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(SelectCityActivity.TAG);
            editor.apply();

        }
        if (pInfo != null) {
            if(pInfo.versionCode > PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("version",pInfo.versionCode);
                editor.apply();
            }
        }

        if(!BuildConfig.DEBUG){
            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            tracker = analytics.newTracker(getString(R.string.analytics_id_tracker)); // Replace with actual tracker/property Id
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
        }else{
            FirebaseMessaging.getInstance().subscribeToTopic("test");
            Log.d(TAG, "Subscribe to test topic");
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        super.onCreate();
    }

    /**
     * Call this method to delete any cache created by app
     * @param context context for your application
     */
    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                File f = new File(appDir, s);
                if(deleteDir(f))
                    Log.i(TAG, String.format("**************** DELETED -> (%s) *******************", f.getAbsolutePath()));
            }
        }
    }
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        assert dir != null;
        return dir.delete();
    }
}
