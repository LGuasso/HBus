package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.firebase.client.annotations.NotNull;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.R;

/**
 * Puxa cidades do datastore apartir de uma country definida
 * @author Rafael Correa
 * @since 05/02/16
 */
public class PullCitiesEndpointsAsyncTask extends AsyncTask<Pair<Context,String>,Integer,List<City>>{

    private static CityApi cityApi = null;
    @NotNull
    private Context context;
    @NotNull
    private ProgressAsyncTask progressAsyncTask;

    private ResultAsyncTask<City> resultAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultAsyncTask(ResultAsyncTask<City> resultAsyncTask) {
        this.resultAsyncTask = resultAsyncTask;
    }

    @Override
    protected List<City> doInBackground(Pair<Context, String>... params) {

        context = params[0].first;

        if(cityApi == null){
            CityApi.Builder builder = new CityApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            cityApi = builder.build();
        }
        try {
            return cityApi.getCities(params[0].second).execute().getItems();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        //Toast.makeText(context,"Download "+cities.size()+" cities from datastore!", Toast.LENGTH_LONG).show();
        resultAsyncTask.finished(cities);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        progressAsyncTask.setProgressUdate(progress[0], City.class);
    }


}
