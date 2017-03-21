package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.List;

import br.com.expressobits.hbus.model.Code;

/**
 * @author Rafael
 * @since 17/03/17
 */

public class CodesListAdapter extends ArrayAdapter<Code> {

    private List<Code> codes;

    public CodesListAdapter(@NonNull Context context, @LayoutRes int resource,List<Code> codes) {
        super(context, resource);
        this.codes = codes;
    }

    @Override
    public int getCount() {
        return codes.size();
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Code code = codes.get(position);
        CheckedTextView checkedTextView = (CheckedTextView)convertView.findViewById(android.R.id.text1);
        checkedTextView.setText(code.getName());
        return super.getView(position, convertView, parent);
    }
}