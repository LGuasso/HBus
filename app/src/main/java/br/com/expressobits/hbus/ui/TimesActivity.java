package br.com.expressobits.hbus.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.ui.adapters.ViewPagerAdapter;
import br.com.expressobits.hbus.ui.dialog.ChooseCodeFilterFragment;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Activity with Fragment
 */
public class TimesActivity extends AppCompatActivity implements ChooseCodeFilterFragment.OnConfirmFilterCodes{

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

    private String[] codes;
    private boolean[] codesChecked;

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



        ScheduleDAO dao = new ScheduleDAO(this,country,city);
        List<Code> codes = dao.getCodes(company,itinerary);
        this.codes = new String[codes.size()];
        this.codesChecked = new boolean[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            this.codes[i] = codes.get(i).getName();
            this.codesChecked[i] = true;
        }

        toolbar.setTitle(itinerary);
        toolbar.setSubtitle(way);
        textViewCompanyUse.setText(getString(R.string.company_use,company));
        viewPagerAdapter.refresh(country,city,company,itinerary,way);
        //TODO implement sunday days in holiday
        int typeday  = TimeUtils.getTypedayinCalendar(Calendar.getInstance()).toInt();
        viewPager.setCurrentItem(typeday);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.menu_action_filter) {
            openChooseCodeFilter();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openChooseCodeFilter(){
        ChooseCodeFilterFragment chooseCodeFilterFragment = new ChooseCodeFilterFragment();
        chooseCodeFilterFragment.setParameters(this,codes,codesChecked);
        chooseCodeFilterFragment.show(getSupportFragmentManager(),TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_times, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfirmFilter(String[] codes, boolean[] codesChecked) {
        this.codes = codes;
        this.codesChecked = codesChecked;
        refresh();
    }
}
