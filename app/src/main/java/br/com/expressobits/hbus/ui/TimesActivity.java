package br.com.expressobits.hbus.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;

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
    TextView textViewCompanyUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        Toolbar pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (findViewById(R.id.appBar) != null) {
                (findViewById(R.id.appBar)).setElevation(0);
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
        textViewCompanyUse = (TextView) findViewById(R.id.textCompanyUse);
        textViewCompanyUse.setText(getString(R.string.company_use,getIntent().getStringExtra(ARGS_COMPANY)));
    }


    public void initAdView(){
        AdView mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.tablet_test_device_id))
                .addTestDevice(getString(R.string.cel_motorola_xt_1089_test_device_id))
                .build();

        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}
