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
    private ProgressAsyncTask progressAsyncTask;
    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;
    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Integer> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected Code doInBackground(Pair<City,Pair<Company,Code>>... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i].first;
            Company company =  params[i].second.first;
            Code code = params[i].second.second;
            DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CODE_TABLE);
            DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            DatabaseReference companyRef = cityRef.child(company.getName());
            DatabaseReference itineraryRef = companyRef.child(code.getName());
            itineraryRef.setValue(code);
            publishProgress((int) ((i+1 / params.length) * 100));
            return code;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(values[0],Code.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }
    }

    @Override
    protected void onPostExecute(Code code) {
        Log.d(TAG, "SEND OK! \t\t"+code.getName());
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }
    }
}
