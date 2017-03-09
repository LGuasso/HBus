package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScroller;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * @author Rafael Correa
 * @since 27/06/16
 */
public class ItemCompanyAdapter extends RecyclerView.Adapter<ItemCompanyAdapter.CompanyViewHolder> implements FastScroller.SectionIndexer{

    private final List<Company> companies;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ItemCompanyAdapter(Context context,List<Company> companies){
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.companies = companies;
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_list_company,viewGroup,false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyViewHolder holder, int position) {
        Company company = companies.get(position);
        holder.textViewCompanyName.setText(company.getName());
        holder.textViewCompanyDescription.setText(company.getPhoneNumber()+" "+company.getEmail());
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    @Override
    public String getSectionText(int position) {
        return String.valueOf(companies.get(position).getName().charAt(0));
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView textViewCompanyName;
        final TextView textViewCompanyDescription;

        CompanyViewHolder(View itemView) {
            super(itemView);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
            textViewCompanyDescription = (TextView) itemView.findViewById(R.id.textViewCompanyDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());

        }
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }
}