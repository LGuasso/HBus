package br.com.expressobits.hbus.ui.adapters.viewholder;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.provider.CompanyContract;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * @author Rafael Correa
 * @since 22/03/17
 */

public class CompanyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewCompanyName;
    private TextView textViewCompanyDescription;
    public RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public CompanyViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
        textViewCompanyDescription = (TextView) itemView.findViewById(R.id.textViewCompanyDescription);
    }

    public void setData(Cursor c) {
        textViewCompanyName.setText(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_NAME)));
        textViewCompanyDescription.setText(c.getString(c.getColumnIndexOrThrow(CompanyContract.Company.COLUMN_NAME_PHONENUMBER)));
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    @Override
    public void onClick(View v) {
        recyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());
    }
}
