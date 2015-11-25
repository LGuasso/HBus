package br.com.expressobits.hbus.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemCityAdapter;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.ui.ManagerInit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import br.com.expressobits.hbus.dao.ParseDAO;

public class SelectCityActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack/**,FindCallback<ParseObject> */{

    private List<City> cities;
    public boolean initial = false;
    public static final String TAG = "city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initViews();
    }

    private void initActionBar() {

        //Define se é primeira vez do app ou se tem alguma cidade definida...
        initial = (PreferenceManager.getDefaultSharedPreferences(this).getString(TAG,null)==null);
        Toolbar pToolbar = (Toolbar) findViewById(R.id.primary_toolbar);
        setSupportActionBar(pToolbar);
    }

    private void initViews(){
        RecyclerView recyclerViewCities = (RecyclerView) findViewById(R.id.recyclerView_cities);
        recyclerViewCities.setHasFixedSize(true);
        recyclerViewCities.setSelected(true);
        recyclerViewCities.setClickable(true);

        LinearLayoutManager llmUseful = new LinearLayoutManager(this);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCities.setLayoutManager(llmUseful);


        cities = new ArrayList<>();

        City city = new City();
        city.setName("Santa Maria");
        city.setCountry("RS");
        city.setImage(getResources().getDrawable(R.drawable.default_city));

        //ParseDAO dao = new ParseDAO(this);
        //cities = dao.getCities(this);
        //cities = dao.getCities(this);


        //ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        //query.findInBackground(this);

        //TODO lista que vem apartir do parse
        ItemCityAdapter itemCityAdapter = new ItemCityAdapter(this,cities);
        itemCityAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewCities.setAdapter(itemCityAdapter);
        initActionBar();
    }

    @Override
    public void onClickListener(View view, int position) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG, cities.get(position).getName() + " - " + cities.get(position).getCountry());
        editor.apply();

        //TODO implementa download do parse
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose");
        builder.setNegativeButton("Não", null);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ParseDAO parseDAO = new ParseDAO(getBaseContext());
                //parseDAO.insertItineraries();
                //parseDAO.insertCodes();
            }
        });
        builder.show();




        if(initial){
            ManagerInit.manager(this);
        }else{
            this.finish();
        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }


    /**
    public void done(List<ParseObject> objects, ParseException e) {
        ParseDAO dao = new ParseDAO(this);
        for (ParseObject object : objects) {
            cities.add(dao.parseToCity(object));
        }
        ItemCityAdapter itemCityAdapter = new ItemCityAdapter(this,cities);
        itemCityAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewCities.setAdapter(itemCityAdapter);
    }*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
