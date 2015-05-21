package br.com.expressobits.hbus;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.TipoDeDia;


public class ListLineActivity extends ActionBarActivity {

    ListView listView;
    String line;
    String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_line);
        Intent intent = getIntent();
        line = intent.getStringExtra(MainActivity.EXTRA_LINE);
        local = intent.getStringExtra(MainActivity.EXTRA_LOCAL);
        this.setTitle(local+" - "+line);

        listView = (ListView)findViewById(R.id.listview_listline_line);
        ArrayList<String> list = new ArrayList<String>();
        list = (ArrayList<String>)new LinhaFile("vilaschirmer",false, TipoDeDia.UTEIS,this).getHorarios();

        String[] lista = getResources().getStringArray(R.array.line_2_b_d);

        ListViewAdapterLines adapterLines = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1,list);


        listView.setAdapter(adapterLines);


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
