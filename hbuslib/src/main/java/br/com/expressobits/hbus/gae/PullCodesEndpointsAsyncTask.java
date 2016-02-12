package br.com.expressobits.hbus.gae;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.codeApi.CodeApi;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbuslib.R;

/**
 * Puxa códigos do datastore do cloud do google apartir de uma
 * <b>Country</b> e <b>City</b> definidas como parâmetro.
 *
 * Executa essa simples tarefa.
 *
 * <b>Cuidado!</b>
 * <p>Deve ser definido o {@link Context}
 * antes de usar a instância desse método</p>
 *
 *
 * @author Rafael
 * @since 12/02/16
 */
public class PullCodesEndpointsAsyncTask extends AsyncTask<City,Integer,List<Code>>{

    private static final String TAG = PullCodesEndpointsAsyncTask.class.getClass().getSimpleName();

    private CodeApi codeApi;

    private Context context;

    //TODO ver sobre ProgressListener
    private ProgressAsyncTask progressAsyncTask;

    private ResultListenerAsyncTask<Code> resultListenerAsyncTask;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProgressAsyncTask(ProgressAsyncTask progressAsyncTask) {
        this.progressAsyncTask = progressAsyncTask;
    }

    public void setResultListenerAsyncTask(ResultListenerAsyncTask<Code> resultListenerAsyncTask) {
        this.resultListenerAsyncTask = resultListenerAsyncTask;
    }

    @Override
    protected List<Code> doInBackground(City... params) {
        if(codeApi == null){
            CodeApi.Builder builder = new CodeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.api_google_url));
            // end options for devappserver

            codeApi = builder.build();
        }try{
            return codeApi.getCodes(params[0].getCountry(),params[0].getName())
                    .execute().getItems();
        }catch (IOException e){
            e.printStackTrace();
            Log.e("Endpoints",e.getMessage());
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progressAsyncTask!=null){
            progressAsyncTask.setProgressUdate(progress[0], Code.class);
        }else{
            Log.w(this.getClass().getSimpleName(),"progressAsyncTask is null!");
        }
    }

    @Override
    protected void onPostExecute(List<Code> codes) {
        if(resultListenerAsyncTask!=null) {
            Log.d(TAG, "Download " + codes.size() + " codes from datastore!");
            resultListenerAsyncTask.finished(codes);
        }else{
            Log.w(this.getClass().getSimpleName(), "resultListenerAsyncTask is null!");
        }
    }
}
