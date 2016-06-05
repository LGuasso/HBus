package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.expressobits.hbus.gae.ProgressAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 23/05/16
 */
public class PushItinerariesASyncTask extends AsyncTask<Pair<City,Pair<Company,Itinerary>>,Integer,Itinerary> {

    private static final String TAG = PushItinerariesASyncTask.class.getName();
    FirebaseDatabase database;
    private ProgressAsyncTask progressAsyncTask;
    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;
    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Integer> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected Itinerary doInBackground(Pair<City,Pair<Company,Itinerary>>... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i].first;
            Company company = params[i].second.first;
            Itinerary itinerary = params[i].second.second;
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.ITINERARY_TABLE);
            DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            DatabaseReference companyRef = cityRef.child(company.getName());
            DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
            itineraryRef.setValue(itinerary);
            publishProgress((int) ((i+1 / params.length) * 100));
            return itinerary;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(values[0],Itinerary.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }
    }

    @Override
    protected void onPostExecute(Itinerary itinerary) {
        Log.d(TAG, "SEND OK! \t\t"+itinerary.getName());
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }
    }
}
