package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;

/**
 * @author Rafael Correa
 * @since 24/04/16
 */
public class SaveItinerariesAsyncTask extends AsyncTask<Pair<String,List<Itinerary>>,Integer,Integer>{


    private static final String TAG  = "SaveIntinerariesDAO";
    private Context context;
    private boolean isFinished;
    private FinishSaveDAO finishSaveDAO;
    List<Itinerary> list;

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
    protected Integer doInBackground(Pair<String,List<Itinerary>>[] pairs) {

        String cityId = pairs[0].first;
        list = pairs[0].second;
        BusDAO db = new BusDAO(context);
        for (int i=0;i<list.size();i++){
            Itinerary itinerary = list.get(i);
            Log.d(TAG,"itinerary "+itinerary.getName()+" load from datastore ways"+itinerary.getWays());
            db.insert(itinerary);
            publishProgress(i);
        }
        Log.i(TAG, list.toString());
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
        finishSaveDAO.progressUpdate(list.get(0),values[0],list.size());
        super.onProgressUpdate(values);
    }
}
