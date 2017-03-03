package br.com.expressobits.hbus.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Company;

public class CompanyDetailsActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private TextView textViewWebsite;
    private TextView textViewPhoneNumber;
    private TextView textViewAddress;

    public static final String ARGS_COUNTRY = "country";
    public static final String ARGS_CITY = "city";
    public static final String ARGS_COMPANY = "company";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        Toolbar pToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        loadCompanyfromDatabase();
    }

    private void loadCompanyfromDatabase() {
        ScheduleDAO dao = new ScheduleDAO(this,getIntent().getStringExtra(ARGS_COUNTRY),getIntent().getStringExtra(ARGS_CITY));
        Company company = dao.getCompany(getIntent().getStringExtra(ARGS_COMPANY));
        dao.close();
        refreshCompany(company);
    }

    private void refreshCompany(Company company){
        if(textViewAddress.getVisibility()==View.VISIBLE){
            textViewEmail.setText(company.getEmail());
            textViewAddress.setText(company.getAddress());
            textViewPhoneNumber.setText(company.getPhoneNumber());
            textViewWebsite.setText(company.getWebsite());
        }

    }
}
