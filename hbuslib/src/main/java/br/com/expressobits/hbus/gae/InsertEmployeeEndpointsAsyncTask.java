package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import br.com.expressobits.hbus.backend.employeeApi.EmployeeApi;
import br.com.expressobits.hbus.backend.employeeApi.model.Employee;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael
 * @since 01/02/16
 */
public class InsertEmployeeEndpointsAsyncTask extends AsyncTask<Pair<Context,Employee>,Void,Employee>{

    private static EmployeeApi employeeApi = null;
    private Context context;

    @Override
    protected Employee doInBackground(Pair<Context, Employee>... params) {

        context = params[0].first;
        Employee employee = params[0].second;

        if(employeeApi == null) {  // Only do this once
            EmployeeApi.Builder builder = new EmployeeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            employeeApi = builder.build();
        }

        try {
            return employeeApi.insertEmployee(employee).execute();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Endpoints",e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Employee result) {
        Toast.makeText(context, result.getFirstName(), Toast.LENGTH_LONG).show();
    }
}
