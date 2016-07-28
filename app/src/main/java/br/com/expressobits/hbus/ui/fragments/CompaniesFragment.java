package br.com.expressobits.hbus.ui.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.ui.CompanyDetailsActivity;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.TimesActivity;
import br.com.expressobits.hbus.ui.adapters.ItemCompanyAdapter;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompaniesFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    private List<Company> listCompanies = new ArrayList<>();
    public static final String TAG = "CompaniesFragment";
    private RecyclerView recyclerViewCompanies;
    private ProgressBar progressBar;


    public CompaniesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_companies, container, false);
        initViews(view);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setSelectItemNavigation(TAG);
        ((MainActivity)getActivity()).setActionBarTitle();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityId = sharedPreferences.getString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);


        if(listCompanies.size()>0){
            recyclerViewCompanies.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            refreshRecyclerView();
        }else {

        }
        String country = FirebaseUtils.getCountry(cityId);
        String city = FirebaseUtils.getCityName(cityId);
        refresh(country,city);

    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerViewCompanies = (RecyclerView) view.findViewById(R.id.recyclerViewCompanies);
        recyclerViewCompanies.setHasFixedSize(true);


    }

    private void addCompany(Company company){
        if(company.isActived() || PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("no_actived_itens",false)){
            if(company.getId()!=null){
                listCompanies.add(company);
            }
            if(listCompanies.size()>0){
                progressBar.setVisibility(View.INVISIBLE);
                recyclerViewCompanies.setVisibility(View.VISIBLE);
            }
            refreshRecyclerView();
        }else{
            Log.d(TAG,"no listed company "+company.getName()+" no actived!");
        }


    }

    private void refresh(String country,String city){
        listCompanies.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewCompanies.setVisibility(View.INVISIBLE);
        loadCompaniesFromFirebase(country, city);
    }

    private void loadCompaniesFromFirebase(final String country, final String city){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference companiesTable = firebaseDatabase.getReference(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryReference = companiesTable.child(country);
        DatabaseReference cityReference = countryReference.child(city);
        cityReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Company company = dataSnapshot.getValue(Company.class);
                company.setId(FirebaseUtils.getIdCompany(country,city,company.getName()));
                addCompany(company);
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

    public void refreshRecyclerView(){

        ItemCompanyAdapter arrayAdapter = new ItemCompanyAdapter(getContext(),listCompanies);
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewCompanies.setAdapter(arrayAdapter);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCompanies.setLayoutManager(llmUseful);
    }

    @Override
    public void onClickListener(View view, int position) {

        Intent intent;
        Company company = listCompanies.get(position);
        switch (view.getId()){
            case R.id.text2:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,company.getEmail());
                intent.putExtra(Intent.EXTRA_SUBJECT,company.getEmail());
                getActivity().startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.text1:
                intent = new Intent(getContext(), CompanyDetailsActivity.class);
                intent.putExtra(TimesActivity.ARGS_COUNTRY,FirebaseUtils.getCountry(company.getId()));
                intent.putExtra(TimesActivity.ARGS_CITY, FirebaseUtils.getCityName(company.getId()));
                intent.putExtra(TimesActivity.ARGS_COMPANY, company.getName());
                startActivity(intent);
                break;
            case R.id.icon:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String cityId = sharedPreferences.getString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(cityId,company.getName());
                editor.apply();
                refreshRecyclerView();
                ((MainActivity)getActivity()).refresh();
                break;

        }
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
