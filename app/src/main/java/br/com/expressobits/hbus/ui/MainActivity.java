package br.com.expressobits.hbus.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Linha;

public class MainActivity extends ActionBarActivity {

    public static final String EXTRA_LINE = "line_bus";
    public static final String EXTRA_SENTIDO = "line_sentido";
    Spinner spinnerLocal;
    public String selectedItem;
    ListView listViewLines;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        initActionBar();
        initSpinners();

        Button button = (Button) findViewById(R.id.button_main_look_line);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListLineActivity.class);
                intent.putExtra(EXTRA_LINE, selectedItem);
                intent.putExtra(EXTRA_SENTIDO, spinnerLocal.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }

    /**
     * Iniciando os spinners.
     * Cria spinners e define arrayAdapters
     * e define seleção
     */
    private void initSpinners() {
        //spinnerLine = (Spinner) findViewById(R.id.spinner_list_line);
        listViewLines = (ListView) findViewById(R.id.list_lines);
        final ArrayList<Linha> linhas = (ArrayList<Linha>) new LinhaFile("linhas",this).getLinhasList();
        ListViewAdapterLines adapterLinesUteis = new ListViewAdapterLines(this,android.R.layout.simple_list_item_1, linhas);
        listViewLines.setAdapter(adapterLinesUteis);


        spinnerLocal = (Spinner) findViewById(R.id.spinner_list_sentido);

        adapter = ArrayAdapter.createFromResource(this, R.array.list_sentido, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinnerLine.setAdapter(adapter);
        //spinnerLine.setSelection(0);
        listViewLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = linhas.get(position).getNome();
                switch (selectedItem) {

                    case "Seletivo UFSM Bombeiros":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_ufsm_bombeiros, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "T Neves Campus":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_tneves_campus, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Boi Morto":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_boi_morto, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Carolina Sao Jose":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_carolina_sao_jose, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Itarare Brigada":
                    case "Circular Cemiterio Sul":
                    case "Circular Cemiterio Norte":
                    case "Circular Camobi":
                    case "Circular Barao":
                    case "Brigada Itarare":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_circular, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "UFSM Circular":
                    case "UFSM":
                    case "Seletivo UFSM":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_ufsm_centro, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "UFSM Bombeiros":
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido_ufsm_bombeiros, android.R.layout.simple_dropdown_item_1line);
                        break;
                    default:

                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.list_sentido, android.R.layout.simple_dropdown_item_1line);

                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLocal.setAdapter(adapter);
                spinnerLocal.setSelection(0);
            }


        });

    }

    /**
     * Iniciando o actionbar
     */
    private void initActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}
