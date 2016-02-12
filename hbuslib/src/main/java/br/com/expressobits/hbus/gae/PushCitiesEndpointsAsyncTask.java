package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.itineraryApi.ItineraryApi;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael Correa
 * @since 12/02/16
 */
public class PushCitiesEndpointsAsyncTask extends AsyncTask<City,Integer,Integer>{

    private static final String TAG = PushCitiesEndpointsAsyncTask.class.getClass().getSimpleName();

    private static CityApi cityApi = null;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;

    private String country;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Integer> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    protected Integer doInBackground(City... params) {
        if(cityApi == null) {  // Only do this once
            CityApi.Builder builder = new CityApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            cityApi = builder.build();
        }

        for (int i=0;i<params.length;i++) {
            City city = params[i];
            try {
                //TODO modify to add param country name
                cityApi.insertCity(city).execute();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Endpoints", e.getMessage());
            }
            publishProgress((int) ((i+1 / params.length) * 100));
        }
        return params.length;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(progress[0],City.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Log.d(TAG, "Send cities from datastore!");
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }

    }
}
