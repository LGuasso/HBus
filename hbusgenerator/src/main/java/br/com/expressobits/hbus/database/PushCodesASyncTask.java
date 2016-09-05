package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 23/05/16
 */
public class PushCodesASyncTask extends AsyncTask<Pair<City,Pair<Company,Code>>,Integer,Code> {

    private static final String TAG = PushCodesASyncTask.class.getName();
    FirebaseDatabase database;

    @Override
    protected Code doInBackground(Pair<City,Pair<Company,Code>>... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i].first;
            Company company =  params[i].second.first;
            final Code code = params[i].second.second;
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CODE_TABLE);
            DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            final DatabaseReference companyRef = cityRef.child(company.getName());
            companyRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    DatabaseReference codeRef = companyRef.child(code.getName());
                    codeRef.setValue(code);
                }
            });

            Log.d(TAG,code.getName());
            publishProgress((int) ((i+1 / params.length) * 100));
            return code;
        }

        return null;
    }

}
