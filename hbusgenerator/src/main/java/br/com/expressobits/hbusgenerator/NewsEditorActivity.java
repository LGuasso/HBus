package br.com.expressobits.hbusgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.expressobits.hbus.utils.FirebaseUtils;
import hbus.model.City;
import hbus.model.Company;
import hbus.model.News;

public class NewsEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTitle;
    private EditText editTextBody;
    private EditText editTextSource;
    private EditText editTextImage;
    private Spinner spinnerCountrys;
    private ImageButton imageButtonCountrys;
    private Spinner spinnerCities;
    private ImageButton imageButtonCities;
    private Spinner spinnerCompanies;
    private ImageButton imageButtonCompanies;

    private EditText editTextAddedItineraries;

    private Button buttonSave;
    private News news;

    private String id;
    private boolean edit;
    public static String ID_ARGUMENT = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_editor);
        String idEdit = getIntent().getStringExtra(ID_ARGUMENT);
        edit = idEdit!=null;
        if(edit){
            id = idEdit;
            Toast.makeText(this,"EDIT "+id,Toast.LENGTH_LONG).show();
        }else {
            //id = String.valueOf(System.currentTimeMillis());
            Toast.makeText(this,"NEW",Toast.LENGTH_LONG).show();
        }

        news = new News();
        initViews();


        if(edit) {
            loadNews(id);
        }else {
            news.setTime(System.currentTimeMillis());
        }
        setTitle(news.getTime()+"");
    }

    private void initViews() {
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextBody = (EditText) findViewById(R.id.editTextBody);
        editTextSource = (EditText) findViewById(R.id.editTextSource);
        editTextImage = (EditText) findViewById(R.id.editTextImage);
        spinnerCountrys = (Spinner) findViewById(R.id.spinnerCountrys);
        spinnerCities = (Spinner) findViewById(R.id.spinnerCities);
        spinnerCompanies = (Spinner) findViewById(R.id.spinnerCompanies);
        imageButtonCountrys = (ImageButton) findViewById(R.id.imageButtonLoadCountrys);
        imageButtonCities = (ImageButton) findViewById(R.id.imageButtonLoadCities);
        imageButtonCompanies = (ImageButton) findViewById(R.id.imageButtonLoadCompanies);
        editTextAddedItineraries = (EditText) findViewById(R.id.editTextAddedItineraries);
        imageButtonCountrys.setOnClickListener(this);
        imageButtonCities.setOnClickListener(this);
        imageButtonCompanies.setOnClickListener(this);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_news) {
            save();
        }
        if (id == R.id.action_delete_news) {
            delete();
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        removeToFirebase(news);
    }

    private void save() {
        news.setTitle(editTextTitle.getText().toString());
        news.setBody(editTextBody.getText().toString());
        news.setSource(editTextSource.getText().toString());
        ArrayList<String> images = new ArrayList<>();
        images.add(editTextImage.getText().toString());
        news.setImagesUrls(images);
        ArrayList<String> iti = new ArrayList<String>(Arrays.asList(editTextAddedItineraries.getText().toString().split(",")));
        news.setItineraryIds(iti);
        pushToFirebase(news);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSave:

                break;
            case R.id.imageButtonLoadCountrys:
                loadCountrys();
                break;
            case R.id.imageButtonLoadCities:
                loadCities();
                break;
            case R.id.imageButtonLoadCompanies:
                loadCompanies();
                break;
        }

    }

    public void loadNews(final String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                news.setId(id);
                editTextTitle.setText(news.getTitle());
                editTextBody.setText(news.getBody());
                if(news.getImagesUrls().get(0)!=null){
                    editTextImage.setText(news.getImagesUrls().get(0));
                }
                editTextSource.setText(news.getSource());
                NewsEditorActivity.this.news.setTime(news.getTime());
                setTitle(news.getTime()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadCountrys() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child(FirebaseUtils.CITY_TABLE);
        ArrayList<String> countrys = new ArrayList<>();
        countrys.add("BR/RS");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countrys);
        spinnerCountrys.setAdapter(arrayAdapter);
    }

    private void loadCities() {
        final ArrayList<String> cities = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference citiTAbleRef = database.getReference().child(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = citiTAbleRef.child(spinnerCountrys.getSelectedItem().toString());
        countryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                City city = dataSnapshot.getValue(City.class);
                cities.add(city.getName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewsEditorActivity.this,android.R.layout.simple_spinner_item,cities);
                spinnerCities.setAdapter(arrayAdapter);


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

    private void loadCompanies() {
        final ArrayList<String> companies = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference citiTAbleRef = database.getReference().child(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryRef = citiTAbleRef.child(spinnerCountrys.getSelectedItem().toString());
        DatabaseReference citiesRef = countryRef.child(spinnerCities.getSelectedItem().toString());
        citiesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Company company = dataSnapshot.getValue(Company.class);
                companies.add(company.getName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewsEditorActivity.this,android.R.layout.simple_spinner_item,companies);
                spinnerCompanies.setAdapter(arrayAdapter);
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

    private void pushToFirebase(News news) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference databaseResult;
        if(spinnerCompanies.getSelectedItem()!=null){
            databaseResult = databaseReference.child(FirebaseUtils.COMPANY_TABLE)
                    .child(spinnerCountrys.getSelectedItem().toString())
                    .child(spinnerCities.getSelectedItem().toString())
                    .child(spinnerCompanies.getSelectedItem().toString());
        }else if(spinnerCities.getSelectedItem()!=null){
            databaseResult = databaseReference.child(FirebaseUtils.CITY_TABLE)
                    .child(spinnerCountrys.getSelectedItem().toString())
                    .child(spinnerCities.getSelectedItem().toString());
        }else {
            databaseResult = databaseReference.child(FirebaseUtils.GENERAL);
        }
        databaseResult.child(news.getTime()+"").setValue(news);
        finish();
    }

    private void removeToFirebase(News news) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference databaseResult;
        if(spinnerCompanies.getSelectedItem()!=null){
            databaseResult = databaseReference.child(FirebaseUtils.COMPANY_TABLE)
                    .child(spinnerCountrys.getSelectedItem().toString())
                    .child(spinnerCities.getSelectedItem().toString())
                    .child(spinnerCompanies.getSelectedItem().toString());
        }else if(spinnerCities.getSelectedItem()!=null){
            databaseResult = databaseReference.child(FirebaseUtils.CITY_TABLE)
                    .child(spinnerCountrys.getSelectedItem().toString())
                    .child(spinnerCities.getSelectedItem().toString());
        }else {
            databaseResult = databaseReference.child(FirebaseUtils.GENERAL);
        }
        databaseResult.child(news.getTime()+"").removeValue();
        finish();
    }
}
