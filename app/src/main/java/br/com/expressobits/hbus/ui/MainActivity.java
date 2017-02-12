package br.com.expressobits.hbus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.net.Uri;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.alarm.AlarmListFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.fragments.CompaniesFragment;
import br.com.expressobits.hbus.ui.fragments.FavoritesItineraryFragment;
import br.com.expressobits.hbus.ui.fragments.ItinerariesFragment;
import br.com.expressobits.hbus.ui.news.NewsFragment;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.ui.help.HelpActivity;
import br.com.expressobits.hbus.ui.login.LoginActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.settings.SettingsActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity implements OnSettingsListener,
        ChooseWayDialogListener,NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    public static final String TAG = "Atividade Principal";
    public static final String DEBUG = "debug";
    public static final String STACK = "pilha";
    public Toolbar pToolbar;
    private NavigationView navigationView;
    //Gerencia a atuacao dos fragments
    FragmentManager fragmentManager = getSupportFragmentManager();
    TextView textViewCompanyUse;

    ImageView imageViewCity;
    ImageView circleImageViewCityProfile;
    TextView textViewCityName;
    private String country;
    private String city;
    private String company;
    private String itinerary;
    private String way;
    private boolean isDualPane;
    public InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadParams();
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

    private void loadParams() {
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        try {
            city = FirebaseUtils.getCityName(cityId);
            country = FirebaseUtils.getCountry(cityId);
            Log.e(TAG,country);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference cityTableRef = database.getReference(FirebaseUtils.CITY_TABLE);
            DatabaseReference countryRef = cityTableRef.child(country);
            DatabaseReference cityRef = countryRef.child(city);
            cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    City city = dataSnapshot.getValue(City.class);
                    company = city.getCompanyDefault();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(SelectCityActivity.TAG);
            editor.apply();

            Toast.makeText(this,getString(R.string.error_load_city),Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this,SelectCityActivity.class));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadParams();
        refresh();
        initNavigationDrawer();

    }

    public void refresh(){
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        textViewCityName.setText(FirebaseUtils.getCityName(cityId));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FirebaseUtils.REF_STORAGE_HBUS);
        StorageReference tableRef = storageRef.child(FirebaseUtils.CITY_TABLE);
        StorageReference countryRef = tableRef.child(country);
        StorageReference cityRef = countryRef.child(city.toLowerCase().replace(" ","_")+FirebaseUtils.EXTENSION_IMAGE);

        StorageReference cityFlagRef = countryRef.child(city.toLowerCase().replace(" ","_")
                +FirebaseUtils.FLAG_TEXT_FILE+FirebaseUtils.EXTENSION_IMAGE);

        cityRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(MainActivity.this).load(uri)
                        .into(imageViewCity);
            }


        });

        cityFlagRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(MainActivity.this).load(uri)
                        .placeholder(R.drawable.ic_flag_white_48dp)
                        .error(R.drawable.ic_flag_white_48dp)
                        .into(circleImageViewCityProfile);
            }
        });
        textViewCompanyUse.setText(getString(R.string.company_use,PreferenceManager.getDefaultSharedPreferences(this).getString(cityId,"")));
    }



    private void initViews() {
        initActionBar();
        initNavigationDrawer();
        initAdInterstitial();
        textViewCompanyUse = (TextView) findViewById(R.id.textCompanyUse);
    }

    private void initAdInterstitial(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersticial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                openTimes(country, city, company, itinerary, way);
            }

        });
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.tablet_test_device_id))
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
        /**TODO mostrar empresa selecionada no menu com companies */
        naviView.setOnClickListener(this);
        TextView textViewUserName = (TextView)naviHeader.findViewById(R.id.textViewUserName);
        TextView textViewUserEmail = (TextView)naviHeader.findViewById(R.id.textViewUserEmail);
        ImageView imageViewUserProfile = (ImageView) naviHeader.findViewById(R.id.imageViewUserProfile);


        textViewCityName = (TextView) naviHeader.findViewById(R.id.textViewCityName);
        circleImageViewCityProfile = (ImageView) naviHeader.findViewById(R.id.imageViewCityProfile);
        imageViewCity = (ImageView) naviHeader.findViewById(R.id.imageViewCity);
        imageViewCity.setOnClickListener(this);


        //textViewCityName.setText(city + " - " + country);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(userName!=null && !userName.isEmpty()){
            textViewUserName.setText(userName);
            Uri uriImage = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
            Log.d(TAG,"Uri image"+uriImage);
            Picasso.with(this).load(uriImage)
                    .placeholder(R.drawable.ic_account_white_48dp)
                    .error(R.drawable.ic_account_white_48dp)
                    .into(imageViewUserProfile);
        }else {
            textViewUserName.setText(getString(R.string.anonymous));
            imageViewUserProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_outline_white_48dp));
        }

        textViewUserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
            startActivity(new Intent(this, SettingsActivity.class));
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
    public void onSettingsDone(String company,String itinerary, String way) {
        this.company = company;
        this.itinerary = itinerary;
        this.way = way;
        if(showAdIntersticial()){

        }else {
            openTimes(country, city, company, itinerary, way);
        }

    }

    private void openTimes(String country, String city, String company, String itinerary, String way) {
        registerEvent(itinerary, way);
        if (isDualPane) {
            /*pToolbar.setTitle(itinerary);
            pToolbar.setSubtitle(way);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            *//** Se for acessodado de um smartphone o espaco main existir *//*
            *//** Adiciona o fragment com o novo conteudo no unico espaco *//*
            OnibusFragment onibusFragment = (OnibusFragment) fragmentManager.findFragmentByTag("onibusFragment");

            if (findViewById(R.id.framelayout_main) != null) {

                if (onibusFragment != null) {
                    onibusFragment.refresh(country, city, company, itinerary, way);
                } else {
                    onibusFragment = new OnibusFragment();
                    Bundle args = new Bundle();
                    args.putString(OnibusFragment.ARGS_COUNTRY, country);
                    args.putString(OnibusFragment.ARGS_CITY, city);
                    args.putString(OnibusFragment.ARGS_COMPANY, company);
                    args.putString(OnibusFragment.ARGS_ITINERARY, itinerary);
                    args.putString(OnibusFragment.ARGS_WAY, way);
                    onibusFragment.setArguments(args);
                    // Troca o que quer que tenha na view do fragment_container por este fragment,
                    // e adiciona a transa��o novamente na pilha de navega��o
                    ft.replace(R.id.framelayout_main, onibusFragment, OnibusFragment.TAG);
                    ft.addToBackStack("pilha");
                }
            } else if (findViewById(R.id.framelayout_content) != null) {
                if (onibusFragment != null) {
                    onibusFragment.refresh(country, city, company, itinerary, way);
                } else {
                    onibusFragment = new OnibusFragment();
                    Bundle args = new Bundle();
                    args.putString(OnibusFragment.ARGS_COUNTRY, country);
                    args.putString(OnibusFragment.ARGS_CITY, city);
                    args.putString(OnibusFragment.ARGS_COMPANY, company);
                    args.putString(OnibusFragment.ARGS_ITINERARY, itinerary);
                    args.putString(OnibusFragment.ARGS_WAY, way);
                    onibusFragment.setArguments(args);
                    // Troca o que quer que tenha na view do fragment_container por este fragment,
                    // e adiciona a transacao novamente na pilha de navegacao
                    ft.replace(R.id.framelayout_content, onibusFragment, OnibusFragment.TAG);
                }
            }
            ft.commit();*/
        } else {
            Intent intent = new Intent(this, TimesActivity.class);
            intent.putExtra(TimesActivity.ARGS_COUNTRY, country);
            intent.putExtra(TimesActivity.ARGS_CITY, city);
            intent.putExtra(TimesActivity.ARGS_COMPANY, company);
            intent.putExtra(TimesActivity.ARGS_ITINERARY, itinerary);
            intent.putExtra(TimesActivity.ARGS_WAY, way);
            startActivity(intent);
        }
    }

    /**
     * https://support.google.com/firebase/answer/6317508?hl=en&ref_topic=6317484
     * @param itinerary
     * @param way
     */
    private void registerEvent(String itinerary, String way) {
        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, way);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itinerary);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ItineraryContract.Itinerary.TABLE_NAME);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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
            case NewsFragment.TAG:
                navigationView.getMenu().findItem(R.id.nav_news).setChecked(true);
                break;
            case CompaniesFragment.TAG:
                navigationView.getMenu().findItem(R.id.nav_companies).setChecked(true);
                break;
            case AlarmListFragment.TAG:
                navigationView.getMenu().findItem(R.id.nav_alarms).setChecked(true);
                break;
        }
    }

    /**
     *
     * @param TAG
     */
    public void addFragment(String TAG) {
        Fragment fragment = new Fragment();
        setSelectItemNavigation(TAG);
        switch (TAG){
            case FavoritesItineraryFragment.TAG:
                setActionBarTitle(getString(R.string.app_name));
                if(getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(AlarmListFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(CompaniesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case ItinerariesFragment.TAG:
                fragment = new ItinerariesFragment();
                setActionBarTitle(getString(R.string.itineraries));
                if(getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(AlarmListFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(CompaniesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case CompaniesFragment.TAG:
                fragment = new CompaniesFragment();
                setActionBarTitle(getString(R.string.companies));
                if(getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(AlarmListFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case AlarmListFragment.TAG:
                fragment = new AlarmListFragment();
                pToolbar.setTitle(getString(R.string.alarms));
                if(getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(CompaniesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case NewsFragment.TAG:
                fragment = new NewsFragment();
                setActionBarTitle(getString(R.string.news));
                if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(AlarmListFragment.TAG)!=null){
                    getSupportFragmentManager().popBackStack();
                }
                if(getSupportFragmentManager().findFragmentByTag(CompaniesFragment.TAG)!=null){
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
        outState.putString(OnibusFragment.ARGS_COUNTRY, country);
        outState.putString(OnibusFragment.ARGS_CITY, city);
        outState.putString(OnibusFragment.ARGS_COMPANY, company);
        outState.putString(OnibusFragment.ARGS_ITINERARY, itinerary);
        outState.putString(OnibusFragment.ARGS_WAY, way);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setActionBarTitle() {
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        pToolbar.setTitle("HBus");
        pToolbar.setSubtitle(FirebaseUtils.getCityName(cityId) + " - " + FirebaseUtils.getCountry(cityId));
    }

    public void onCreateDialogChooseWay(Itinerary itinerary) {
        List<String> ways;
        String company = FirebaseUtils.getCompany(itinerary.getId());
        try {
            ways = itinerary.getWays();
            if (ways.size() > 1) {
                ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
                chooseWayDialogFragment.setParameters(this,company,itinerary.getName(), ways);
                chooseWayDialogFragment.show(MainActivity.this.getSupportFragmentManager(), ChooseWayDialogFragment.TAG);
            } else {
                onSettingsDone(company,itinerary.getName(), ways.get(0));
            }
        }catch (SQLiteCantOpenDatabaseException exception){
            Toast.makeText(this,"Ocorreu um erro no servidor,tente novamente...",Toast.LENGTH_LONG).show();
        }
    }

    public void openHelp(){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void setActionBarTitle(String title){
        pToolbar.setTitle(title);
    }


    @Override
    public void onItemClick(String company,String itinerary, String way) {
        onSettingsDone(company,itinerary, way);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
        }else if (id == R.id.nav_favorites) {
            addFragment(FavoritesItineraryFragment.TAG);
        } else if (id == R.id.nav_all_itineraries) {
            addFragment(ItinerariesFragment.TAG);
        }else if (id == R.id.nav_alarms) {
            addFragment(AlarmListFragment.TAG);
        }else if (id == R.id.nav_companies) {
            addFragment(CompaniesFragment.TAG);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_news){
            addFragment(NewsFragment.TAG);
        } else if(id == R.id.nav_help){
            openHelp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewCity:
                startActivity(new Intent(MainActivity.this,SelectCityActivity.class));
                break;
        }
    }
}
