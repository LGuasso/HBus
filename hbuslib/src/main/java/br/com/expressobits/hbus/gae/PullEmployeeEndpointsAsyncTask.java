package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

import br.com.expressobits.hbus.backend.employeeApi.EmployeeApi;
import br.com.expressobits.hbus.backend.employeeApi.model.Employee;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael
 * @since 03/02/16
 */
public class PullEmployeeEndpointsAsyncTask extends AsyncTask<Context,Void,List<Employee>>{


    private static EmployeeApi employeeApi = null;
    private Context context;

    @Override
    protected List<Employee> doInBackground(Context... params) {
        context = params[0];

        if(employeeApi == null) {  // Only do this once
            EmployeeApi.Builder builder = new EmployeeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            employeeApi = builder.build();
        }

        try {
            return employeeApi.getEmployees().execute().getItems();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Employee> employees) {
        Toast.makeText(context,"adsad", Toast.LENGTH_LONG).show();
    }
}
