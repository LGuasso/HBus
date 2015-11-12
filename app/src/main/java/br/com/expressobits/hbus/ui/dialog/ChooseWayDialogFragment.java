package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.OnSettingsListener;

/**
 * @author Rafael
 * @since 08/11/15
 *
 */
public class ChooseWayDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    public static final String TAG = "chooseDialog";
    List<String> ways;
    String itinerary;
    private ChooseWayDialogListener mCallback;

    public void setParameters(ChooseWayDialogListener mCallback,String itinerary,List<String> ways){
        this.itinerary = itinerary;
        this.ways = ways;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_way_list_item,ways);
        builder.setAdapter(arrayAdapter,this);
        // Create the AlertDialog object and return it

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mCallback.onItemClick(itinerary,ways.get(which));
    }
}
