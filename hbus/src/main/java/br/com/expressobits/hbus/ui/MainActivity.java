package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.fragments.AddFavoriteFragment;
import br.com.expressobits.hbus.ui.fragments.FavoritesItineraryFragment;
import br.com.expressobits.hbus.ui.fragments.ItinerariesFragment;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.settings.SettingsActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnSettingsListener, Drawer.OnDrawerItemClickListener, ChooseWayDialogListener {

    public static final String TAG = "Atividade Principal";
    public static final String DEBUG = "debug";
    public static final String STACK = "pilha";
    private Toolbar pToolbar;
    //Navigation Drawer
    private Drawer navigationDrawer;
    //Gerencia a atuacao dos fragments
    FragmentManager fragmentManager = getSupportFragmentManager();
    Long itineraryId;
    String way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new LinhaFile(this).init(this);
        if (savedInstanceState == null) {

            FavoritesItineraryFragment favoritesItineraryFragment = new FavoritesItineraryFragment();
            if (findViewById(R.id.framelayout_main) != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.framelayout_main, favoritesItineraryFragment,FavoritesItineraryFragment.TAG);
                ft.commit();
            } else if (findViewById(R.id.framelayout_content) != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.framelayout_menu, favoritesItineraryFragment,FavoritesItineraryFragment.TAG);
                ft.add(R.id.framelayout_content, new OnibusFragment(),OnibusFragment.TAG);
                ft.commit();
            }
        }
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createNavigationDrawer();
    }

    private void initNavigationDrawer() {
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(pToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.START)
                .withSelectedItem(0)
                .withActionBarDrawerToggle(true)
                .withOnDrawerItemClickListener(this)
                .build();
        createNavigationDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return false;
    }

    private void initViews() {
        initActionBar();
        initNavigationDrawer();
    }

    /**
     * Iniciando o actionbar
     * <ul>
     * <li>Mostra este logo como bot�o de HOME</li>
     * </ul>
     */
    private void initActionBar() {
        pToolbar = (Toolbar) findViewById(R.id.primary_toolbar);
        setSupportActionBar(pToolbar);
    }

    @Override
    public void onPopStackBack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRemoveFavorite() {
        createNavigationDrawer();
    }

    @Override
    public void onAddFavorite() {
        createNavigationDrawer();
    }

    private void createNavigationDrawer() {
        TimesDbHelper dao  = new TimesDbHelper(this);
        FavoriteDAO favoriteDAO = new FavoriteDAO(this);
        navigationDrawer.getDrawerItems().clear();
        navigationDrawer.addItem(new SectionDrawerItem());
        navigationDrawer.addItem(new PrimaryDrawerItem()
                .withName(R.string.favorites)
                .withBadge(getString(R.string.number_of_itineraries,
                        favoriteDAO.getItineraries(PreferenceManager.getDefaultSharedPreferences(this).getLong(SelectCityActivity.TAG,0)).size()))
                .withIdentifier(0)
                .withIcon(R.drawable.ic_star_grey600_24dp));
        navigationDrawer.addItem(new PrimaryDrawerItem()
                .withName(R.string.all_lines)
                .withBadge(getString(R.string.number_of_itineraries,dao.getItineraries().size()))
                .withIdentifier(1)
                .withIcon(R.drawable.ic_format_list_bulleted_grey600_24dp));
        navigationDrawer.addItem(new SectionDrawerItem());
        navigationDrawer.addItem(new PrimaryDrawerItem()
                .withName(R.string.action_settings)
                .withIdentifier(2)
                .withIcon(R.drawable.ic_settings_grey600_24dp));

        dao.close();
        favoriteDAO.close();

    }

    @Override
    public void onSettingsDone(Long itineraryId, String sentido) {
        this.itineraryId = itineraryId;
        this.way = sentido;
        TimesDbHelper db = new TimesDbHelper(this);
        pToolbar.setTitle(db.getItinerary(itineraryId).getName());
        db.close();
        pToolbar.setSubtitle(sentido);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        /** Se for acessodado de um smartphone o espaco main existir */
        /** Adiciona o fragment com o novo conteudo no unico espaco */
        OnibusFragment onibusFragment = (OnibusFragment) fragmentManager.findFragmentByTag("onibusFragment");

        if (findViewById(R.id.framelayout_main) != null) {

            if (onibusFragment != null) {
                onibusFragment.refresh(itineraryId, sentido);
            } else {
                onibusFragment = new OnibusFragment();
                Bundle args = new Bundle();
                args.putLong(OnibusFragment.ARGS_LINHA, itineraryId);
                args.putString(OnibusFragment.ARGS_SENTIDO, sentido);
                onibusFragment.setArguments(args);
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transa��o novamente na pilha de navega��o
                ft.replace(R.id.framelayout_main, onibusFragment,OnibusFragment.TAG);
                ft.addToBackStack("pilha");
            }
        } else if (findViewById(R.id.framelayout_content) != null) {
            if (onibusFragment != null) {
                onibusFragment.refresh(itineraryId, sentido);
            } else {
                onibusFragment = new OnibusFragment();
                Bundle args = new Bundle();
                args.putLong(OnibusFragment.ARGS_LINHA, itineraryId);
                args.putString(OnibusFragment.ARGS_SENTIDO, sentido);
                onibusFragment.setArguments(args);
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transacao novamente na pilha de navegacao
                ft.replace(R.id.framelayout_content, onibusFragment,OnibusFragment.TAG);
            }
        }
        ft.commit();
    }

    public void addFragment(String TAG) {
        Fragment fragment = new Fragment();
        if(TAG.equals(ItinerariesFragment.TAG)){
            fragment = new ItinerariesFragment();
        }else if(TAG.equals(AddFavoriteFragment.TAG)){
            fragment = new AddFavoriteFragment();
        }else{

            if(getSupportFragmentManager().findFragmentByTag(ItinerariesFragment.TAG)!=null){
                getSupportFragmentManager().popBackStack();
            }
            if(getSupportFragmentManager().findFragmentByTag(AddFavoriteFragment.TAG)!=null){
                getSupportFragmentManager().popBackStack();
            }
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
        outState.putLong(OnibusFragment.ARGS_LINHA, itineraryId);
        outState.putString(OnibusFragment.ARGS_SENTIDO, way);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setActionBarTitle(String title, String subtitle) {
        pToolbar.setTitle(title);
        pToolbar.setSubtitle(subtitle);
        Long cityId = PreferenceManager.getDefaultSharedPreferences(this).getLong(SelectCityActivity.TAG, 0l);
        String cityname = getString(R.string.not_found_city);
        if(cityId>0l) {
            TimesDbHelper db = new TimesDbHelper(this);
            cityname = db.getCity(cityId).getName();
            db.close();
        }

        pToolbar.setTitle(cityname);
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        //TODO numerar navigationDrawer
        switch (i){
            case 1:
                addFragment(FavoritesItineraryFragment.TAG);
                break;
            case 2:
                addFragment(ItinerariesFragment.TAG);
                break;
            case 4:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        return false;
    }

    public void onCreateDialogChooseWay(Long itineraryId) {
        TimesDbHelper dao = new TimesDbHelper(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_button:
                addFragment(AddFavoriteFragment.TAG);
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onItemClick(Long itineraryId, String way) {
        onSettingsDone(itineraryId, way);
    }
}
