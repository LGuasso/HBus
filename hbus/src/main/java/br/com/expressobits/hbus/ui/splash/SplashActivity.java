package br.com.expressobits.hbus.ui.splash;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.ManagerInit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        /* Duration of wait */
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ManagerInit.manager(SplashActivity.this);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }




    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
