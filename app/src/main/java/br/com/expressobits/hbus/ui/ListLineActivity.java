package br.com.expressobits.hbus.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.modelo.TipoDeDia;


public class ListLineActivity extends ActionBarActivity {

    ListView listViewUseful;
    ListView listViewSaturday;
    ListView listViewSunday;

    TabHost abas;

    String line;
    String sentido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bus);
        initViews();
    }

    private void initViews() {
        abas = (TabHost) findViewById(R.id.tabhost);
        abas.setup();
        initTabs();

        //Definir pelo dia retornado
        abas.setCurrentTab(0);


        Intent intent = getIntent();
        line = intent.getStringExtra(MainActivity.EXTRA_LINE);
        sentido = intent.getStringExtra(MainActivity.EXTRA_SENTIDO).toLowerCase().replace(" > ","-").replace(" ", "");

        this.setTitle(intent.getStringExtra(MainActivity.EXTRA_SENTIDO) + " - " + line);

        listViewUseful = (ListView)findViewById(R.id.listview_listline_line_uteis);
        ArrayList<Onibus> listUseful = (ArrayList<Onibus>)new LinhaFile(toFileName(line), sentido, TipoDeDia.UTEIS,this).getOnibusList();
        ListViewAdapterBus adapterLinesUteis = new ListViewAdapterBus(this,android.R.layout.simple_list_item_1, listUseful);
        listViewUseful.setAdapter(adapterLinesUteis);


        for(int i=0;i<listUseful.size();i++){
            if(listUseful.get(i).getHora()<=new GregorianCalendar().get(Calendar.HOUR_OF_DAY)){
                listViewUseful.setSelection(i);
                break;
            }
        }

        listViewSaturday = (ListView)findViewById(R.id.listview_listline_line_sabado);
        ArrayList<Onibus> listSabado = (ArrayList<Onibus>)new LinhaFile(toFileName(line), sentido, TipoDeDia.SABADO,this).getOnibusList();
        ListViewAdapterBus adapterLinesSabado = new ListViewAdapterBus(this,android.R.layout.simple_list_item_1, listSabado);
        listViewSaturday.setAdapter(adapterLinesSabado);


        listViewSunday = (ListView)findViewById(R.id.listview_listline_line_domingo);
        ArrayList<Onibus> listDomingo = (ArrayList<Onibus>)new LinhaFile(toFileName(line),sentido, TipoDeDia.DOMINGO,this).getOnibusList();
        ListViewAdapterBus adapterLinesDomingo = new ListViewAdapterBus(this,android.R.layout.simple_list_item_1, listDomingo);
        listViewSunday.setAdapter(adapterLinesDomingo);

    }

    private void initTabs() {
        TabHost.TabSpec descritor = abas.newTabSpec("tag1");
        descritor.setContent(R.id.linear_layout_uteis);
        descritor.setIndicator(getString(R.string.useful));
        abas.addTab(descritor);

        descritor = abas.newTabSpec("tag2");
        descritor.setContent(R.id.linear_layout_sabado);
        descritor.setIndicator(getString(R.string.saturday));
        abas.addTab(descritor);

        descritor = abas.newTabSpec("tag3");
        descritor.setContent(R.id.linear_layout_domingo);
        descritor.setIndicator(getString(R.string.sunday));
        abas.addTab(descritor);

        // Assim como voce mencionou
        TabWidget tabWidget = abas.getTabWidget();

        // Como o TabWidget eh um ViewGroup, ele tem filhos e podemos iterar
        // sobre os mesmos
        int childCount = tabWidget.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = tabWidget.getChildTabViewAt(i);
            // Que eh o mesmo que
            //View child = tabWidget.getChildAt(i);
            // Vide o codigo fonte

            // O Drawable vai variar conforme o nome do seu tema, confirme se tem
            // algum nome parecido com esse e altere aqui
            child.setBackgroundResource(R.drawable.tab_indicator_ab_orange);
        }
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
