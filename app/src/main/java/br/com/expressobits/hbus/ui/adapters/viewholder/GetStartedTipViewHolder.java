package br.com.expressobits.hbus.ui.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * @author Rafael Correa
 * @since 06/03/17
 */

public class GetStartedTipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    public Button buttonSeeItineraries;
    public TextView textViewTitle;
    public TextView textViewMessage;
    public ImageView imageView;

    public GetStartedTipViewHolder(View itemView) {
        super(itemView);
        buttonSeeItineraries = (Button) itemView.findViewById(R.id.cardViewEmptyStateButton);
        textViewTitle = (TextView) itemView.findViewById(R.id.cardViewEmptyStateTextViewTitle);
        textViewMessage = (TextView) itemView.findViewById(R.id.cardViewEmptyStateTextViewMessage);
        imageView = (ImageView) itemView.findViewById(R.id.cardViewEmptyStateImageView);
        buttonSeeItineraries.setOnClickListener(this);
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
}
