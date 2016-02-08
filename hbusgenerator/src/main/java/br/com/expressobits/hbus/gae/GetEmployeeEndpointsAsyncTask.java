package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import br.com.expressobits.hbus.backend.employeeApi.EmployeeApi;
import br.com.expressobits.hbus.backend.employeeApi.model.Employee;
import br.com.expressobits.hbusgenerator.R;

/**
 * Created by rafael on 02/02/16.
 */
public class GetEmployeeEndpointsAsyncTask extends AsyncTask<Context,Void,Employee> {

    private static EmployeeApi employeeApi = null;
    private Context context;
    @Override
    protected Employee doInBackground(Context... params) {
        context = params[0];

        if(employeeApi == null) {  // Only do this once
            EmployeeApi.Builder builder = new EmployeeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            employeeApi = builder.build();
        }

        try {
            return employeeApi.getEmployee("Rafael Correa").execute();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Employee result) {
        Toast.makeText(context, result.getAttendedHrTraining().toString(), Toast.LENGTH_LONG).show();
    }
}
