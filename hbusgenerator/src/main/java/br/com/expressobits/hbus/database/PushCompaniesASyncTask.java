package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.expressobits.hbus.utils.FirebaseUtils;
import hbus.model.City;
import hbus.model.Company;

/**
 * @author Rafael Correa
 * @since 23/05/16
 */
public class PushCompaniesASyncTask extends AsyncTask<Pair<City,Company>,Integer,Company> {

    private static final String TAG = PushCompaniesASyncTask.class.getName();
    FirebaseDatabase database;

    @Override
    protected Company doInBackground(Pair<City,Company>... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i].first;
            final Company company = params[i].second;
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.COMPANY_TABLE);
            DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
            final DatabaseReference cityRef = countryRef.child(city.getName());
            cityRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    DatabaseReference companyRef = cityRef.child(company.getName());
                    companyRef.setValue(company);
                }
            });


            Log.d(TAG,company.getName());
            //publishProgress((int) ((i+1 / params.length) * 100));
            return company;
        }
    return null;
    }

}
