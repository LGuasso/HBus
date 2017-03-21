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

import java.util.ArrayList;
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

    private List<Code> codes;
    private List<Code> codesNoSelected;
    private String country;
    private String city;
    private String company;
    private String itinerary;
    private String way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadParams();
        loadCodes();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        initViews();
    }

    /**
     * Load codes in memory from database
     */
    private void loadCodes() {
        ScheduleDAO dao = new ScheduleDAO(this, country, city);
        codes = dao.getCodes(company, itinerary);
        codesNoSelected = new ArrayList<>();
    }

    /**
     * Load args params from intent activity
     */
    private void loadParams() {
        country = getIntent().getStringExtra(ARGS_COUNTRY);
        city = getIntent().getStringExtra(ARGS_CITY);
        company = getIntent().getStringExtra(ARGS_COMPANY);
        itinerary = getIntent().getStringExtra(ARGS_ITINERARY);
        way = getIntent().getStringExtra(ARGS_WAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh(getCharSequenceArrayList(codesNoSelected));
        //TODO implement sunday days in holiday
        int typeday  = TimeUtils.getTypedayinCalendar(Calendar.getInstance()).toInt();
        viewPager.setCurrentItem(typeday);
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
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Refresh all views with data from getIntent()
     */
    public void refresh(ArrayList<String> codesName){
        loadParams();
        toolbar.setTitle(itinerary);
        toolbar.setSubtitle(way);
        textViewCompanyUse.setText(getString(R.string.company_use,company));
        viewPagerAdapter.refresh(country,city,company,itinerary,way,codesName);
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

    /**
     * Open alert dialog for filter codes in Bus list
     */
    private void openChooseCodeFilter(){
        ChooseCodeFilterFragment chooseCodeFilterFragment = new ChooseCodeFilterFragment();
        chooseCodeFilterFragment.setParameters(this,codes, codesNoSelected);
        chooseCodeFilterFragment.show(getSupportFragmentManager(),TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_times, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfirmFilter(List<Code> codesNoSelected) {
        this.codesNoSelected = codesNoSelected;
        refresh(getCharSequenceArrayList(codesNoSelected));
    }

    /**
     * Transform codes object list in string name list
     * @param codesSelected Codes list
     * @return ArrayList of name codes
     */
    private ArrayList<String> getCharSequenceArrayList(List<Code> codesSelected){
        ArrayList<String> codesNamesSelected = new ArrayList<>();
        for(Code code:codesSelected){
            codesNamesSelected.add(code.getName());
        }
        return codesNamesSelected;
    }
}
