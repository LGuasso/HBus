package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.gae.PullBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.ResultListenerAsyncTask;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * Caixa de dialogo para baixar informações do firebase
 * @author Rafael
 * @since 10/01/16.
 */
public class DownloadDataDialogFragment extends DialogFragment implements ResultListenerAsyncTask<Itinerary>{

    public static String TAG = "DownloadData";
    private Context context;
    private City city;
    private BusDAO db;
    private ProgressBar progressBar;
    private TextView textView;
    private List<Itinerary> itineraries= new ArrayList<>();
    private FinishListener finishListener;
    private List<Code> codes = new ArrayList<>();
    private HashMap<Itinerary,Bus> buses = new HashMap<>();
    public static int count=0;

    //TODO implementar feedback de timeElapse
    private Long timeElapse;

    public void setParameters(FinishListener finishListener,Context context,City city){
        this.context = context;
        this.city = city;
        this.finishListener = finishListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_download_data, null);
        textView = (TextView)view.findViewById(R.id.textViewMessage);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        textView.setText(getString(R.string.downloading_data, city));
        builder.setView(view);
        pullData();
        return builder.create();
    }

    public void pullData(){
        timeElapse = System.currentTimeMillis();
        PullItinerariesEndpointsAsyncTask pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
        pullItinerariesEndpointsAsyncTask.setContext(context);
        pullItinerariesEndpointsAsyncTask.setResultListenerAsyncTask(this);
        pullItinerariesEndpointsAsyncTask.execute(city);
    }


    @Override
    public void finished(List<Itinerary> itineraries) {

        Log.d(TAG,"Time elapsed in datastore push and save in database local "+
                HoursUtils.longTimetoString(System.currentTimeMillis() - timeElapse));

        BusDAO dao = new BusDAO(context);
        for (Itinerary itinerary:itineraries){
            //dao.insert(itinerary);
        }

        /**for(final Itinerary itinerary:itineraries){
            PullBusEndpointsAsyncTask pullBusEndpointsAsyncTask = new PullBusEndpointsAsyncTask();
            pullBusEndpointsAsyncTask.setContext(context);
            pullBusEndpointsAsyncTask.setResultListenerAsyncTask(new ResultListenerAsyncTask<Bus>() {
                @Override
                public void finished(List<Bus> buses) {
                    Log.d("ULTRA TEST",buses.get(0).getWay()+"    "+itinerary+" count="+(count++));

                }
            });
            pullBusEndpointsAsyncTask.execute(new Pair<City, Itinerary>(city,itineraries.get(0)));

        }*/

        Log.d(TAG,"Time elapsed in datastore push and save in database local "+
                HoursUtils.longTimetoString(System.currentTimeMillis() - timeElapse));
        finishListener.onFinish();

    }
}

