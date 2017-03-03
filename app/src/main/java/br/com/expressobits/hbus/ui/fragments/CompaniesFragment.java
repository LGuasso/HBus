package br.com.expressobits.hbus.ui.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.dao.ScheduleDAO;
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

    private void addCompanies(List<Company> companies){
        listCompanies = companies;
        if(listCompanies.size()>0){
            progressBar.setVisibility(View.INVISIBLE);
            recyclerViewCompanies.setVisibility(View.VISIBLE);
        }
        refreshRecyclerView();
    }

    private void refresh(String country,String city){
        listCompanies.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewCompanies.setVisibility(View.INVISIBLE);
        loadCompaniesFromDatabase(country, city);
    }

    private void loadCompaniesFromDatabase(String country,String city){
        ScheduleDAO dao = new ScheduleDAO(getContext(),country,city);
        addCompanies(dao.getCompanies());
        dao.close();
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
            case R.id.text1:
                intent = new Intent(getContext(), CompanyDetailsActivity.class);
                intent.putExtra(TimesActivity.ARGS_COUNTRY, SQLConstants.getCountryFromBusId(company.getId()));
                intent.putExtra(TimesActivity.ARGS_CITY, SQLConstants.getCityFromBusId(company.getId()));
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
