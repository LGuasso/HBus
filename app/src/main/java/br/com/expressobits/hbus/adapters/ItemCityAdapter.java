package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * @author Rafael Correa
 * @since 18/10/15
 */
public class ItemCityAdapter extends RecyclerView.Adapter<ItemCityAdapter.HolderCity> {

    private Context context;
    private List<City> listCities;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ItemCityAdapter(Context context, List<City> lista){
        this.context = context;
        this.listCities = lista;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public HolderCity onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_city,viewGroup,false);
        HolderCity myViewHolder = new HolderCity(view,listCities);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(HolderCity holder, int position) {
        //TODO implementar imagem da cidade

        String name = listCities.get(position).getName()+" - "+listCities.get(position).getCountry();
        holder.textViewCity.setText(name);

        name=name.replace(" - ","-");
        name=name.replace(" ","_");
        name=name.toLowerCase();
        if(name.equals("santa_maria-rs")){
            holder.imageViewCity.setImageDrawable(context.getResources().getDrawable(R.drawable.santa_maria_rs));
        }
        if(name.equals("cruz_alta-rs")){
            holder.imageViewCity.setImageDrawable(context.getResources().getDrawable(R.drawable.cruz_alta_rs));
        }

        //holder.imageViewCity.setImageDrawable(listCities.get(position).getImageDrawable());

    }



    @Override
    public int getItemCount() {
        return listCities.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    public class HolderCity extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        public TextView textViewCity;
        public ImageView imageViewCity;
        public CardView cardView;

        public HolderCity(View itemView,List<City> list) {
            super(itemView);
            textViewCity = (TextView) itemView.findViewById(R.id.textView_city_name);
            imageViewCity = (ImageView) itemView.findViewById(R.id.imageView_city);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                return recyclerViewOnClickListenerHack.onLongClickListener(v, getPosition());
            }
            return false;
        }
    }

}