package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 23/05/16
 */
public class PushCitiesASyncTask extends AsyncTask<City,Integer,City> {

    private static final String TAG = PushCitiesASyncTask.class.getName();
    FirebaseDatabase database;

    @Override
    protected City doInBackground(City... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i];
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CITY_TABLE);

            DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            cityRef.setValue(city);

            Log.d(TAG,city.getName());
            //publishProgress((int) ((i+1 / params.length) * 100));

            return city;
        }
        return null;
    }

}
