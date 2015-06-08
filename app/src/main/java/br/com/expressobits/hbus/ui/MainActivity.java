package br.com.expressobits.hbus.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.modelo.Onibus;

public class MainActivity extends AppCompatActivity implements LinhasFragment.OnSettingsListener {

    //Gerencia a atuação dos fragments
    FragmentManager fm = getSupportFragmentManager();
    int lastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(savedInstanceState == null){
            LinhasFragment linhasFragment = new LinhasFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.linear_layout_main,linhasFragment,"linhasFragment");
            ft.commit();
        }

        initViews();
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
        if (id == R.id.action_settings) {
            Toast.makeText(this, getString(R.string.action_settings), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void initViews() {
        initActionBar();
    }

    /**
     * Iniciando o actionbar
     * <ul>
     *     <li>Coloca uma imagem</li>
     *     <li>Mostra este logo como botão de HOME</li>
     * </ul>
     */
    private void initActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
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
        ft.replace(R.id.linear_layout_main,onibusFragment,"onibusFragment");
        ft.addToBackStack("pilha");
        ft.commit();
        lastPosition = 1;
    }

    @Override
    public void OnSettingsDone(String linha, String sentido) {
        FragmentTransaction ft = fm.beginTransaction();
        OnibusFragment onibusFragment = (OnibusFragment)fm.findFragmentByTag("onibusFragment");
        if(onibusFragment != null){
            onibusFragment.refresh(linha, sentido);
        }else{
            onibusFragment = new OnibusFragment();
            Bundle args = new Bundle();
            args.putString(OnibusFragment.ARGS_LINHA,linha);
            args.putString(OnibusFragment.ARGS_SENTIDO,sentido);
            onibusFragment.setArguments(args);
            // Cria um fragment e passa para ele como parâmetro as configurações selecionadas
            FragmentTransaction ft2 = fm.beginTransaction();
            // Troca o que quer que tenha na view do fragment_container por este fragment,
            // e adiciona a transação novamente na pilha de navegação
            ft2.replace(R.id.linear_layout_main, onibusFragment, "onibusFragment");
            ft2.addToBackStack("pilha");
            // Finaliza a transção com sucesso
            ft2.commit();
            lastPosition = 1;

        }
    }
}
