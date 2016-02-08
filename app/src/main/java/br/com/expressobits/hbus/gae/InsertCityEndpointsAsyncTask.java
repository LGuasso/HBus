package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.myApi.MyApi;
import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.R;

/**
 * Tarefa de inserção de cidades
 * @author Rafael
 * @since 31/01/16
 */
public class InsertCityEndpointsAsyncTask extends AsyncTask<Pair<Context, City>, Void, City> {

    private static CityApi cityApiService = null;
    private Context context;

    @Override
    protected City doInBackground(Pair<Context, City>... params) {


        context = params[0].first;
        City city = params[0].second;

        if(cityApiService == null) {  // Only do this once
            CityApi.Builder builder = new CityApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            cityApiService = builder.build();
        }

        ArrayList<City> cities = new ArrayList<>();

        try {
            return cityApiService.insertCity(city).execute();
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(City result) {
        Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
    }

}
