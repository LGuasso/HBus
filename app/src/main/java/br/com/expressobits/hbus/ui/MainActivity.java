package br.com.expressobits.hbus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import br.com.expressobits.hbus.application.AdManager;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.messaging.ClickActionHelper;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.alarm.AlarmListFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.fragments.CompaniesFragment;
import br.com.expressobits.hbus.ui.fragments.HomeFragment;
import br.com.expressobits.hbus.ui.fragments.ScheduleFragment;
import br.com.expressobits.hbus.ui.fragments.ItinerariesFragment;
import br.com.expressobits.hbus.ui.help.HelpActivity;
import br.com.expressobits.hbus.ui.login.LoginActivity;
import br.com.expressobits.hbus.ui.news.NewsFragment;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.settings.SettingsActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/* *************************************************************************************************
~ Juramento do Programador no desenvolvimento de Código Limpo:
~
~  Antes de codificar, me colocarei na posição dos outros colaboradores desenvolvedores,
~  buscando me expressar de maneira simples, logo:
~
~  - Nomearei as entidades como classes, métodos e variáveis com nomes significativos, pronunciáveis
~  e pesquisável, que revelem a sua verdadeira e atual intenção;
~
~  - Farei com que cada método e cada deve ter apenas uma única responsabilidade, caso contrário,
~  deve ser refatorados em métodos unitários;
~
~  - Ao comentar sobre uma entidade, deixarei claro qual é o seu papel atual e sugerirei  melhorias
~  futuras, se for o caso; Atualizar comentários sempre quando atualizar código;
~ **************************************************************************************************/

public class MainActivity extends AppCompatActivity implements FragmentManagerListener,
        ChooseWayDialogListener,NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {


    private static final String TAG = "MainActivity";
    private static final String STACK = "stack";
    public static final String DEBUG = "debug";
    private Toolbar pToolbar;
    private NavigationView navigationView;
    //Gerencia a atuacao dos fragments
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TextView textViewCompanyUse;

    private ImageView imageViewCity;
    private ImageView circleImageViewCityProfile;
    private TextView textViewCityName;
    private String country;
    private String city;
    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        checkIntent(getIntent());
        setContentView(R.layout.activity_main);

        loadParams();
        if (savedInstanceState == null) {
            Fragment fragment = new HomeFragment();
            if (findViewById(R.id.framelayout_main) != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.framelayout_main, fragment, HomeFragment.TAG);
                ft.commit();
            }
        }
        initViews();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);
    }

    private void checkIntent(Intent intent) {
        if (intent.hasExtra("click_action")) {
            ClickActionHelper.startActivity(intent.getStringExtra("click_action"), intent.getExtras(), this);
        }
    }

    private void loadParams() {
        String cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        city = FirebaseUtils.getCityName(cityId);
        country = FirebaseUtils.getCountry(cityId);
        try {
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

        cityRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(MainActivity.this).load(uri)
                .into(imageViewCity));

        cityFlagRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(MainActivity.this).load(uri)
                .placeholder(R.drawable.ic_flag_white_48dp)
                .error(R.drawable.ic_flag_white_48dp)
                .into(circleImageViewCityProfile));
        textViewCompanyUse.setText(getString(R.string.company_use,PreferenceManager.getDefaultSharedPreferences(this).getString(cityId,"")));
    }



    private void initViews() {
        initActionBar();
        initNavigationDrawer();
        AdManager.initAdInterstitial(this);
        textViewCompanyUse = (TextView) findViewById(R.id.textCompanyUse);
    }

    private void initNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, pToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View naviHeader = navigationView.getHeaderView(0);
        View naviView = naviHeader.findViewById(R.id.side_nav_bar);
        naviView.setOnClickListener(this);
        TextView textViewUserName = (TextView)naviHeader.findViewById(R.id.textViewUserName);
        TextView textViewUserEmail = (TextView)naviHeader.findViewById(R.id.textViewUserEmail);
        ImageView imageViewUserProfile = (ImageView) naviHeader.findViewById(R.id.imageViewUserProfile);


        textViewCityName = (TextView) naviHeader.findViewById(R.id.textViewCityName);
        circleImageViewCityProfile = (ImageView) naviHeader.findViewById(R.id.imageViewCityProfile);
        imageViewCity = (ImageView) naviHeader.findViewById(R.id.imageViewCity);
        imageViewCity.setOnClickListener(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            String userName = currentUser.getDisplayName();
            textViewUserName.setText(userName);
            Uri uriImage = currentUser.getPhotoUrl();
            Log.d(TAG,"Uri image"+uriImage);
            Picasso.with(this).load(uriImage)
                    .placeholder(R.drawable.ic_account_white_48dp)
                    .error(R.drawable.ic_account_white_48dp)
                    .into(imageViewUserProfile);
            textViewUserEmail.setText(currentUser.getEmail());
        }else{
            textViewUserName.setText(getString(R.string.anonymous));
            imageViewUserProfile.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_outline_white_48dp));
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


    /**
     * Ao abrir qualquer fragment, seta o navigation dawer evitando assim atalhos que não sejam
     * selecionados no navigation
     */
    private void setSelectItemNavigation(String TAG){
        switch (TAG){
            case HomeFragment.TAG:
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

    public void addFragment(String TAG) {
        Fragment fragment = new Fragment();
        setSelectItemNavigation(TAG);
        switch (TAG){
            case HomeFragment.TAG:
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
            }
            // Finaliza a transacao com sucesso
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(ScheduleFragment.ARGS_COUNTRY, country);
        outState.putString(ScheduleFragment.ARGS_CITY, city);
        outState.putString(ScheduleFragment.ARGS_COMPANY, company);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onCreateDialogChooseWay(Itinerary itinerary) {
        List<String> ways;
        String company = FirebaseUtils.getCompany(itinerary.getId());
        ways = itinerary.getWays();
        if (ways.size() > 1) {
            ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
            chooseWayDialogFragment.setParameters(this,country,city,company,itinerary.getName(), ways);
            chooseWayDialogFragment.show(MainActivity.this.getSupportFragmentManager(), ChooseWayDialogFragment.TAG);
        } else {
            AppManager.onSettingsDone(this,country,city,company,itinerary.getName(),ways.get(0));
        }
    }

    private void openHelp(){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void setActionBarTitle(String title){
        pToolbar.setTitle(title);
    }


    @Override
    public void onItemClick(String country,String city,String company,String itinerary, String way) {
        AppManager.onSettingsDone(this,country,city,company,itinerary,way);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
        }else if (id == R.id.nav_favorites) {
            addFragment(HomeFragment.TAG);
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
