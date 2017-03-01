package br.com.expressobits.hbus.ui.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.util.TimeUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 21/02/17
 */

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public final TextView textViewNewsTitle;
    public final TextView textViewNewsSubtitle;
    public final TextView textViewNewsBody;
    public final TextView textViewNewsTime;
    public final TextView textViewNewsSource;
    public final ImageView imageViewNewsMain;
    public final ImageView imageViewNewsIcon;
    public final LinearLayout linearLayoutNewsChips;
    //public final TextView textViewNewsUnread;
    public RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public NewsViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        textViewNewsTitle = (TextView) itemView.findViewById(R.id.textViewNewsTitle);
        textViewNewsSubtitle = (TextView) itemView.findViewById(R.id.textViewNewsSubtitle);
        textViewNewsBody = (TextView) itemView.findViewById(R.id.textViewNewsBody);
        textViewNewsTime = (TextView) itemView.findViewById(R.id.textViewNewsTime);
        textViewNewsSource = (TextView) itemView.findViewById(R.id.textViewNewsSource);
        //textViewNewsUnread = (TextView) itemView.findViewById(R.id.textViewNewsUnread);
        imageViewNewsMain = (ImageView) itemView.findViewById(R.id.imageViewNewsMain);
        imageViewNewsIcon = (ImageView) itemView.findViewById(R.id.imageViewNewsIcon);
        linearLayoutNewsChips = (LinearLayout) itemView.findViewById(R.id.linearLayoutNewsChips);

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

    @Override
    public boolean onLongClick(View v) {
        return recyclerViewOnClickListenerHack != null && recyclerViewOnClickListenerHack.onLongClickListener(v,getAdapterPosition());
    }

    public static void bindNews(LayoutInflater layoutInflater, Context context, NewsViewHolder newsViewHolder, News news) {
        String body = news.getBody();
        newsViewHolder.textViewNewsTitle.setText(news.getTitle());
        newsViewHolder.textViewNewsSubtitle.setText(news.getSubtitle());
        if(news.getImagesUrls().size()>0 && !news.getImagesUrls().get(0).isEmpty()){
            Picasso.with(context).load(news.getImagesUrls().get(0)).into(newsViewHolder.imageViewNewsIcon);
        }
        if(news.getImagesUrls().size()>1 && !news.getImagesUrls().get(1).isEmpty()){
            Picasso.with(context).load(news.getImagesUrls().get(1)).into(newsViewHolder.imageViewNewsMain);
        }
        newsViewHolder.textViewNewsTime.setText(TimeUtils.getTimeAgo(news.getTime(),context));
        newsViewHolder.textViewNewsSource.setText(news.getSource());

        for(int i=0;i<news.getImagesUrls().size();i++){
            if(news.getBody().contains("--"+ FirebaseUtils.NEWS_BODY_IMAGE_TAG+i+"--")){
                body = news.getBody().replace("--"+FirebaseUtils.NEWS_BODY_IMAGE_TAG+i+"--","");
            }
        }
        newsViewHolder.textViewNewsBody.setText(body);
        updateNewsChips(layoutInflater,newsViewHolder,news);
        /**if(!PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(NewsDetailsActivity.READ_PREFERENCE+"/"+news.getId(),false)){
            newsViewHolder.textViewNewsUnread.setVisibility(View.VISIBLE);
        }*/
    }

    private static void updateNewsChips(LayoutInflater layoutInflater, NewsViewHolder holderNews, News news){
        holderNews.linearLayoutNewsChips.removeAllViews();
        View viewCity;
        viewCity = layoutInflater.inflate(R.layout.item_news_chips,holderNews.linearLayoutNewsChips,false);
        TextView textViewCity = (TextView) viewCity.findViewById(R.id.textViewNewsChip);
        String city = FirebaseUtils.getNewsCityName(news.getId());
        if(city!=null){

            textViewCity.setText(city);
        }else {
            textViewCity.setText(layoutInflater.getContext().getString(R.string.pref_header_general));
        }
        holderNews.linearLayoutNewsChips.addView(viewCity);
        List<String> itinerariesIDs = news.getItineraryIds();
        if(itinerariesIDs!=null) {
            for (String itineraryId : itinerariesIDs) {
                View view;
                view = layoutInflater.inflate(R.layout.item_news_chips, holderNews.linearLayoutNewsChips, false);
                TextView textView = (TextView) view.findViewById(R.id.textViewNewsChip);
                String itineraryName = FirebaseUtils.getNewsItinerary(itineraryId);
                if (city != null) {


                    textView.setText(itineraryName);
                    textView.setSelected(true);
                    holderNews.linearLayoutNewsChips.addView(view);

                }
            }
        }

    }
}
