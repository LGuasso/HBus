package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.utils.DAOUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Activity with Fragment
 */
public class TimesActivity extends AppCompatActivity {

    private Toolbar pToolbar;

    public static final String ARGS_CITYID = "cityId";
    public static final String ARGS_ITINERARYID = "itineraryId";
    public static final String ARGS_WAY = "Way";

    //Gerencia a atuacao dos fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(DAOUtils.getNameItinerary(getIntent().getStringExtra(ARGS_ITINERARYID)));
        pToolbar.setSubtitle(getIntent().getStringExtra(ARGS_WAY));

        initViews();
    }

    public void initViews(){
        OnibusFragment onibusFragment = new OnibusFragment();
        Bundle args = new Bundle();
        args.putString(OnibusFragment.ARGS_CITYID,getIntent().getStringExtra(ARGS_CITYID));
        args.putString(OnibusFragment.ARGS_ITINERARYID, getIntent().getStringExtra(ARGS_ITINERARYID));
        args.putString(OnibusFragment.ARGS_WAY, getIntent().getStringExtra(ARGS_WAY));
        onibusFragment.setArguments(args);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.framelayout_times, onibusFragment, OnibusFragment.TAG);
        ft.commit();
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
