package br.com.expressobits.hbus.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class CompanyDetailsActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private TextView textViewWebsite;
    private TextView textViewPhoneNumber;
    private TextView textViewAddress;
    private Toolbar pToolbar;

    public static final String ARGS_COUNTRY = "country";
    public static final String ARGS_CITY = "city";
    public static final String ARGS_COMPANY = "company";

    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    private void initViews(){
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewWebsite = (TextView) findViewById(R.id.textViewWebsite);
        textViewPhoneNumber = (TextView) findViewById(R.id.textViewPhoneNumber);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        getSupportActionBar().setTitle(getIntent().getStringExtra(ARGS_COMPANY));

        textViewEmail.setText(getString(R.string.loading));
        textViewAddress.setText(getString(R.string.loading));
        textViewPhoneNumber.setText(getString(R.string.loading));
        textViewWebsite.setText(getString(R.string.loading));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCompanyfromFirebase();
    }

    private void loadCompanyfromFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference companiesTable = firebaseDatabase.getReference(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryReference = companiesTable.child(getIntent().getStringExtra(ARGS_COUNTRY));
        DatabaseReference cityReference = countryReference.child(getIntent().getStringExtra(ARGS_CITY));
        DatabaseReference companyReference = cityReference.child(getIntent().getStringExtra(ARGS_COMPANY));
        companyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                refreshCompany(company);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish();
            }
        });
    }


    private void refreshCompany(Company company){
        this.company = company;
        if(textViewAddress.getVisibility()==View.VISIBLE){
            textViewEmail.setText(company.getEmail());
            textViewAddress.setText(company.getAddress());
            textViewPhoneNumber.setText(company.getPhoneNumber());
            textViewWebsite.setText(company.getWebsite());
        }

    }
}
