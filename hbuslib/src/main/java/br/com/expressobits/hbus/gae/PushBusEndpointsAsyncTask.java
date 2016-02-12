package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;

import br.com.expressobits.hbus.backend.busApi.BusApi;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael
 * @since 12/02/16
 */
public class PushBusEndpointsAsyncTask extends AsyncTask<Bus,Integer,Integer> {

    private static final String TAG = PushBusEndpointsAsyncTask.class.getClass().getSimpleName();

    private static BusApi busApi = null;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;

    private String country;

    private String cityName;

    private String itineraryName;

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

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    @Override
    protected Integer doInBackground(Bus... params) {
        if(busApi == null) {  // Only do this once
            BusApi.Builder builder = new BusApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            busApi = builder.build();
        }

        for (int i=0;i<params.length;i++) {
            Bus bus = params[i];
            try {
                //TODO modify to add param country name
                busApi.insertBus(country,cityName,itineraryName,bus).execute();

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
            progressAsyncTask.setProgressUdate(progress[0],Bus.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Log.d(TAG, "Send buses from datastore!");
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }

    }

}
