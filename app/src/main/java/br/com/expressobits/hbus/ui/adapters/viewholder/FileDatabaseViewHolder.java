package br.com.expressobits.hbus.ui.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * @author Rafael Correa
 * @since 06/03/17
 */

public class FileDatabaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textViewFileName;
    public TextView textViewFileSize;
    public ImageButton imageButtonDelete;
    public RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public FileDatabaseViewHolder(View itemView) {
        super(itemView);
        imageButtonDelete = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteDatabase);
        textViewFileName = (TextView) itemView.findViewById(R.id.textViewFileName);
        textViewFileSize = (TextView) itemView.findViewById(R.id.textViewFileSize);
        imageButtonDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        recyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }
}
