package br.com.expressobits.hbus.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.io.File;

import br.com.expressobits.hbus.BuildConfig;
import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BusDAO;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author  Rafael
 * @since  15/09/15.
 * Classe que gerencia processos inicias do sistema
 */
public class TPCDataBaseInit extends Application{

    public static final String TAG = "TPCDataBaseInit";
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {


        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "pmozmo0oSDUsmX5xWxIjqG1ZRKlT5EDMdld9cMyE", "XKZQXJhT9MIERWpmAYBIj2qAWdsx9oOZ7fpsYNdr");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int version = pInfo.versionCode;
        if(56> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            clearApplicationData(this);
        }
        if(59> PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            this.deleteDatabase("santa_maria_rs_bus_data.db");
        }
        if(pInfo.versionCode > PreferenceManager.getDefaultSharedPreferences(this).getInt("version",0)){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("version",pInfo.versionCode);
            editor.apply();
        }

        if(!BuildConfig.DEBUG){
            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            tracker = analytics.newTracker(getString(R.string.analytics_id_tracker)); // Replace with actual tracker/property Id
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
        }


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        //.setDefaultFontPath("fonts/Ubuntu-L.ttf")
                        //.setDefaultFontPath("fonts/proximanova_regular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
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
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
