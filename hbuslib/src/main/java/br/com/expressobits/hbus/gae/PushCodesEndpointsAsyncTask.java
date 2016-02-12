package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;

import br.com.expressobits.hbus.backend.codeApi.CodeApi;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbuslib.R;

/**
 *
 * @author Rafael
 * @since 12/02/16
 */
public class PushCodesEndpointsAsyncTask extends AsyncTask<Code,Integer,Integer>{

    private static final String TAG = PushCodesEndpointsAsyncTask.class.getSimpleName();

    private static CodeApi codeApi;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;

    private String cityName;

    private String country;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Integer> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    protected Integer doInBackground(Code... params) {
        if(codeApi == null){
            CodeApi.Builder builder = new CodeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            codeApi = builder.build();
        }

        for (int i=0;i<params.length;i++) {
            Code code = params[i];
            try {
                codeApi.insertCode(country, cityName,code).execute();

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
            progressAsyncTask.setProgressUdate(progress[0],Code.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Log.d(TAG, "Send codes from datastore!");
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }

    }


}
