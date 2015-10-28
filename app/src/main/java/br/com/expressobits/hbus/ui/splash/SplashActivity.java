package br.com.expressobits.hbus.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;


import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.tour.TourActivity;

public class SplashActivity extends AppCompatActivity{

    private static final String TAG="SPLASH";
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Class cl = MainActivity.class;

                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, cl);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }




    @Override
    protected void onResume() {
        super.onResume();


    }
}
