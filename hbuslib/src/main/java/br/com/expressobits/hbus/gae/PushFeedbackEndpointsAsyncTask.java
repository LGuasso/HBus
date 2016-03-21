package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;

import br.com.expressobits.hbus.backend.cityApi.CityApi;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.feedbackApi.FeedbackApi;
import br.com.expressobits.hbus.backend.feedbackApi.model.Feedback;
import br.com.expressobits.hbuslib.R;

/**
 * @author Rafael Correa
 * @since 12/02/16
 */
public class PushFeedbackEndpointsAsyncTask extends AsyncTask<Feedback,Integer,Integer>{

    private static final String TAG = PushFeedbackEndpointsAsyncTask.class.getClass().getSimpleName();

    private static FeedbackApi feedbackApi = null;

    private Context context;

    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Integer> resultListenerAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Integer> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected Integer doInBackground(Feedback... params) {
        if(feedbackApi == null) {  // Only do this once
            FeedbackApi.Builder builder = new FeedbackApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            feedbackApi = builder.build();
        }

        for (int i=0;i<params.length;i++) {
            Feedback feedback = params[i];
            try {
                feedbackApi.insertFeedback(feedback).execute();
                Log.i(TAG, "Push feedback " + feedback.getMessage());

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
            progressAsyncTask.setProgressUdate(progress[0],Feedback.class);
        }else{
            Log.w(TAG,"progressAsyncTask is null!");
        }

    }

    @Override
    protected void onPostExecute(Integer percent) {
        Log.d(TAG, "Send cities from datastore!");
        if(resultListenerAsyncTask!=null){
            resultListenerAsyncTask.finished(new ArrayList<Integer>(0));
        }else{
            Log.w(TAG,"resultListenerAsyncTask is null!");
        }

    }
}
