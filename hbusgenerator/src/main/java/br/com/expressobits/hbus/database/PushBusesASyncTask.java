package br.com.expressobits.hbus.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import hbus.model.Bus;
import hbus.model.City;
import hbus.model.Company;
import hbus.model.Itinerary;
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
            final City city = params[i].first;
            final Company company = params[i].second.first;
            final Itinerary itinerary = params[i].second.second.first;
            final List<Bus> buses = params[i].second.second.second;
            Log.e("FIREBASE", "Remove " + FirebaseUtils.BUS_TABLE);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference tableRef = database.getReference(FirebaseUtils.BUS_TABLE);
            DatabaseReference countryRef = tableRef.child(city.getCountry());
            DatabaseReference cityRef = countryRef.child(city.getName());
            DatabaseReference companyRef = cityRef.child(company.getName());
            final DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
            itineraryRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    for(int j=0;j<buses.size();j++){
                        Bus bus = buses.get(j);
                        DatabaseReference wayRef = itineraryRef.child(bus.getWay());
                        DatabaseReference typedayRef = wayRef.child(bus.getTypeday());
                        DatabaseReference busRef = typedayRef.child(String.valueOf(bus.getTime()));
                        busRef.setValue(bus);
                        publishProgress((int) ((j+1 / buses.size()) * 100));
                    }
                }
            });
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
