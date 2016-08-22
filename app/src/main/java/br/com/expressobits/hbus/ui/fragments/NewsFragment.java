package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.adapters.ItemNewsAdapter;
import br.com.expressobits.hbus.util.NewsUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    public static final String TAG = "NewsFragment";

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewNews;
    private List<News> newses;
    private LinearLayout linearLayoutEmptyView;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        initListView(view);
        updateListView();
        initEmptyView(view);
    }

    private void initEmptyView(View view) {
    }

    /**
     * Atualiza as informações da lista
     */
    private void updateListView() {
        //TODO gerar do Firebase
        newses = NewsUtils.getNews();
        ItemNewsAdapter adapter = new ItemNewsAdapter(getActivity(),newses);
        recyclerViewNews.setAdapter(adapter);
    }

    private void initListView(View view) {
        recyclerViewNews = (RecyclerView) view.findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setHasFixedSize(true);
        recyclerViewNews.setSelected(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNews.setLayoutManager(linearLayoutManager);
    }

}
