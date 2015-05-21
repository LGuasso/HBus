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

import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.TipoDeDia;


public class ListLineActivity extends ActionBarActivity {

    ListView listViewUseful;
    ListView listViewSaturday;
    ListView listViewSunday;

    String line;
    String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_line);

        TabHost abas = (TabHost) findViewById(R.id.tabhost);
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
        local = intent.getStringExtra(MainActivity.EXTRA_LOCAL);
        this.setTitle(local+" - "+line);

        listViewUseful = (ListView)findViewById(R.id.listview_listline_line_uteis);
        ArrayList<String> listUseful = (ArrayList<String>)new LinhaFile("vilaschirmer",false, TipoDeDia.UTEIS,this).getHorarios();
        ListViewAdapterLines adapterLinesUteis = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1,listUseful);
        listViewUseful.setAdapter(adapterLinesUteis);

        listViewSaturday = (ListView)findViewById(R.id.listview_listline_line_sabado);
        ArrayList<String> listSabado = (ArrayList<String>)new LinhaFile("vilaschirmer",false, TipoDeDia.SABADO,this).getHorarios();
        ListViewAdapterLines adapterLinesSabado = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1,listSabado);
        listViewSaturday.setAdapter(adapterLinesSabado);

        listViewSunday = (ListView)findViewById(R.id.listview_listline_line_domingo);
        ArrayList<String> listDomingo = (ArrayList<String>)new LinhaFile("vilaschirmer",false, TipoDeDia.DOMINGO,this).getHorarios();
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
}
