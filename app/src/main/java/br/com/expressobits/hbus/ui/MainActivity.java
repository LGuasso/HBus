package br.com.expressobits.hbus.ui;

import android.content.Intent;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import br.com.expressobits.hbus.ui.fragments.AddFavoriteFragment;
import br.com.expressobits.hbus.ui.fragments.FavoritesItineraryFragment;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.settings.SettingsActivity;
import br.com.expressobits.hbus.ui.tour.TourActivity;
import br.com.expressobits.hbus.utils.Popup;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnSettingsListener,Drawer.OnDrawerItemClickListener {

    public static final String TAG = "Atividade Principal";
    public static final String DEBUG = "debug";
    private Toolbar pToolbar;
    //Navigation Drawer
    private Drawer navigationDrawer;


    //Gerencia a atua��o dos fragments
    FragmentManager fm = getSupportFragmentManager();
    int lastPosition = 0;
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
        initNavigationDrawer();
        setVisibleLayout();
    }


    private void initNavigationDrawer() {

        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(pToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSelectedItem(0)
                .withActionBarDrawerToggle(true)
                .withOnDrawerItemClickListener(this)
                .build();
        //TODO atualizar a lista de navigationDrawer no onResume
        navigationDrawer.addItem(new SectionDrawerItem().withName(R.string.favorites));
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("city","").length()>1){
            BusDAO dao = new BusDAO(this);

            List<Itinerary> itinerarieses = dao.getItineraries(true);
            for(Itinerary itinerary : itinerarieses){
                String name = "";
                if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DEBUG,false)){
                    name+=itinerary.getId()+" - "+itinerary.getName();
                }else{
                    name+=itinerary.getName();
                }
                navigationDrawer.addItem(new PrimaryDrawerItem().withName(name).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_bus)));
            }
            navigationDrawer.addItem(new SectionDrawerItem().withName(R.string.all_lines));
            List<Itinerary>allItinerarios = dao.getItineraries(false);

            for(Itinerary itinerary:allItinerarios){
                String name = "";
                if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DEBUG,false)){
                    name+=itinerary.getId()+" - "+itinerary.getName();
                }else{
                    name+=itinerary.getName();
                }
                navigationDrawer.addItem(new SecondaryDrawerItem().withName(name).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_bus)));
            }
        }


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
        //initAdView();

    }

    /**
     * Iniciando o actionbar
     * <ul>
     *     <li>Mostra este logo como bot�o de HOME</li>
     * </ul>
     */
    private void initActionBar() {
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        pToolbar = (Toolbar) findViewById(R.id.primary_toolbar);
        setSupportActionBar(pToolbar);

        //pToolbar.setTitle("My lines");
        //pToolbar.setSubtitle("subtitle");


    }

    /**
     * Gerencia forma dos {@link android.support.v4.app.Fragment}
     */
    public void addOnibusFragment(){
        FragmentTransaction ft = fm.beginTransaction();
        OnibusFragment onibusFragment = (OnibusFragment)fm.findFragmentByTag("onibusFragment");
        if(onibusFragment == null){
            onibusFragment = new OnibusFragment();
        }
        ft.replace(R.id.framelayout_main, onibusFragment, "onibusFragment");
        ft.addToBackStack("pilha");
        ft.commit();
        lastPosition = 1;
    }

    @Override
    public void onSettingsDone(boolean type){
        FragmentTransaction ft = fm.beginTransaction();
        if(!type){
            getSupportFragmentManager().popBackStack();
            //ft.remove(getSupportFragmentManager().findFragmentByTag("addFavoriteFragment"));
        }else {


            AddFavoriteFragment addFavoriteFragment = (AddFavoriteFragment) fm.findFragmentByTag("addFavoriteFragment");
            if (findViewById(R.id.framelayout_main) != null) {

                addFavoriteFragment = new AddFavoriteFragment();
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transa��o novamente na pilha de navega��o
                ft.replace(R.id.framelayout_main, addFavoriteFragment, "addFavoriteFragment");
                ft.addToBackStack("pilha");
                lastPosition = 1;


            } else if (findViewById(R.id.framelayout_content) != null) {
                addFavoriteFragment = new AddFavoriteFragment();
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transa��o novamente na pilha de navega��o
                ft.replace(R.id.framelayout_content, addFavoriteFragment, "onibusFragment");
                lastPosition = 1;

            }
        }

        // Finaliza a trans��o com sucesso
        ft.commit();

    }

    @Override
    public void onSettingsDone(String linha, String sentido) {

        this.linha = linha;
        this.sentido = sentido;



        getSupportActionBar().setTitle(linha);
        getSupportActionBar().setSubtitle(sentido);

        FragmentTransaction ft = fm.beginTransaction();

        /** Se for acessodado de um smartphone o espa�o main existir� */
        /** Adiciona o fragment com o novo conte�do no �nico espa�o */
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
                // e adiciona a transa��o novamente na pilha de navega��o
                ft.replace(R.id.framelayout_main, onibusFragment, "onibusFragment");
                ft.addToBackStack("pilha");
                lastPosition = 1;

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
                // e adiciona a transa��o novamente na pilha de navega��o
                ft.replace(R.id.framelayout_content, onibusFragment, "onibusFragment");
                lastPosition = 1;
            }
        }

        // Finaliza a trans��o com sucesso
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(OnibusFragment.ARGS_LINHA, linha);
        outState.putString(OnibusFragment.ARGS_SENTIDO, sentido);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setActionBarTitle(String title,String subtitle){
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        String cityname = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "Sem cidade");
        pToolbar.setTitle(cityname);
        Log.i(TAG, "Trocando o t�tulo da action bar para " + title + " ,trocando o subt�tulo para " + subtitle);
    }



    public void setVisibleLayout(){
        //TODO set visiblity in frame fragment list itinerary

    }

    public void setLoadText(String text){
        //textViewLoadBus.setText(text);
    }


    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        String selectedItem = new String("");
        if(iDrawerItem instanceof SecondaryDrawerItem) {
                 selectedItem = ((SecondaryDrawerItem) iDrawerItem).getName();
        }
        if(iDrawerItem instanceof PrimaryDrawerItem) {
            selectedItem = ((PrimaryDrawerItem) iDrawerItem).getName();
        }
        BusDAO dao = new BusDAO(this);

        List<String> sentidos = dao.getItinerary(selectedItem).getSentidos();
        if(sentidos.size()>1){
            Popup.showPopUp(this, view, selectedItem, dao.getItinerary(selectedItem).getSentidos());
        }else{
            onSettingsDone(selectedItem, Arrays.asList(getResources().getStringArray(R.array.list_sentido_circular)).get(0));
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_button:
                onSettingsDone(true);
            return;
        }
    }
}
