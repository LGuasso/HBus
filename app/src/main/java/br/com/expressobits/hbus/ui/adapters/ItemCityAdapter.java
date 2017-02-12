package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 18/10/15
 */
public class ItemCityAdapter extends RecyclerView.Adapter<ItemCityAdapter.HolderCity> {

    private Context context;
    private List<City> listCities;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private static final String TAG = "ItemCityAdapter";

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
    public void onBindViewHolder(final HolderCity holder, final int position) {
        String name = listCities.get(position).getName()+" - "+listCities.get(position).getCountry();
        holder.textViewCity.setText(name);

        if(listCities.get(position).isActived() || PreferenceManager.getDefaultSharedPreferences(context).getBoolean("no_actived_itens",false)){
            holder.textViewComingSoon.setVisibility(View.INVISIBLE);
            holder.cardView.setOnClickListener(holder);
        }else {
            holder.imageViewCity.setColorFilter(context.getResources().getColor(R.color.md_blue_gray_500), PorterDuff.Mode.MULTIPLY);
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FirebaseUtils.REF_STORAGE_HBUS);
        StorageReference tableRef = storageRef.child(FirebaseUtils.CITY_TABLE);
        StorageReference countryRef = tableRef.child(listCities.get(position).getCountry());
        StorageReference cityRef = countryRef.child(listCities.get(position).getName().toLowerCase().replace(" ","_")+FirebaseUtils.EXTENSION_IMAGE);

        StorageReference cityFlagRef = countryRef.child(listCities.get(position).getName().toLowerCase().replace(" ","_")
                +FirebaseUtils.FLAG_TEXT_FILE+FirebaseUtils.EXTENSION_IMAGE);



        cityRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(ItemCityAdapter.this.context).load(uri)
                        .into(holder.imageViewCity);
            }


        });

        cityFlagRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(ItemCityAdapter.this.context).load(uri)
                        .error(R.drawable.ic_flag_white_48dp)
                        .placeholder(R.drawable.ic_flag_white_48dp)
                        .into(holder.imageViewPhoto);
                Log.i(TAG,"Load image "+uri.getPath());
            }
        });

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
        public TextView textViewComingSoon;
        public ImageView imageViewCity;
        public CardView cardView;
        public ImageView imageViewPhoto;

        public HolderCity(View itemView,List<City> list) {
            super(itemView);
            textViewCity = (TextView) itemView.findViewById(R.id.textView_city_name);
            textViewComingSoon = (TextView) itemView.findViewById(R.id.textView_coming_soon);
            imageViewCity = (ImageView) itemView.findViewById(R.id.imageView_city);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.circleImageViewCityFlag);
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
