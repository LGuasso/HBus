package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.util.TimeUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * Classe que implementa cada item do Recycler view de noticias
 * @author Rafael Correa
 * @since 21/08/16
 */
public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.HolderNewsItinerary>{

    private Context context;
    private List<News> newses;
    private LayoutInflater layoutInflater;

    public ItemNewsAdapter(Context context,List<News> list){
        this.context = context;
        this.newses = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public HolderNewsItinerary onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_news,parent,false);
        HolderNewsItinerary holderNewsItinerary = new HolderNewsItinerary(view);
        return holderNewsItinerary;
    }

    @Override
    public void onBindViewHolder(HolderNewsItinerary holder, int position) {
        News news = newses.get(position);
        holder.textViewNewsTitle.setText(news.getTitle());
        holder.textViewNewsBody.setText(news.getBody());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String time = sdf.format(news.getTime());
        //TODO Imprimir formato do material design para tempo da notícia Ex. 5 min atras....
        if(!news.getImagesUrls().get(0).isEmpty()){
            Picasso.with(context).load(news.getImagesUrls().get(0)).into(holder.imageViewNewsMain);
        }
        holder.textViewNewsTime.setText(TimeUtils.getTimeAgo(news.getTime(),context));
        String city = FirebaseUtils.getNewsCityName(news.getId());
        if(city!=null){

            holder.textViewNewsId.setText(city);
        }else {
            holder.textViewNewsId.setText("Geral");
        }

        holder.textViewNewsSource.setText(news.getSource());

    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    public class HolderNewsItinerary extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewNewsTitle;
        public TextView textViewNewsBody;
        public TextView textViewNewsTime;
        public TextView textViewNewsSource;
        public TextView textViewNewsId;
        public ImageView imageViewNewsMain;

        public HolderNewsItinerary(View itemView){
            super(itemView);
            textViewNewsTitle = (TextView) itemView.findViewById(R.id.textViewNewsTitle);
            textViewNewsBody = (TextView) itemView.findViewById(R.id.textViewNewsBody);
            textViewNewsTime = (TextView) itemView.findViewById(R.id.textViewNewsTime);
            textViewNewsSource = (TextView) itemView.findViewById(R.id.textViewNewsSource);
            textViewNewsId = (TextView) itemView.findViewById(R.id.textViewNewsId);
            imageViewNewsMain = (ImageView) itemView.findViewById(R.id.imageViewNewsMain);
        }

        @Override
        public void onClick(View view) {

        }

    }
}
