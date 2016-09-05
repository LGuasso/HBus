package br.com.expressobits.hbusgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class NewsListActivity extends AppCompatActivity  implements View.OnClickListener,
        AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{

    private List<News> newses = new ArrayList<>();
    private List<City> cities = new ArrayList<>();

    private ImageButton imageButton;
    private Spinner spinnerCities;
    private Spinner spinnerMode;
    private ListView listView;

    private static final String[] MODES = {FirebaseUtils.GENERAL,FirebaseUtils.CITY_TABLE};
    private static final String COUNTRY = "BR/RS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        setTitle("News");
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        spinnerCities = (Spinner) findViewById(R.id.spinnerCities);
        spinnerMode = (Spinner) findViewById(R.id.spinnerMode);
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(this);
        imageButton.setOnClickListener(this);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,MODES);
        spinnerMode.setAdapter(arrayAdapter);
        spinnerMode.setOnItemSelectedListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_news) {
            startActivity(new Intent(this,NewsEditorActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void pullCities(final String country) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tableRef = database.getReference(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = tableRef.child(country);
        countryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                City city = dataSnapshot.getValue(City.class);
                city.setId(FirebaseUtils.getIdCity(country,city.getName()));
                addCity(city);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void update(){
        ArrayAdapter<City> arrayAdapter = new ArrayAdapter<City>(this,
                android.R.layout.simple_list_item_1,cities);
        spinnerCities.setAdapter(arrayAdapter);
    }

    private void addCity(City city) {
        cities.add(city);
        update();
    }

    @Override

    public void onClick(View view) {
        String mode = MODES[spinnerMode.getSelectedItemPosition()];
        newses.clear();
        if(mode.equals(FirebaseUtils.GENERAL)){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference newsRef = database.getReference().child(FirebaseUtils.NEWS_TABLE);
            DatabaseReference generalRef = newsRef.child(FirebaseUtils.GENERAL);
            generalRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    News news = dataSnapshot.getValue(News.class);
                    news.setId(FirebaseUtils.getIdNewsGeneral(String.valueOf(news.getTime())));
                    addNews(news);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            final City city = cities.get(spinnerCities.getSelectedItemPosition());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference newsRef = database.getReference().child(FirebaseUtils.NEWS_TABLE);
            DatabaseReference cityTable = newsRef.child(FirebaseUtils.CITY_TABLE);
            DatabaseReference countryRef = cityTable.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            cityRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    News news = dataSnapshot.getValue(News.class);
                    news.setId(FirebaseUtils.getIdNewsCity(
                            String.valueOf(news.getTime()),city.getCountry(),
                            city.getName()));
                    addNews(news);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    private void addNews(News news) {
        newses.add(news);
        ArrayAdapter<News> simple  = new ArrayAdapter<News>(this,android.R.layout.simple_list_item_1,newses);
        listView.setAdapter(simple);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        News news = newses.get(i);
        Intent intent = new Intent(this,NewsEditorActivity.class);
        intent.putExtra(NewsEditorActivity.ID_ARGUMENT,news.getId());
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (MODES[spinnerMode.getSelectedItemPosition()].equals(FirebaseUtils.GENERAL)) {
            spinnerCities.setEnabled(false);
        } else {
            spinnerCities.setEnabled(true);
            cities.clear();
            pullCities(COUNTRY);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
