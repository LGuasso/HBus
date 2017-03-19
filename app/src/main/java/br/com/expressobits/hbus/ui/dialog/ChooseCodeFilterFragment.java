package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * @author Rafael
 * @since 08/11/15
 *
 */
public class ChooseCodeFilterFragment extends AppCompatDialogFragment implements DialogInterface.OnClickListener,DialogInterface.OnMultiChoiceClickListener{

    public static final String TAG = "chooseCodeFilter";
    private String[] codes;
    private boolean[] codesChecked;
    private OnConfirmFilterCodes onConfirmFilterCodes;

    public void setParameters(OnConfirmFilterCodes onConfirmFilterCodes,String[] codes,boolean[] codesChecked){
        this.codes = codes;
        this.codesChecked = codesChecked;
        this.onConfirmFilterCodes = onConfirmFilterCodes;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        /**LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_choose_code_filter, null))
                // Add action buttons
                .setMultiChoiceItems(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
        return builder.create();*/

        builder.setTitle("Choose code view");
        //CodesListAdapter codesListAdapter = new CodesListAdapter(getContext(),android.R.layout.select_dialog_multichoice,codes);
        //builder.setAdapter(codesListAdapter,this);
        builder.setMultiChoiceItems(codes,codesChecked,this);
        // Create the AlertDialog object and return it
        return builder.create();
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        //onConfirmFilterCodes.onConfirmFilter(codes);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

    }


    public interface OnConfirmFilterCodes{
        public void onConfirmFilter(String[] codes,boolean[] codesChecked);
    }
}
