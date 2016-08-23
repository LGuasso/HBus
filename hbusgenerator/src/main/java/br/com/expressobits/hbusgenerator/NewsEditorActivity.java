package br.com.expressobits.hbusgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class NewsEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTitle;
    private EditText editTextBody;
    private EditText editTextID;
    private EditText editTextSource;
    private EditText editTextImage;
    private Spinner spinnerCountrys;
    private ImageButton imageButtonCountrys;
    private Spinner spinnerCities;
    private ImageButton imageButtonCities;
    private Spinner spinnerCompanies;
    private ImageButton imageButtonCompanies;

    private Button buttonSave;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_editor);
        initViews();
    }

    private void initViews() {
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextBody = (EditText) findViewById(R.id.editTextBody);
        editTextSource = (EditText) findViewById(R.id.editTextSource);
        editTextID = (EditText) findViewById(R.id.editTextId);
        editTextImage = (EditText) findViewById(R.id.editTextImage);
        spinnerCountrys = (Spinner) findViewById(R.id.spinnerCountrys);
        spinnerCities = (Spinner) findViewById(R.id.spinnerCities);
        spinnerCompanies = (Spinner) findViewById(R.id.spinnerCompanies);
        imageButtonCountrys = (ImageButton) findViewById(R.id.imageButtonLoadCountrys);
        imageButtonCities = (ImageButton) findViewById(R.id.imageButtonLoadCities);
        imageButtonCompanies = (ImageButton) findViewById(R.id.imageButtonLoadCompanies);
        imageButtonCountrys.setOnClickListener(this);
        imageButtonCities.setOnClickListener(this);
        imageButtonCompanies.setOnClickListener(this);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        news = new News();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSave:
                news.setId(editTextID.getText().toString());
                news.setTitle(editTextTitle.getText().toString());
                news.setBody(editTextBody.getText().toString());
                news.setSource(editTextSource.getText().toString());
                news.setTime(System.currentTimeMillis());
                ArrayList<String> images = new ArrayList<>();
                images.add(editTextImage.getText().toString());
                news.setImagesUrls(images);
                pushToFirebase(news);
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

    private void loadCountrys() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child(FirebaseUtils.CITY_TABLE);
        ArrayList<String> countrys = new ArrayList<>();
        countrys.add("RS");
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
        databaseResult.child(String.valueOf(news.getTime())).setValue(news);
        finish();
    }
}
