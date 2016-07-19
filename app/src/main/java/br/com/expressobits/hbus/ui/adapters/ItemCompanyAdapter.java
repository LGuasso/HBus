package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;

/**
 * @author Rafael Correa
 * @since 27/06/16
 */
public class ItemCompanyAdapter extends RecyclerView.Adapter<ItemCompanyAdapter.CompanyViewHolder>{

    private List<Company> companies;
    private Context context;
    private LayoutInflater layoutInflater;
    private String cityId;
    private String companySelected;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ItemCompanyAdapter(Context context,List<Company> companies){
        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.companies = companies;
        cityId = PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);
        companySelected = PreferenceManager.getDefaultSharedPreferences(context).getString(cityId,"SIMSM");
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_company,viewGroup,false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyViewHolder holder, int position) {
        Company company = companies.get(position);
        holder.textView1.setText(company.getName());
        holder.textView2.setText(company.getEmail());
        if(company.getName().equals(companySelected)){
            holder.imageView.setSelected(true);
        }

    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textView1;
        public TextView textView2;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.text1);
            textView2 = (TextView) itemView.findViewById(R.id.text2);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutItemList);
            imageView.setSelected(false);
            linearLayout.setOnClickListener(this);
            textView2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.text2){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,companies.get(getAdapterPosition()).getEmail());
                context.startActivity(Intent.createChooser(intent, "Send Email"));
            }else{
                recyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());
            }

        }
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }
}
