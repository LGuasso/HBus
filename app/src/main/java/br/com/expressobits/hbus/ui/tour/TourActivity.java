package br.com.expressobits.hbus.ui.tour;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.MainActivity;
import me.relex.circleindicator.CircleIndicator;

/**
 * Tela de tour(intro)
 * <p>Esta tela apresenta várias instâncias de um mesmo objeto de <link>Fragment</link>
 * que exibem informações diferentes com mesmos views</p>
 * @author Rafael Correa
 * @since 27/09/15
 */
public class TourActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = "Tour";
    private ViewPager defaultViewpager;
    private ContentPagerAdapter defaultPagerAdapter;

    private Button jumpButton;
    private Button nextButton;
    private Toolbar mToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                startActivity(new Intent(TourActivity.this,MainActivity.class));
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (defaultPagerAdapter.getCount() == defaultViewpager.getCurrentItem() + 1) {
                    startActivity(new Intent(TourActivity.this, MainActivity.class));
                }
                if(defaultViewpager.getCurrentItem() + 2 == defaultPagerAdapter.getCount()){
                    nextButton.setText(getResources().getText(R.string.finish));
                }else{
                    nextButton.setText(getResources().getText(R.string.next));
                }

                defaultViewpager.setCurrentItem(defaultViewpager.getCurrentItem() + 1);
            }
        });
    }

    private void showCircleIndicatorDefault() {
        defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        defaultViewpager.setOffscreenPageLimit(2);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        ArrayList<Fragment> fragments = new ArrayList<>();
        //TODO para resources este dados
        fragments.add(PagerFragment.newInstance(1, 0, "HBus", "A hora nas suas mãos!\nEncontre a linha urbana favorita!", R.drawable.modelo));
        fragments.add(PagerFragment.newInstance(1, 0, "Fácil de usar!", "Com poucos toques utilize o máximo!\nCom favoritos que exibem já os próximos horários!", R.mipmap.ic_launcher));
        fragments.add(PagerFragment.newInstance(1, 0, "Ajude-nos!", "Ainda em fase de testes, este aplicativo vai crescer com sua ajuda e feedback!", R.mipmap.ic_launcher));

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
            nextButton.setText(getResources().getText(R.string.next));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
