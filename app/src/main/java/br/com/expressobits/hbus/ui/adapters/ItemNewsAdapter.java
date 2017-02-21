package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.news.NewsDetailsActivity;
import br.com.expressobits.hbus.util.TimeUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Class that implements each item in the Recycler news view
 * @author Rafael Correa
 * @since 21/08/16
 */
public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.HolderNews>{

    private final Context context;
    private final List<News> newses;
    private final LayoutInflater layoutInflater;

    public ItemNewsAdapter(Context context,List<News> list){
        this.context = context;
        this.newses = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public HolderNews onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_news,parent,false);
        return new HolderNews(view);
    }

    @Override
    public void onBindViewHolder(HolderNews holder, int position) {
        //List<String> urlsActivedImages = new ArrayList<>();
        News news = newses.get(position);
        //String body = news.getBody();
        holder.textViewNewsTitle.setText(news.getTitle());
        holder.textViewNewsSubtitle.setText(news.getSubtitle());
        if(!news.getImagesUrls().get(0).isEmpty()){
            Picasso.with(context).load(news.getImagesUrls().get(0)).into(holder.imageViewNewsMain);
        }
        holder.textViewNewsTime.setText(TimeUtils.getTimeAgo(news.getTime(),context));
        holder.textViewNewsSource.setText(news.getSource());

        /**for(int i=0;i<news.getImagesUrls().size();i++){
            String url = news.getImagesUrls().get(i);
            if(news.getBody().contains("--"+FirebaseUtils.NEWS_BODY_IMAGE_TAG+i+"--")){
                urlsActivedImages.add(url);
                //body = news.getBody().replace("--"+FirebaseUtils.NEWS_BODY_IMAGE_TAG+i+"--","");
            }
        }*/
        //holder.textViewNewsBody.setText(body);
        //getImageList(holder,urlsActivedImages);
        updateNewsChips(holder,news);

    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    private void updateNewsChips(HolderNews holderNews, News news){
        holderNews.linearLayoutNewsChips.removeAllViews();
        View viewCity;
        viewCity = layoutInflater.inflate(R.layout.item_news_chips,holderNews.linearLayoutNewsChips,false);
        TextView textViewCity = (TextView) viewCity.findViewById(R.id.textViewNewsChip);
        String city = FirebaseUtils.getNewsCityName(news.getId());
        if(city!=null){

            textViewCity.setText(city);
        }else {
            textViewCity.setText(context.getString(R.string.pref_header_general));
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

    class HolderNews extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView textViewNewsTitle;
        final TextView textViewNewsSubtitle;
        final TextView textViewNewsTime;
        final TextView textViewNewsSource;
        final ImageView imageViewNewsMain;
        final LinearLayout linearLayoutNewsChips;

        HolderNews(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            textViewNewsTitle = (TextView) itemView.findViewById(R.id.textViewNewsTitle);
            textViewNewsSubtitle = (TextView) itemView.findViewById(R.id.textViewNewsSubtitle);
            textViewNewsTime = (TextView) itemView.findViewById(R.id.textViewNewsTime);
            textViewNewsSource = (TextView) itemView.findViewById(R.id.textViewNewsSource);
            imageViewNewsMain = (ImageView) itemView.findViewById(R.id.imageViewNewsMain);
            linearLayoutNewsChips = (LinearLayout) itemView.findViewById(R.id.linearLayoutNewsChips);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NewsDetailsActivity.class);
            intent.putExtra(NewsDetailsActivity.ARGS_NEWS_ID,newses.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }

    }
}
