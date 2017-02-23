package br.com.expressobits.hbus.ui.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael Correa
 * @since 21/02/17
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder{

    public TextView textViewHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        textViewHeader = (TextView) itemView.findViewById(R.id.textViewHeader);
    }
}
