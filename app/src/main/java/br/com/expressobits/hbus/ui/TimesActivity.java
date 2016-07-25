package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.utils.DAOUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Activity with Fragment
 */
public class TimesActivity extends AppCompatActivity {

    public static final String TAG = "TimesActivity";
    public static final String ARGS_COUNTRY = "country";
    public static final String ARGS_CITY = "city";
    public static final String ARGS_COMPANY = "company";
    public static final String ARGS_ITINERARY = "itinerary";
    public static final String ARGS_WAY = "way";
    //Gerencia a atuacao dos fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        Toolbar pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (((AppBarLayout)findViewById(R.id.appBar)) != null) {
                ((AppBarLayout)findViewById(R.id.appBar)).setElevation(0);
            }
        }
        getSupportActionBar().setTitle(getIntent().getStringExtra(ARGS_ITINERARY));
        pToolbar.setSubtitle(getIntent().getStringExtra(ARGS_WAY));
        initViews();
    }


    public void initViews(){
        OnibusFragment onibusFragment = new OnibusFragment();
        Bundle args = new Bundle();
        args.putString(OnibusFragment.ARGS_COUNTRY,getIntent().getStringExtra(ARGS_COUNTRY));
        args.putString(OnibusFragment.ARGS_CITY,getIntent().getStringExtra(ARGS_CITY));
        args.putString(OnibusFragment.ARGS_COMPANY,getIntent().getStringExtra(ARGS_COMPANY));
        args.putString(OnibusFragment.ARGS_ITINERARY, getIntent().getStringExtra(ARGS_ITINERARY));
        args.putString(OnibusFragment.ARGS_WAY, getIntent().getStringExtra(ARGS_WAY));
        onibusFragment.setArguments(args);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.framelayout_times, onibusFragment, OnibusFragment.TAG);
        ft.commit();
        initAdView();
    }


    public void initAdView(){
        AdView mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}
