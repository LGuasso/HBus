package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.adapters.viewholder.CompanyViewHolder;

/**
 * @author Rafael Correa
 * @since 22/03/17
 */

public class ItemCompanyCursorRecyclerViewAdapter extends CursorRecyclerViewAdapter {

    public ItemCompanyCursorRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        CompanyViewHolder holder = (CompanyViewHolder) viewHolder;
        holder.setRecyclerViewOnClickListenerHack(this);
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }




    @Override
    public void onClickListener(View view, int position) {
        recyclerViewOnClickListenerHack.onClickListener(view,position);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
