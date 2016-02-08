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
import br.com.expressobits.hbusgenerator.R;

/**
 * Push city
 * @author Rafael
 * @since 03/02/16
 */
public class PushCitiesEndpointsAsyncTask extends AsyncTask<City,Integer,Integer>{

    private static CityApi cityApi = null;
    @NotNull
    private Context context;
    @NotNull
    private ProgressAsyncTask progressAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
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
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Toast.makeText(context,"Sucess push "+percent+" cities", Toast.LENGTH_LONG).show();
    }
}
