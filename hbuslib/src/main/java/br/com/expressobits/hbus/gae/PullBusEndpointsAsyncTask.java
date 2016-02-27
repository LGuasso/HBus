package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.backend.busApi.BusApi;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.itineraryApi.ItineraryApi;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.TextUtils;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael
 * @since 12/02/16
 */
public class PullBusEndpointsAsyncTask extends AsyncTask<Pair<City,Itinerary>,Integer,List<Bus>>{

    private static final String TAG = PullBusEndpointsAsyncTask.class.getSimpleName();

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
            for(String way :itinerary.getWays()){
                for(int i=0;i<3;i++){
                    List<Bus> buses1 = busApi.getBuses(
                            city.getCountry(), city.getName(), itinerary.getName(), way, TextUtils.getTypeDayInt(i))
                            .execute().getItems();
                    if(buses1!=null){
                        buses.addAll(buses1);
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return null;
        }
        return buses;
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
