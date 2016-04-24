package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.ui.alarm.AlarmListFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.fragments.FavoritesItineraryFragment;
import br.com.expressobits.hbus.ui.fragments.ItinerariesFragment;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.ui.help.HelpActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.settings.SettingsActivity2;
import br.com.expressobits.hbus.utils.DAOUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements OnSettingsListener,
        ChooseWayDialogListener,NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    public static final String TAG = "Atividade Principal";
    public static final String DEBUG = "debug";
    public static final String STACK = "pilha";
    public Toolbar pToolbar;
    private NavigationView navigationView;
    //Gerencia a atuacao dos fragments
    FragmentManager fragmentManager = getSupportFragmentManager();
    String cityId;
    String itineraryId;
    String way;
    private boolean isDualPane;
    public InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        if (savedInstanceState == null) {

            Fragment fragment = new FavoritesItineraryFragment();




            if (findViewById(R.id.framelayout_main) != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.framelayout_main, fragment,FavoritesItineraryFragment.TAG);
                ft.commit();
            } else if (findViewById(R.id.framelayout_content) != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.framelayout_menu, fragment,FavoritesItineraryFragment.TAG);
                ft.add(R.id.framelayout_content, new OnibusFragment(),OnibusFragment.TAG);
                ft.commit();
                //Define se tela é para dois framgnetos
                isDualPane = true;
            }
        }
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        //TODO "resumir" os views sem ter carregar novamente
        initNavigationDrawer();
    }

    private void initViews() {
        initActionBar();
        initNavigationDrawer();
        initAdInterstitial();
    }

    private void initAdInterstitial(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersticial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                openTimes(itineraryId, way);
            }

        });
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private boolean showAdIntersticial() {
        if (mInterstitialAd.isLoaded() && AppManager.countTimesActivity(this)) {
            mInterstitialAd.show();
            return true;
        } else {
            return false;
        }
    }

    private void initNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, pToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View naviHeader = navigationView.getHeaderView(0);
        View naviView = naviHeader.findViewById(R.id.side_nav_bar);
        naviView.setOnClickListener(this);
        TextView textViewCityName = (TextView)naviHeader.findViewById(R.id.textViewCityName);
        TextView textViewCoutry = (TextView)naviHeader.findViewById(R.id.textViewCountry);
        textViewCityName.setText(DAOUtils.getNameCity(cityId));
        textViewCoutry.setText(DAOUtils.getNameCountry(cityId));

        ImageView imageViewCity = (ImageView)naviHeader.findViewById(R.id.imageViewCity);
        if(cityId.equals("RS/Santa Maria")){
            imageViewCity.setImageDrawable(getResources().getDrawable(R.drawable.santa_maria_rs));
        }
        if(cityId.equals("RS/Cruz Alta")){
            imageViewCity.setImageDrawable(getResources().getDrawable(R.drawable.cruz_alta_rs));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_action_settings) {
            startActivity(new Intent(this, SettingsActivity2.class));
            return true;
        }
        if (id == R.id.menu_action_help) {
            openHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Iniciando o actionbar
     * <ul>
     * <li>Mostra este logo como bot�o de HOME</li>
     * </ul>
     */
    private void initActionBar() {
        pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
    }



    @Override
    public void onSettingsDone(String itineraryId, String sentido) {
        this.itineraryId = itineraryId;
        this.way = sentido;
        if(showAdIntersticial()){

        }else {
            openTimes(itineraryId, sentido);
        }

    }

    private void openTimes(String itineraryId, String sentido) {
        BusDAO db = new BusDAO(this);
        if (isDualPane) {
            pToolbar.setTitle(db.getItinerary(itineraryId).getName());
            db.close();
            pToolbar.setSubtitle(sentido);
            FragmentTransaction ft = fragmentManager.beginTransaction();

            /** Se for acessodado de um smartphone o espaco main existir */
            /** Adiciona o fragment com o novo conteudo no unico espaco */
            OnibusFragment onibusFragment = (OnibusFragment) fragmentManager.findFragmentByTag("onibusFragment");

            if (findViewById(R.id.framelayout_main) != null) {

                if (onibusFragment != null) {
                    onibusFragment.refresh(
                            cityId,
                            itineraryId, sentido);
                } else {
                    onibusFragment = new OnibusFragment();
                    Bundle args = new Bundle();
                    args.putString(OnibusFragment.ARGS_CITYID, cityId);
                    args.putString(OnibusFragment.ARGS_ITINERARYID, itineraryId);
                    args.putString(OnibusFragment.ARGS_WAY, sentido);
                    onibusFragment.setArguments(args);
                    // Troca o que quer que tenha na view do fragment_container por este fragment,
                    // e adiciona a transa��o novamente na pilha de navega��o
                    ft.replace(R.id.framelayout_main, onibusFragment, OnibusFragment.TAG);
                    ft.addToBackStack("pilha");
                }
            } else if (findViewById(R.id.framelayout_content) != null) {
                if (onibusFragment != null) {
                    onibusFragment.refresh(
                            PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY),
                            itineraryId, sentido);
                } else {
                    onibusFragment = new OnibusFragment();
                    Bundle args = new Bundle();
                    args.putString(OnibusFragment.ARGS_CITYID, cityId);
                    args.putString(OnibusFragment.ARGS_ITINERARYID, itineraryId);
                    args.putString(OnibusFragment.ARGS_WAY, sentido);
                    onibusFragment.setArguments(args);
                    // Troca o que quer que tenha na view do fragment_container por este fragment,
                    // e adiciona a transacao novamente na pilha de navegacao
                    ft.replace(R.id.framelayout_content, onibusFragment, OnibusFragment.TAG);
                }
            }
            ft.commit();
        } else {
            Intent intent = new Intent(this, TimesActivity.class);
            intent.putExtra(TimesActivity.ARGS_CITYID, cityId);
            intent.putExtra(TimesActivity.ARGS_ITINERARYID, itineraryId);
            intent.putExtra(TimesActivity.ARGS_WAY, way);
            startActivity(intent);
        }
    }


    /**
     * Ao abrir qualquer fragment, seta o navigation dawer evitando assim atalhos que não sejam
     * selecionados no navigation
     * @param TAG
     */
    public void setSelectItemNavigation(String TAG){
        switch (TAG){
            case FavoritesItineraryFragment.TAG:
                navigationView.getMenu().findItem(R.id.nav_favorites).setChecked(true);
                break;
            case ItinerariesFragment.TAG:
                navigationView.getMenu().findItem(R.id.nav_all_itineraries).setChecked(true);
                break;
            case AlarmListFragment.TAG:
                navigationView.getMenu().findItem(R.id.nav_alarms).setChecked(true);
                break;
        }
    }

    /**
     * S
     * @param TAG
     */
    public void addFragment(String TAG) {
        Fragment fragment = new Fragment();
        switch (TAG){
            case FavoritesItineraryFragment.TAG:
                if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(AlarmListFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case ItinerariesFragment.TAG:
                fragment = new ItinerariesFragment();
                if(getSupportFragmentManager().findFragmentByTag(AlarmListFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case AlarmListFragment.TAG:
                Bundle args = new Bundle();
                args.putString(AlarmListFragment.ARGS_CITYID, cityId);
                fragment = new AlarmListFragment();
                fragment.setArguments(args);
                if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
        if(getSupportFragmentManager().findFragmentByTag(TAG)==null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (findViewById(R.id.framelayout_main) != null) {
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transa��o novamente na pilha de navegacao
                fragmentTransaction.replace(R.id.framelayout_main, fragment,TAG);
                fragmentTransaction.addToBackStack(STACK);
            } else if (findViewById(R.id.framelayout_content) != null) {
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transa��o novamente na pilha de navegacaoo
                fragmentTransaction.replace(R.id.framelayout_content, fragment,TAG);
            }
            // Finaliza a transacao com sucesso
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(OnibusFragment.ARGS_CITYID, cityId);
        outState.putString(OnibusFragment.ARGS_ITINERARYID, itineraryId);
        outState.putString(OnibusFragment.ARGS_WAY, way);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setActionBarTitle() {
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        pToolbar.setTitle("HBus");
        pToolbar.setSubtitle(DAOUtils.getNameCity(cityId) + " - " + DAOUtils.getNameCountry(cityId));
    }

    public void onCreateDialogChooseWay(String itineraryId) {
        BusDAO dao = new BusDAO(this);

        List<String> ways = dao.getItinerary(itineraryId).getWays();
        if (ways.size() > 1) {
            ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
            chooseWayDialogFragment.setParameters(this, itineraryId, ways);
            chooseWayDialogFragment.show(MainActivity.this.getSupportFragmentManager(), ChooseWayDialogFragment.TAG);
        } else {
            onSettingsDone(itineraryId, ways.get(0));
        }
        dao.close();
    }

    public void openHelp(){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    public void setActionBarTitle(String title){
        pToolbar.setTitle(title);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onItemClick(String itineraryId, String way) {
        onSettingsDone(itineraryId, way);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {
            addFragment(FavoritesItineraryFragment.TAG);
        } else if (id == R.id.nav_all_itineraries) {
            addFragment(ItinerariesFragment.TAG);
        }else if (id == R.id.nav_alarms) {
            addFragment(AlarmListFragment.TAG);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity2.class));
        } else if (id == R.id.nav_help) {
            openHelp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.side_nav_bar:
                startActivity(new Intent(MainActivity.this,SelectCityActivity.class));
                break;
        }
    }
}
