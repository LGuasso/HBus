package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

import br.com.expressobits.hbuslib.R;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.itineraryApi.ItineraryApi;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;

/**
 * Puxa itinerários do datastore do cloud do google apartir de uma
 * <b>Country</b> e <b>City</b> definidas como parâmetro.
 *
 * Executa essa simples tarefa.
 *
 * <b>Cuidado!</b>
 * <p>Deve ser definido o {@link Context}
 * antes de usar a instância desse método</p>
 *
 *
 * @author Rafael Correa
 * @since 08/02/16
 */
public class PullItinerariesEndpointsAsyncTask extends AsyncTask<City,Integer,List<Itinerary>>{

    private static final String TAG = PullItinerariesEndpointsAsyncTask.class.getClass().getSimpleName();

    private ItineraryApi itineraryApi;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Itinerary> resultListenerAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Itinerary> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected List<Itinerary> doInBackground(City... params) {
        if(itineraryApi == null){
            ItineraryApi.Builder builder = new ItineraryApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            itineraryApi = builder.build();
        }
        try {
            Log.d(TAG,"Pull ititneraries "+params[0].getCountry()+"-"+params[0].getName());
            return itineraryApi.getItineraries(params[0].getCountry(),params[0].getName()).execute().getItems();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<Itinerary> itineraries) {
        if(itineraries!=null){
            Log.d(TAG, "Download " + itineraries.size() + " itineraries from datastore!");
        }
        if(resultListenerAsyncTask!=null) {
            resultListenerAsyncTask.finished(itineraries);
        }else{
            Log.w(this.getClass().getSimpleName(), "resultListenerAsyncTask is null!");
        }

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(progress[0], Itinerary.class);
        }else{
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }

    }
}
