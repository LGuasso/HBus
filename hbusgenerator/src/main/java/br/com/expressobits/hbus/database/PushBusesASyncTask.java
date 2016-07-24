package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 23/05/16
 */
public class PushBusesASyncTask extends AsyncTask<Pair<City,Pair<Company,Pair<Itinerary,List<Bus>>>>,Integer,Itinerary> {

    private static final String TAG = PushBusesASyncTask.class.getName();
    FirebaseDatabase database;
    @Override
    protected Itinerary doInBackground(Pair<City,Pair<Company,Pair<Itinerary,List<Bus>>>>... params) {

        database = FirebaseDatabase.getInstance();

        for (int i=0;i<params.length;i++) {
            City city = params[i].first;
            Company company = params[i].second.first;
            Itinerary itinerary = params[i].second.second.first;
            List<Bus> buses = params[i].second.second.second;
            for(int j=0;j<buses.size();j++){
                Bus bus = buses.get(j);
                DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.BUS_TABLE);
                DatabaseReference countryRef = citiesTableRef.child(city.getCountry());
                DatabaseReference cityRef = countryRef.child(city.getName());
                DatabaseReference companyRef = cityRef.child(company.getName());
                DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
                DatabaseReference wayRef = itineraryRef.child(bus.getWay());
                DatabaseReference typedayRef = wayRef.child(bus.getTypeday());
                DatabaseReference busRef = typedayRef.child(bus.getTime());
                busRef.setValue(bus);
                Log.d(TAG,bus.getTime());
                publishProgress((int) ((j+1 / buses.size()) * 100));
            }
            return itinerary;

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        /**if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(values[0],Itinerary.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }*/
    }

    @Override
    protected void onPostExecute(Itinerary itinerary) {
       /** Log.d(TAG, "SEND OK! \t\t\t"+itinerary.getName());
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }*/
    }
}
