package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import br.com.expressobits.hbus.model.Code;

/**
 * @author Rafael Correa
 * @since 24/04/16
 */
public class SaveCodesAsyncTask extends AsyncTask<Pair<String,List<Code>>,Integer,Integer>{


    private static final String TAG  = "SaveCodesDAO";
    private Context context;
    private List<Code> codes;

    private boolean isFinished;
    private FinishSaveDAO finishSaveDAO;


    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinishSaveDAO(FinishSaveDAO finishSaveDAO) {
        this.finishSaveDAO = finishSaveDAO;
    }

    /** TODO implementar uma noificação para mostrar que está salvando as */



    @Override
    protected Integer doInBackground(Pair<String,List<Code>>[] pairs) {

        String cityId = pairs[0].first;
        codes = pairs[0].second;
        BusDAO db = new BusDAO(context);
        //int result = db.deleteCode(cityId);
        //Log.d(TAG, "result delete itineraries " + result);
        for (int i = 0;i<codes.size();i++){
            Code code = codes.get(i);
            Log.d(TAG,"code "+code.getName()+" load from datastore ways"+code.getDescrition());
            db.insert(code);
            publishProgress(i);
        }
        Log.i(TAG, codes.toString());
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        isFinished = true;
        finishSaveDAO.finish();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        finishSaveDAO.progressUpdate(codes.get(0),values[0],codes.size());
        super.onProgressUpdate(values);
    }
}
