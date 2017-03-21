package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Code;

/**
 * @author Rafael
 * @since 08/11/15
 *
 */
public class ChooseCodeFilterFragment extends AppCompatDialogFragment implements DialogInterface.OnClickListener,DialogInterface.OnMultiChoiceClickListener{

    public static final String TAG = "chooseCodeFilter";
    private List<Code> codes;
    private List<Code> codesNoSelected;
    private OnConfirmFilterCodes onConfirmFilterCodes;

    /**
     * Define parâmetros necessários para iniciar o AlertDialog
     * @param onConfirmFilterCodes ouvinte com resposta de onDismiss ou onClick
     * @param codes ArrayList of all codes avaible from itinerary
     * @param codesSelected ArrayList of codes enable in filter from itinerary
     */
    public void setParameters(OnConfirmFilterCodes onConfirmFilterCodes,List<Code> codes,
                              List<Code> codesSelected){
        this.codes = codes;
        this.codesNoSelected = codesSelected;
        this.onConfirmFilterCodes = onConfirmFilterCodes;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        if(codes.size()>0){
            builder.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_filter_list_grey_600_24dp));
            builder.setPositiveButton(R.string.filter,this);
            builder.setTitle(R.string.filter_the_buses_for_your_code);
            String[] codesName = new String[codes.size()];
            boolean[] codesChecked = new boolean[codes.size()];
            for (int i = 0; i < codes.size(); i++) {
                codesName[i] = codes.get(i).getName()+" - "+codes.get(i).getDescrition();
                codesChecked[i] = !codesNoSelected.contains(codes.get(i));
            }

            builder.setMultiChoiceItems(codesName,codesChecked,this);
            // Create the AlertDialog object and return it
        }else{
            builder.setMessage(R.string.there_are_no_codes_to_filter_on_this_itinerary);
            builder.setTitle(R.string.no_codes);
            builder.setPositiveButton(android.R.string.ok,this);
        }

        return builder.create();
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        onConfirmFilterCodes.onConfirmFilter(codesNoSelected);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        Code code = codes.get(which);
        Log.d(TAG,code.getName());
        if(isChecked && codesNoSelected.contains(code)){
            codesNoSelected.remove(code);
        }else if(!isChecked && !codesNoSelected.contains(code)){
            codesNoSelected.add(code);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onConfirmFilterCodes.onConfirmFilter(codesNoSelected);
    }

    public interface OnConfirmFilterCodes{
        void onConfirmFilter(List<Code> codesSelected);
    }
}
