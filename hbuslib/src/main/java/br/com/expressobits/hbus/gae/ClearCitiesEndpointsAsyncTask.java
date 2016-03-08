package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;

import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael Correa
 * @since 09/02/16
 */
public class ClearCitiesEndpointsAsyncTask extends AsyncTask<String,Integer,Integer> {

    private static final String TAG = ClearCitiesEndpointsAsyncTask.class.getClass().getSimpleName();

    private static CityApi cityApi = null;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;

    private String country;

    public void setCountry(String country) {
        this.country = country;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    @Override
    protected Integer doInBackground(String... params) {
        if(cityApi == null){
            CityApi.Builder builder = new CityApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            cityApi = builder.build();
        }
        try {
            cityApi.clearCities(params[0]);
            return 0;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(progress[0],City.class);
        }else{
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Log.d(TAG, "Clear cities from datastore!");
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(this.getClass().getSimpleName(),"resultListener is null!");
        }

    }
}
