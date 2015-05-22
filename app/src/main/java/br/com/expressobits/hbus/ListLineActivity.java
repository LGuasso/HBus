package br.com.expressobits.hbus;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.modelo.TipoDeDia;


public class ListLineActivity extends ActionBarActivity {

    ListView listViewUseful;
    ListView listViewSaturday;
    ListView listViewSunday;

    TabHost abas;

    String line;
    boolean centro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_line);

        abas = (TabHost) findViewById(R.id.tabhost);
        abas.setup();

        TabHost.TabSpec descritor = abas.newTabSpec("tag1");
        descritor.setContent(R.id.listview_listline_line_uteis);
        descritor.setIndicator(getString(R.string.useful));
        abas.addTab(descritor);

        descritor = abas.newTabSpec("tag2");
        descritor.setContent(R.id.listview_listline_line_sabado);
        descritor.setIndicator(getString(R.string.saturday));
        abas.addTab(descritor);

        descritor = abas.newTabSpec("tag3");
        descritor.setContent(R.id.listview_listline_line_domingo);
        descritor.setIndicator(getString(R.string.sunday));
        abas.addTab(descritor);

        //Definir pelo dia retornado
        abas.setCurrentTab(0);


        Intent intent = getIntent();
        line = intent.getStringExtra(MainActivity.EXTRA_LINE);
        centro = intent.getBooleanExtra(MainActivity.EXTRA_SENTIDO,false);
        this.setTitle((centro ? "Centro > Bairro" : "Bairro > Centro")+" - "+line);

        listViewUseful = (ListView)findViewById(R.id.listview_listline_line_uteis);
        ArrayList<Onibus> listUseful = (ArrayList<Onibus>)new LinhaFile(toFileName(line),centro, TipoDeDia.UTEIS,this).getHorarios();
        ListViewAdapterLines adapterLinesUteis = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1,listUseful);
        listViewUseful.setAdapter(adapterLinesUteis);

        for(int i=0;i<listUseful.size();i++){
            if(listUseful.get(i).getHora()<=new GregorianCalendar().get(Calendar.HOUR_OF_DAY)){
                listViewUseful.setSelection(i);
                break;
            }
        }

        listViewSaturday = (ListView)findViewById(R.id.listview_listline_line_sabado);
        ArrayList<Onibus> listSabado = (ArrayList<Onibus>)new LinhaFile(toFileName(line),centro, TipoDeDia.SABADO,this).getHorarios();
        ListViewAdapterLines adapterLinesSabado = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1,listSabado);
        listViewSaturday.setAdapter(adapterLinesSabado);

        listViewSunday = (ListView)findViewById(R.id.listview_listline_line_domingo);
        ArrayList<Onibus> listDomingo = (ArrayList<Onibus>)new LinhaFile(toFileName(line),false, TipoDeDia.DOMINGO,this).getHorarios();
        ListViewAdapterLines adapterLinesDomingo = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1,listDomingo);
        listViewSunday.setAdapter(adapterLinesDomingo);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    public void loadList(String[] lista){




    }

    /**
     * Retorna nome sem espaço sem acentos e sem maiusculas.
     * @param name
     * @return
     */
    public String toFileName(String name){
        String ok = name;
        ok = ok.replace(" ", "-");
        ok = ok.replace("é","e");
        ok = ok.toLowerCase();
        return ok;
    }
}
