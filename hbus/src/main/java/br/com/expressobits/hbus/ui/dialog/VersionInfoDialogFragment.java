package br.com.expressobits.hbus.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.expressobits.hbus.R;

/**
 * Classe que armazena informçaões para exibir caixa de diálogo com informações sobre o app e
 * versão do app
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
        String  versionInfo = "rafa";
        if (bundle != null) {
            versionInfo = getString(R.string.version)+" "+bundle.getString(KEY_VERSION_INFO);
        }else{
            Log.d(TAG,"Bundle arguments null!");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_version_info, null);
        TextView textView = (TextView)view.findViewById(R.id.textViewVersionInfo);
        textView.setText(versionInfo);
        builder.setView(view);
        return builder.create();
    }
}
