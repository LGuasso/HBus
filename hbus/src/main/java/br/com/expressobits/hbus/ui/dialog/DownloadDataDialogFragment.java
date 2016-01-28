package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.FirebaseDAO;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Caixa de dialogo para baixar informa��es do firebase
 * @author Rafael
 * @since 10/01/16.
 */
public class DownloadDataDialogFragment extends DialogFragment{

    public static String TAG = "DownloadData";
    private Context context;
    private City city;
    private BusDAO db;
    private ProgressBar progressBar;
    private TextView textView;
    private List<Itinerary> itineraries= new ArrayList<>();
    private FinishListener finishListener;
    private List<Code> codes = new ArrayList<>();
    private List<Bus> buses = new ArrayList<>();
    private FirebaseDAO dao;

    //TODO implementar feedback de timeElapse
    private Long timeElapse;

    public void setParameters(Context context,City city){
        this.context = context;
        this.city = city;
    }

    public void addFinishListener(FinishListener finishListener){
        this.finishListener = finishListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        dao = new FirebaseDAO("https://hbus.firebaseio.com/");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_download_data, null);
        textView = (TextView)view.findViewById(R.id.textViewTitle);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        textView.setText(getString(R.string.downloading_data) +" "+ city);
        builder.setView(view);
        downloadItineraries(city);
        return builder.create();
    }

    public void downloadItineraries(City city){
        db  = new BusDAO(context);
        readItineraries(city);
    }

    public void readItineraries(final City city){
        timeElapse = System.currentTimeMillis();
        dao.getItineraries(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Itinerary itinerary = postSnapshot.getValue(Itinerary.class);
                    itinerary.setId(FirebaseUtils.getIdItinerary(city, itinerary));
                    itineraries.add(itinerary);

                }
                for (int i = 0; i < itineraries.size(); i++) {
                    db.insert(itineraries.get(i));
                    for(String way:itineraries.get(i).getWays()){
                        for(TypeDay typeday: TypeDay.values()){
                            readBus(city, itineraries.get(i), way, typeday.toString());
                        }
                    }
                }

                readCodes(city);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }, city);
    }

    public void readCodes(final City city){
        dao.getCodes(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Code code = postSnapshot.getValue(Code.class);
                    code.setId(FirebaseUtils.getIdCode(city, code));
                    codes.add(code);

                }
                for (int i = 0; i < codes.size(); i++) {
                    db.insert(codes.get(i));
                }
                Log.i(TAG,"Time for download data info "+(System.currentTimeMillis() - timeElapse));
                finishListener.onFinish();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }, city);
    }

    public void readBus(final City city,final Itinerary itinerary, final String way, final String typeday){
        dao.getBus(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Bus> buses = new ArrayList<Bus>();

                Log.d(TAG,"Downloaded "+city.getName()+" "+itinerary.getName()+" "+way+" "+typeday);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Bus bus = postSnapshot.getValue(Bus.class);
                    bus.setId(FirebaseUtils.getIdBus(city, itinerary, way,typeday,bus.getTime()));
                    buses.add(bus);
                }
                for (int i = 0; i < buses.size(); i++) {
                    db.insert(buses.get(i));
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }, city,itinerary,way,typeday);
    }



}
