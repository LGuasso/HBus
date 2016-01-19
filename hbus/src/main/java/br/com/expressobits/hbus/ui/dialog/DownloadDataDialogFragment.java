package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FirebaseDAO;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * Caixa de dialogo para baixar informações do firebase
 * @author Rafael
 * @since 10/01/16.
 */
public class DownloadDataDialogFragment extends DialogFragment{

    public static String TAG = "DownloadData";
    public static String KEY_CITY_ID = "CITY_ID";
    private Context context;
    private Long cityId = 0l;
    private TimesDbHelper db;
    private ProgressBar progressBar;
    private TextView textView;
    private List<Itinerary> itineraries= new ArrayList<>();
    private FinishListener finishListener;
    private List<Code> codes = new ArrayList<>();
    private FirebaseDAO dao;

    public void setParameters(Context context,Long cityId){
        this.context = context;
        this.cityId = cityId;
    }

    public void addFinishListener(FinishListener finishListener){
        this.finishListener = finishListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityId = bundle.getLong(KEY_CITY_ID);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_download_data, null);
        textView = (TextView)view.findViewById(R.id.textViewTitle);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        textView.setText(getString(R.string.downloading_data)+cityId);
        builder.setView(view);
        downloadItineraries();
        return builder.create();
    }

    public void downloadItineraries(){
        progressBar.setProgress(0);
        progressBar.setMax(100);
        db  = new TimesDbHelper(context);
        db.deleteAllItineraries();
        dao = new FirebaseDAO("https://hbus.firebaseio.com/");
        readItineraries();
    }

    public void readItineraries(){
        dao.getItineraries(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Itinerary itinerary = postSnapshot.getValue(Itinerary.class);
                        itineraries.add(itinerary);

                }
                for (int i = 0; i < itineraries.size(); i++) {
                    int progress = itineraries.size() * 100 / (i + 1);
                    progressBar.setProgress(progress);
                    textView.setText(progress + " %");
                    db.insert(itineraries.get(i));
                }
                readCodes();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void readCodes(){
        dao.getCodes(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Code code = postSnapshot.getValue(Code.class);
                        codes.add(code);

                }
                for(int i=0;i<codes.size();i++){
                    int progress = codes.size()*100/(i+1);
                    progressBar.setProgress(progress);
                    textView.setText(progress+" %");
                    db.insert(codes.get(i));
                }
                if(finishListener!=null){
                    finishListener.onFinish();
                }

                //readCodes();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
