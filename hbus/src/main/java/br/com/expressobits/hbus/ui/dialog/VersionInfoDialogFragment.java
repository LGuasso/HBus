package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael
 * @since 02/01/16.
 */
public class VersionInfoDialogFragment extends DialogFragment {

    public static String TAG = "VersionInfo";
    public static String KEY_VERSION_INFO = "VERSION_INFO";



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Bundle bundle = getArguments();
        String  versionInfo = "";
        if (bundle != null) {
            versionInfo = getString(R.string.version)+" "+bundle.getString(KEY_VERSION_INFO);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_version_info, null);
        TextView textView = (TextView)view.findViewById(R.id.textViewVersionInfo);
        textView.setText(versionInfo);
        builder.setView(view);
        return builder.create();
    }
}
