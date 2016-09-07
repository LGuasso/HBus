package br.com.expressobits.hbus.ui.tour;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.ManagerInit;
import me.relex.circleindicator.CircleIndicator;

/**
 * Tela de tour(intro)
 * <p>Esta tela apresenta várias instâncias de um mesmo objeto de <link>Fragment</link>
 * que exibem informações diferentes com mesmos views</p>
 * @author Rafael Correa
 * @since 27/09/15
 */
public class TourActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    public static final String TAG = "Tour";
    private ViewPager defaultViewpager;
    private ContentPagerAdapter defaultPagerAdapter;
    private boolean starter = false;
    public static final String STARTER_MODE = "starter";
    private Button jumpButton;
    private Button nextButton;
    private Toolbar mToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        starter = getIntent().getBooleanExtra(STARTER_MODE,false);
        Log.e("TESTE",starter + " starter mode");


        setContentView(R.layout.activity_tour);

        referencesLayoutXML();

        showCircleIndicatorDefault();

        setSupportActionBar(mToolbarBottom);

        setupToolbarBottom();
    }


    @Override
    protected void onResume() {
        super.onResume();
        defaultViewpager.setCurrentItem(0);
    }

    private void referencesLayoutXML() {
        mToolbarBottom = (Toolbar) findViewById(R.id.inc_splash_toolbar);
        jumpButton = (Button) mToolbarBottom.findViewById(R.id.previousText);
        nextButton = (Button) mToolbarBottom.findViewById(R.id.nextButton);
    }

    private void setupToolbarBottom() {
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTour();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (defaultPagerAdapter.getCount() == defaultViewpager.getCurrentItem() + 1) {
                    finishTour();


                }
                if(defaultViewpager.getCurrentItem() + 2 == defaultPagerAdapter.getCount()){
                    nextButton.setText(getResources().getText(R.string.finish));
                }else{
                    nextButton.setText(getResources().getText(R.string.action_next));
                }

                defaultViewpager.setCurrentItem(defaultViewpager.getCurrentItem() + 1);
            }
        });
    }

    private void finishTour() {
        if(starter){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TourActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(TourActivity.TAG, false);
            editor.apply();
            ManagerInit.manager(TourActivity.this);
        }else{
            finish();
        }

    }

    private void showCircleIndicatorDefault() {
        defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        defaultViewpager.setOffscreenPageLimit(2);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        ArrayList<Fragment> fragments = new ArrayList<>();
        //TODO para resources este dados
        fragments.add(
                PagerFragment.newInstance(
                        getResources().getColor(R.color.tour_color_palm),
                        0,
                        getString(R.string.tour_title_palm),
                        getString(R.string.tour_subtitle_palm),
                        R.drawable.ic_bus_white_48dp)
        );
        fragments.add(
                PagerFragment.newInstance(
                        getResources().getColor(R.color.tour_color_update),
                        1,
                        getString(R.string.tour_title_update),
                        getString(R.string.tour_subtitle_update),
                        R.drawable.ic_refresh_white_48dp)
        );
        fragments.add(
                PagerFragment.newInstance(
                        getResources().getColor(R.color.tour_color_feedback),
                        2,
                        getString(R.string.tour_title_feedback),
                        getString(R.string.tour_subtitle_feedback),
                        R.drawable.ic_information_white_48dp)
        );
        //fragments.add(PagerFragment.newInstance(1, 0,"HBus", "A hora nas suas mãos!\nEncontre a linha urbana favorita!", R.drawable.modelo));
        //fragments.add(PagerFragment.newInstance(1, 0, "Fácil de usar!", "Com poucos toques utilize o máximo!\nCom favoritos que exibem já os próximos horários!", R.mipmap.ic_launcher));
        //fragments.add(PagerFragment.newInstance(1, 0, "Ajude-nos!", "Ainda em fase de testes, este aplicativo vai crescer com sua ajuda e feedback!", R.mipmap.ic_launcher));

        defaultPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(), fragments);
        defaultViewpager.setAdapter(defaultPagerAdapter);

        defaultViewpager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(), fragments));

        defaultIndicator.setViewPager(defaultViewpager);
        defaultViewpager.setOnPageChangeListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(defaultViewpager.getCurrentItem() + 1 == defaultPagerAdapter.getCount()){
            nextButton.setText(getResources().getText(R.string.finish));
        }else{
            nextButton.setText(getResources().getText(R.string.action_next));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
