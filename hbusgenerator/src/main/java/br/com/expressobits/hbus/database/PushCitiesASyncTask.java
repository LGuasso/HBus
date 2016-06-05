package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.expressobits.hbus.gae.ProgressAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 23/05/16
 */
public class PushCitiesASyncTask extends AsyncTask<City,Integer,City> {

    private static final String TAG = PushCitiesASyncTask.class.getName();
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
    protected City doInBackground(City... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i];
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CITY_TABLE);
            DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            cityRef.setValue(city);
            //publishProgress((int) ((i+1 / params.length) * 100));

            return city;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(values[0],City.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }
    }

    @Override
    protected void onPostExecute(City city) {
        Log.d(TAG, "SEND OK! "+city.getName());
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }
    }
}
