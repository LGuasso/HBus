package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.backend.busApi.BusApi;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.utils.TextUtils;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael Correa
 * @since 14/02/16
 */
public class PullAllBusEndpointsAsyncTask extends AsyncTask<Pair<City,Itinerary>,Integer,List<Bus>>{

    private static final String TAG = PullAllBusEndpointsAsyncTask.class.getSimpleName();

    private BusApi busApi;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Bus> resultListenerAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Bus> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected List<Bus> doInBackground(Pair<City,Itinerary>... params) {
        if(busApi == null){
            BusApi.Builder builder = new BusApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            busApi = builder.build();
        }
        City city = params[0].first;
        Itinerary itinerary = params[0].second;
        ArrayList<Bus> buses  = new ArrayList<>();
        try {
                return busApi.getAllBuses(
                        city.getCountry(), city.getName())
                        .execute().getItems();



        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(progress[0], Bus.class);
        }else{
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }
    }

    @Override
    protected void onPostExecute(List<Bus> buses) {
        if(resultListenerAsyncTask!=null) {
            Log.d(TAG, "Download " + buses.size() + " codes from datastore!");
            resultListenerAsyncTask.finished(buses);
        }else{
            Log.w(this.getClass().getSimpleName(), "resultListenerAsyncTask is null!");
        }
    }

}
