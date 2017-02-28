package br.com.expressobits.hbus.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.adapters.ViewPagerAdapter;
import br.com.expressobits.hbus.utils.TimeUtils;

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

    private TextView textViewCompanyUse;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * Initialize all views
     */
    public void initViews(){
        initAdView();
        initToolbar();
        initTabLayout();
    }

    /**
     * Initialize ad view
     */
    public void initAdView(){
        AdView mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.tablet_test_device_id))
                .addTestDevice(getString(R.string.cel_motorola_xt_1089_test_device_id))
                .build();

        mAdView.loadAd(adRequest);
    }

    /**
     * Initialize toolbar view
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (findViewById(R.id.appBar) != null) {
                (findViewById(R.id.appBar)).setElevation(4);
            }
        }
        textViewCompanyUse = (TextView) findViewById(R.id.textCompanyUse);
    }

    /**
     * Initialize tab layout with view pager
     * @see ViewPagerAdapter
     */
    private void initTabLayout(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Refresh all views with data from getIntent()
     */
    public void refresh(){
        String country = getIntent().getStringExtra(ARGS_COUNTRY);
        String city = getIntent().getStringExtra(ARGS_CITY);
        String company = getIntent().getStringExtra(ARGS_COMPANY);
        String itinerary = getIntent().getStringExtra(ARGS_ITINERARY);
        String way = getIntent().getStringExtra(ARGS_WAY);
        toolbar.setTitle(itinerary);
        toolbar.setSubtitle(way);
        textViewCompanyUse.setText(getString(R.string.company_use,company));
        viewPagerAdapter.refresh(country,city,company,itinerary,way);
        //TODO implement sunday days in holiday
        int typeday  = TimeUtils.getTypedayinCalendar(Calendar.getInstance()).toInt();
        viewPager.setCurrentItem(typeday);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
