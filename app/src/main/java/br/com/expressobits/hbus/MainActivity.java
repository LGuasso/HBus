package br.com.expressobits.hbus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    public static final String EXTRA_LINE = "line_bus";
    public static final String EXTRA_LOCAL = "line_local";
    Spinner spinnerLine;
    Spinner spinnerLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        initSpinners();

        Button button = (Button)findViewById(R.id.button_main_look_line);


        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_line, android.R.layout.simple_dropdown_item_1line);
        //spinnerLine.setAdapter(adapter);
        //ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.list_local, android.R.layout.simple_dropdown_item_1line);
        //spinnerLocal.setAdapter(adapter1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListLineActivity.class);
                intent.putExtra(EXTRA_LINE, spinnerLine.getSelectedItem().toString());
                intent.putExtra(EXTRA_LOCAL, spinnerLocal.getSelectedItem().toString());
                startActivity(intent);
            }
        });
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
        if(id == R.id.action_settings){
            Toast.makeText(this,getString(R.string.action_settings),Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Iniciando os spinners.
     * Cria spinners e define arrayAdapters
     * e define seleção
     */
    private void initSpinners(){
        spinnerLine = (Spinner) findViewById(R.id.spinner_list_line);
        spinnerLocal = (Spinner) findViewById(R.id.spinner_list_local);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_line, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLine.setAdapter(adapter);
        spinnerLine.setSelection(0);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.list_local, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(adapter2);
        spinnerLocal.setSelection(0);
    }
}
