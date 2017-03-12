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

public class HotTipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    public TextView textViewTitle;
    public TextView textViewMessage;
    public ImageView imageView;
    public Button button1;
    public Button button2;

    public HotTipViewHolder(View itemView) {
        super(itemView);
        button1 = (Button) itemView.findViewById(R.id.buttonHotTip1);
        button2 = (Button) itemView.findViewById(R.id.buttonHotTip2);
        textViewTitle = (TextView) itemView.findViewById(R.id.cardViewEmptyStateTextViewTitle);
        textViewMessage = (TextView) itemView.findViewById(R.id.cardViewEmptyStateTextViewMessage);
        imageView = (ImageView) itemView.findViewById(R.id.cardViewEmptyStateImageView);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
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
