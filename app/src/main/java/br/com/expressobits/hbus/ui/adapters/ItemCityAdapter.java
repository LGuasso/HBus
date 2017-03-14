package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
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
public class ItemCityAdapter extends RecyclerView.Adapter<ItemCityAdapter.HolderCity> implements FastScrollRecyclerView.SectionedAdapter{

    private final Context context;
    private final List<City> listCities;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private static final String TAG = "ItemCityAdapter";

    public ItemCityAdapter(Context context, List<City> list){
        this.context = context;
        this.listCities = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public HolderCity onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_list_city,viewGroup,false);
        return new HolderCity(view);
    }



    @Override
    public void onBindViewHolder(final HolderCity holder, final int position) {
        String name = listCities.get(position).getName();
        holder.textViewCity.setText(name);

        if(listCities.get(position).isActived() || PreferenceManager.getDefaultSharedPreferences(context).getBoolean("no_actived_items",false)){
            holder.cardView.setOnClickListener(holder);
        }else {
            holder.imageViewCityPhoto.setColorFilter(ContextCompat.getColor(context,R.color.md_blue_gray_500), PorterDuff.Mode.MULTIPLY);
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FirebaseUtils.REF_STORAGE_HBUS);
        StorageReference imageRef = storageRef.child(FirebaseUtils.REF_STORAGE_HBUS_IMAGE);
        StorageReference tableRef = imageRef.child(FirebaseUtils.CITY_TABLE);
        StorageReference countryRef = tableRef.child(listCities.get(position).getCountry());
        StorageReference cityRef = countryRef.child(listCities.get(position).getName());

        StorageReference cityProfileRef = cityRef.child(FirebaseUtils.IMAGE_CITY_PHOTO_FILE +FirebaseUtils.EXTENSION_IMAGE_JPG);
        StorageReference cityFlagRef = cityRef.child(FirebaseUtils.IMAGE_CITY_COATS_OF_ARMS_FILE +FirebaseUtils.EXTENSION_IMAGE_PNG);

        cityProfileRef.getDownloadUrl().addOnSuccessListener(uri -> {

            Picasso.with(ItemCityAdapter.this.context).load(uri)
                    .placeholder(R.drawable.default_city)
                    .into(holder.imageViewCityPhoto);
        });

        cityFlagRef.getDownloadUrl().addOnSuccessListener(uri -> {

            Picasso.with(ItemCityAdapter.this.context).load(uri)
                    .error(R.drawable.ic_flag_white_48dp)
                    .placeholder(R.drawable.ic_shield_grey600_24dp)
                    .into(holder.imageViewCityCoatsOfArms);
        });

    }

    @Override
    public int getItemCount() {
        return listCities.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(listCities.get(position).getName().charAt(0));
    }

    class HolderCity extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        final TextView textViewCity;
        final ImageView imageViewCityPhoto;
        final CardView cardView;
        final ImageView imageViewCityCoatsOfArms;

        HolderCity(View itemView) {
            super(itemView);
            textViewCity = (TextView) itemView.findViewById(R.id.textView_city_name);
            imageViewCityPhoto = (ImageView) itemView.findViewById(R.id.imageViewCityPhoto);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imageViewCityCoatsOfArms = (ImageView) itemView.findViewById(R.id.circleImageViewCityCoatsOfArms);
        }



        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            return recyclerViewOnClickListenerHack != null && recyclerViewOnClickListenerHack.onLongClickListener(v, getAdapterPosition());
        }
    }

}
