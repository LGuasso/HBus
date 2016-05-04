package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbuslib.R;

/**
 * Puxa cidades do datastore apartir de uma country definida
 * @author Rafael Correa
 * @since 05/02/16
 */
public class PullCitiesEndpointsAsyncTask extends AsyncTask<String,Integer,List<City>>{

    private static final String TAG = PullCitiesEndpointsAsyncTask.class.getSimpleName();

    private static CityApi cityApi = null;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<City> resultListenerAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<City> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected List<City> doInBackground(String... params) {

        if(cityApi == null){
            CityApi.Builder builder = new CityApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            cityApi = builder.build();
        }
        try {
            return cityApi.getCities(params[0]).execute().getItems();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        if(cities!=null){
            Log.d(TAG, "Download " + cities.size() + " cities from datastore!");
        }
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(cities);
        }else{
            Log.w(this.getClass().getSimpleName(), "resultListenerAsyncTask is null!");
        }

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(progress[0], City.class);
        }else {
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }

    }


}
