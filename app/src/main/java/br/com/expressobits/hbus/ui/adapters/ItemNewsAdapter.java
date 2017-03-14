package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.viewholder.NewsViewHolder;
import br.com.expressobits.hbus.ui.news.NewsDetailsActivity;

/**
 * Class that implements each item in the Recycler news view
 * @author Rafael Correa
 * @since 21/08/16
 */
public class ItemNewsAdapter extends RecyclerView.Adapter<NewsViewHolder> implements RecyclerViewOnClickListenerHack{

    private final Context context;
    private final List<News> newses;
    private final LayoutInflater layoutInflater;

    public ItemNewsAdapter(Context context,List<News> list){
        this.context = context;
        this.newses = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_news,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int position) {
        News news = newses.get(position);
        NewsViewHolder.bindNews(layoutInflater,context,newsViewHolder, news);
        newsViewHolder.setRecyclerViewOnClickListenerHack(this);
    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    @Override
    public void onClickListener(View view, int position) {
        News news = newses.get(position);
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(NewsDetailsActivity.ARGS_NEWS_ID,news.getId());
        context.startActivity(intent);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

}
