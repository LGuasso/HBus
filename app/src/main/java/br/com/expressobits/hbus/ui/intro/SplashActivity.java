package br.com.expressobits.hbus.ui.intro;

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
 * Tela de intro(splash)
 * <p>Esta tela apresenta várias instâncias de um mesmo objeto de <link>Fragment</link>
 * que exibem informações diferentes com mesmos views</p>
 * @author Rafael Correa
 * @since 27/09/15
 */
public class SplashActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = "Splash";
    private ViewPager defaultViewpager;
    private ContentPagerAdapter defaultPagerAdapter;

    private Button jumpButton;
    private Toolbar mToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        referencesLayoutXML();

        showCircleIndicatorDefault();

        setSupportActionBar(mToolbarBottom);

        setupToolbarBottom();
    }

    private void referencesLayoutXML() {
        mToolbarBottom = (Toolbar) findViewById(R.id.inc_splash_toolbar);
        jumpButton = (Button) mToolbarBottom.findViewById(R.id.previousText);
    }

    private void setupToolbarBottom() {
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        });
    }

    private void showCircleIndicatorDefault() {
        defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        defaultViewpager.setOffscreenPageLimit(2);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(PagerFragment.newInstance(1, 0, "HBUS", "A hora nas suas mãos!", R.drawable.modelo));
        fragments.add(PagerFragment.newInstance(1, 0, "Comodidade", "Encontre a linha urbana favorita! ", R.mipmap.ic_launcher));
        fragments.add(PagerFragment.newInstance(1, 0, "Fácil", "Com poucos toques utilize o máximo!", R.mipmap.ic_launcher));
        fragments.add(PagerFragment.newInstance(1, 0, "Ajude", "Ainda em fase de testes, este aplicativo vai crescer com sua ajuda e feedback!", R.mipmap.ic_launcher));

        defaultPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(), fragments);
        defaultViewpager.setAdapter(defaultPagerAdapter);

        defaultViewpager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(), fragments));

        defaultIndicator.setViewPager(defaultViewpager);
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
        /*switch (item.getItemId()) {
            case R.id.action_next:
                Log.i(TAG, "defaultViewpager.getCurrentItem(): " + defaultViewpager.getCurrentItem());
                if (defaultPagerAdapter.getCount() == defaultViewpager.getCurrentItem() + 1) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                defaultViewpager.setCurrentItem(defaultViewpager.getCurrentItem() + 1);
                return true;
        }
        return super.onOptionsItemSelected(item);*/
        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
