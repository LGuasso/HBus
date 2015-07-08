package br.com.expressobits.hbus.ui;

import android.app.Fragment;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FavoritosDAO;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Bus;
import br.com.expressobits.hbus.modelo.Itinerario;
import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.modelo.TipoDeDia;
import br.com.expressobits.hbus.preferences.Preferences;
import br.com.expressobits.hbus.ui.fragments.AddFavoriteFragment;
import br.com.expressobits.hbus.ui.fragments.LinhasFragment;
import br.com.expressobits.hbus.ui.fragments.OnibusFragment;
import br.com.expressobits.hbus.utils.Popup;

public class MainActivity extends AppCompatActivity implements OnSettingsListener,Drawer.OnDrawerItemClickListener {

    public static final String TAG = "Atividade Principal";

    private Toolbar pToolbar;
    //Navigation Drawer
    private Drawer navigationDrawer;

    //Gerencia a atuação dos fragments
    FragmentManager fm = getSupportFragmentManager();
    int lastPosition = 0;
    String linha;
    String sentido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){

            LinhasFragment linhasFragment = new LinhasFragment();
            if(findViewById(R.id.framelayout_main)!=null){
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.framelayout_main,linhasFragment,"linhasFragment");
                ft.commit();
            }else if(findViewById(R.id.framelayout_content)!=null){
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.framelayout_menu,linhasFragment,"linhasFragment");
                ft.add(R.id.framelayout_content,new OnibusFragment(),"onibusFragment");
                ft.commit();
            }

        }

        initViews();
        initNavigationDrawer();
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
        navigationDrawer.addItem(new SectionDrawerItem().withName(R.string.favorites));
        FavoritosDAO dao = new FavoritosDAO(this);
        new LinhaFile(this).iniciarDados(dao.getLista());
        List<Itinerario> itinerarios = LinhaFile.getItinerarios();
        for(Itinerario itinerario:itinerarios){
            navigationDrawer.addItem(new PrimaryDrawerItem().withName(itinerario.getNome()).withIcon(getResources().getDrawable(R.mipmap.ic_launcher)));
        }
        navigationDrawer.addItem(new SectionDrawerItem().withName(R.string.all_lines));
        List<String>allItinerarios = new LinhaFile(this).getNomeLinhas();
        for(String txt:allItinerarios){
            navigationDrawer.addItem(new SecondaryDrawerItem().withName(txt).withIcon(getResources().getDrawable(R.mipmap.ic_launcher_foreground)));
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
            Toast.makeText(this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_action_add) {
            onSettingsDone(true);
            Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    private void initViews() {
        initActionBar();
        initAdView();

    }

    /**
     * Iniciando o actionbar
     * <ul>
     *     <li>Coloca uma imagem</li>
     *     <li>Mostra este logo como botão de HOME</li>
     * </ul>
     */
    private void initActionBar() {
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        pToolbar = (Toolbar) findViewById(R.id.primary_toolbar);
        pToolbar.setTitle("My lines");
        pToolbar.setSubtitle("subtitle");
        setSupportActionBar(pToolbar);

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

        AddFavoriteFragment addFavoriteFragment = (AddFavoriteFragment)fm.findFragmentByTag("addFavoriteFragment");
        if(findViewById(R.id.framelayout_main)!=null){

                addFavoriteFragment = new AddFavoriteFragment();
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_main, addFavoriteFragment, "addFavoriteFragment");
                ft.addToBackStack("pilha");
                lastPosition = 1;



        }else if(findViewById(R.id.framelayout_content)!=null){
                addFavoriteFragment = new AddFavoriteFragment();
                // Troca o que quer que tenha na view do fragment_container por este fragment,
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_content, addFavoriteFragment, "onibusFragment");
                lastPosition = 1;

        }

        // Finaliza a transção com sucesso
        ft.commit();

    }

    @Override
    public void onSettingsDone(String linha, String sentido) {

        this.linha = linha;
        this.sentido = sentido;

        getSupportActionBar().setTitle(linha);
        getSupportActionBar().setSubtitle(sentido);

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
                // e adiciona a transação novamente na pilha de navegação
                ft.replace(R.id.framelayout_content, onibusFragment, "onibusFragment");
                lastPosition = 1;
            }
        }

        // Finaliza a transção com sucesso
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(OnibusFragment.ARGS_LINHA,linha);
        outState.putString(OnibusFragment.ARGS_SENTIDO,sentido);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setActionBarTitle(String title,String subtitle){
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        Log.i(TAG, "Trocando o título da action bar para " + title + " ,trocando o subtítulo para " + subtitle);
    }

    public void initAdView(){
        AdView mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
            switch (selectedItem) {
                case "Itarare Brigada":
                case "Circular Cemiterio Sul":
                case "Circular Cemiterio Norte":
                case "Circular Camobi":
                case "Circular Barao":
                case "Brigada Itarare":
                    onSettingsDone(selectedItem, Arrays.asList(getResources().getStringArray(R.array.list_sentido_circular)).get(0));
                    break;

                default:

                    Popup.showPopUp(this, view, selectedItem,new LinhaFile(this).getSentidos(this,selectedItem));
            }

        return false;
    }
}
