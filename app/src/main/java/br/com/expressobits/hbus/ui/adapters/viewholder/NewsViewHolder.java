package br.com.expressobits.hbus.ui.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * @author Rafael Correa
 * @since 21/02/17
 */

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public final TextView textViewNewsTitle;
    public final TextView textViewNewsSubtitle;
    public final TextView textViewNewsBody;
    public final TextView textViewNewsTime;
    public final TextView textViewNewsSource;
    public final ImageView imageViewNewsMain;
    public final LinearLayout linearLayoutNewsChips;
    public RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public NewsViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        textViewNewsTitle = (TextView) itemView.findViewById(R.id.textViewNewsTitle);
        textViewNewsSubtitle = (TextView) itemView.findViewById(R.id.textViewNewsSubtitle);
        textViewNewsBody = (TextView) itemView.findViewById(R.id.textViewNewsBody);
        textViewNewsTime = (TextView) itemView.findViewById(R.id.textViewNewsTime);
        textViewNewsSource = (TextView) itemView.findViewById(R.id.textViewNewsSource);
        imageViewNewsMain = (ImageView) itemView.findViewById(R.id.imageViewNewsMain);
        linearLayoutNewsChips = (LinearLayout) itemView.findViewById(R.id.linearLayoutNewsChips);

    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    @Override
    public void onClick(View v) {
        if(recyclerViewOnClickListenerHack != null){
            recyclerViewOnClickListenerHack.onClickListener(v,this.getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return recyclerViewOnClickListenerHack != null && recyclerViewOnClickListenerHack.onLongClickListener(v,getAdapterPosition());
    }
}
