package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.annotations.NotNull;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.itineraryApi.ItineraryApi;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;

/**
 * @author Rafael Correa
 * @since 08/02/16
 */
public class PushItinerariesEndpointsAsyncTask extends AsyncTask<Itinerary,Integer,Integer>{

    private static final String TAG = PushItinerariesEndpointsAsyncTask.class.getClass().getSimpleName();

    private static ItineraryApi itineraryApi = null;
    @NotNull
    private Context context;
    @NotNull
    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;

    private String cityName;

    private String country;

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Integer> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected Integer doInBackground(Itinerary... params) {
        if(itineraryApi == null) {  // Only do this once
            ItineraryApi.Builder builder = new ItineraryApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            itineraryApi = builder.build();
        }

        for (int i=0;i<params.length;i++) {
            Itinerary itinerary = params[i];
            try {
                itineraryApi.insertItinerary(country, cityName,itinerary).execute();

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
            progressAsyncTask.setProgressUdate(progress[0],Itinerary.class);
        }else{
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Log.d(TAG, "Send itineraries from datastore!");
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }

    }
}
