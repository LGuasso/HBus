package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.fragments.AddFavoriteFragment;
import br.com.expressobits.hbus.ui.fragments.FavoritesItineraryFragment;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.settings.SettingsActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnSettingsListener,Drawer.OnDrawerItemClickListener,ChooseWayDialogListener {

    public static final String TAG = "Atividade Principal";
    public static final String DEBUG = "debug";
    private Toolbar pToolbar;
    //Navigation Drawer
    private Drawer navigationDrawer;
    //Gerencia a atuação dos fragments
    FragmentManager fm = getSupportFragmentManager();
    String linha;
    String sentido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new LinhaFile(this).init(this);
        if(savedInstanceState == null){

            FavoritesItineraryFragment favoritesItineraryFragment = new FavoritesItineraryFragment();
            if(findViewById(R.id.framelayout_main)!=null){
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.framelayout_main, favoritesItineraryFragment,"linhasFragment");
                ft.commit();
            }else if(findViewById(R.id.framelayout_content)!=null){
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.framelayout_menu, favoritesItineraryFragment,"linhasFragment");
                ft.add(R.id.framelayout_content,new OnibusFragment(),"onibusFragment");
                ft.commit();
            }
        }
        initViews();
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
       onUpdateNavigationDrawer();
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
     *     <li>Mostra este logo como botão de HOME</li>
     * </ul>
     */
    private void initActionBar() {
        pToolbar = (Toolbar) findViewById(R.id.primary_toolbar);
        setSupportActionBar(pToolbar);
    }

    @Override
    public void onSettingsDone(boolean type){
        FragmentTransaction ft = fm.beginTransaction();
        if(!type){
            getSupportFragmentManager().popBackStack();
        }else {
            AddFavoriteFragment addFavoriteFragment;
            if (findViewById(R.id.framelayout_main) != null) {

                addFavoriteFragment = new AddFavoriteFragment();
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_main, addFavoriteFragment, "addFavoriteFragment");
                ft.addToBackStack("pilha");
            } else if (findViewById(R.id.framelayout_content) != null) {
                addFavoriteFragment = new AddFavoriteFragment();
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_content, addFavoriteFragment, "onibusFragment");
            }
        }

        // Finaliza a transção com sucesso
        ft.commit();

    }

    @Override
    public void onRemoveFavorite() {
        onUpdateNavigationDrawer();
    }

    @Override
    public void onAddFavorite() {
        onUpdateNavigationDrawer();
    }

    private void onUpdateNavigationDrawer() {
        navigationDrawer.getDrawerItems().clear();
        navigationDrawer.addItem(new SectionDrawerItem().withName(R.string.favorites));
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("city","").length()>1) {
            BusDAO dao = new BusDAO(this);

            List<Itinerary> itinerarieses = dao.getItineraries(true);
            for (Itinerary itinerary : itinerarieses) {
                String name = "";
                if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DEBUG, false)) {
                    name += itinerary.getId() + " - " + itinerary.getName();
                } else {
                    name += itinerary.getName();
                }
                navigationDrawer.addItem(new PrimaryDrawerItem().withName(name).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_bus)));
            }
            navigationDrawer.addItem(new SectionDrawerItem().withName(R.string.all_lines));
            List<Itinerary> allItinerarios = dao.getItineraries(false);

            for (Itinerary itinerary : allItinerarios) {
                String name = "";
                if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DEBUG, false)) {
                    name += itinerary.getId() + " - " + itinerary.getName();
                } else {
                    name += itinerary.getName();
                }
                navigationDrawer.addItem(new SecondaryDrawerItem().withName(name).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_bus)));
            }
            dao.close();
        }
    }

    @Override
    public void onSettingsDone(String linha, String sentido) {
        this.linha = linha;
        this.sentido = sentido;
        pToolbar.setTitle(linha);
        pToolbar.setSubtitle(sentido);
        FragmentTransaction ft = fm.beginTransaction();
        /** Se for acessodado de um smartphone o espaço main existirá */
        /** Adiciona o fragment com o novo conteúdo no único espaço */
        OnibusFragment onibusFragment = (OnibusFragment)fm.findFragmentByTag("onibusFragment");

        if(findViewById(R.id.framelayout_main)!=null){

            if(onibusFragment != null){
                onibusFragment.refresh(linha, sentido);
            }else{
                onibusFragment = new OnibusFragment();
                Bundle args = new Bundle();
                args.putString(OnibusFragment.ARGS_LINHA,linha);
                args.putString(OnibusFragment.ARGS_SENTIDO, sentido);
                onibusFragment.setArguments(args);
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_main, onibusFragment, "onibusFragment");
                ft.addToBackStack("pilha");

            }

        }else if(findViewById(R.id.framelayout_content)!=null){
            if(onibusFragment != null){
                onibusFragment.refresh(linha, sentido);
            }else{
                onibusFragment = new OnibusFragment();
                Bundle args = new Bundle();
                args.putString(OnibusFragment.ARGS_LINHA,linha);
                args.putString(OnibusFragment.ARGS_SENTIDO, sentido);
                onibusFragment.setArguments(args);
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_content, onibusFragment, "onibusFragment");
            }
        }
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(OnibusFragment.ARGS_LINHA, linha);
        outState.putString(OnibusFragment.ARGS_SENTIDO, sentido);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setActionBarTitle(String title,String subtitle){
        pToolbar.setTitle(title);
        pToolbar.setSubtitle(subtitle);
        String cityname = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG,getString(R.string.not_found_city));
        pToolbar.setTitle(cityname);
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        String selectedItem = "";
        if(iDrawerItem instanceof SecondaryDrawerItem) {
                 selectedItem = ((SecondaryDrawerItem) iDrawerItem).getName();
        }
        if(iDrawerItem instanceof PrimaryDrawerItem) {
            selectedItem = ((PrimaryDrawerItem) iDrawerItem).getName();
        }
        BusDAO dao = new BusDAO(this);

        List<String> ways = dao.getItinerary(selectedItem).getSentidos();
        if(ways.size()>1){
            ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
            chooseWayDialogFragment.setParameters(this,selectedItem,ways);
            chooseWayDialogFragment.show(MainActivity.this.getSupportFragmentManager(),ChooseWayDialogFragment.TAG);
        }else{
            onSettingsDone(selectedItem, Arrays.asList(getResources().getStringArray(R.array.list_sentido_circular)).get(0));
        }
        dao.close();

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_button:
                onSettingsDone(true);
            break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onItemClick(String itinerary,String way) {
        onSettingsDone(itinerary, way);
    }
}
