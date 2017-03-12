package br.com.expressobits.hbus.ui.news;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.adapters.ItemNewsAdapter;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment{

    public static final String TAG = "NewsFragment";

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewNews;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<News> newses = new ArrayList<>();
    private LinearLayout linearLayoutEmptyView;
    private ViewGroup progressBarLayout;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initViews(view);

        setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View view) {
        initListView(view);
        initEmptyView(view);

        progressBarLayout = (ViewGroup) view.findViewById(R.id.progressBarLayout);
        updateListView();
    }

    private void initListView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(),R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            update(true);
        });
        recyclerViewNews = (RecyclerView) view.findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setHasFixedSize(true);
        recyclerViewNews.setSelected(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewNews.setLayoutManager(linearLayoutManager);
        ItemNewsAdapter itemNewsAdapter = new ItemNewsAdapter(getActivity(),newses);
        recyclerViewNews.setAdapter(itemNewsAdapter);
    }

    private void initEmptyView(View view) {
        linearLayoutEmptyView = (LinearLayout) view.findViewById(R.id.list_empty);
    }

    @Override
    public void onResume() {
        super.onResume();
        update(false);
    }

    private void update(boolean isSwipe){
        newses = new ArrayList<>();
        updateListView();
        getGeneralNews();
        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);
        String country = FirebaseUtils.getCountry(cityId);
        String city = FirebaseUtils.getCityName(cityId);
        getCityNews(country,city);
        String company = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(cityId,"");
        getCompanyNews(company,country,city);
        if(isSwipe){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Atualiza as informações da lista
     */
    private void updateListView() {
        if(newses.size()>0){
            linearLayoutEmptyView.setVisibility(View.INVISIBLE);
            progressBarLayout.setVisibility(View.INVISIBLE);
            recyclerViewNews.setVisibility(View.VISIBLE);

        }else {
            linearLayoutEmptyView.setVisibility(View.INVISIBLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            recyclerViewNews.setVisibility(View.INVISIBLE);
            //linearLayoutEmptyView.setVisibility(View.VISIBLE);
        }
        Collections.sort(newses);
        ItemNewsAdapter adapter = new ItemNewsAdapter(getActivity(),newses);
        recyclerViewNews.setAdapter(adapter);
    }


    private void getGeneralNews(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newsTableRef = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference generalRef = newsTableRef.child(FirebaseUtils.GENERAL);
        generalRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                News news = dataSnapshot.getValue(News.class);
                news.setId(FirebaseUtils.getIdNewsGeneral(String.valueOf(news.getTime())));
                if(news.isActived()){
                    newses.add(news);
                }
                updateListView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCityNews(final String country, final String cityName){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newsTableRef = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference cityTableRef = newsTableRef.child(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = cityTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(cityName);
        //TODO criar configuração com número de notícias exibido
        Query recentNewsQuery = cityRef.limitToLast(6);
        recentNewsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                News news = dataSnapshot.getValue(News.class);
                news.setId(FirebaseUtils.getIdNewsCity(String.valueOf(news.getTime()),country,cityName));
                if(news.isActived()){
                    newses.add(news);
                }
                updateListView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCompanyNews(final String company, final String country, final String cityName){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newsTableRef = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference cityTableRef = newsTableRef.child(FirebaseUtils.COMPANY_TABLE);
        DatabaseReference countryRef = cityTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(cityName);
        DatabaseReference companyRef = cityRef.child(company);
        companyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                News news = dataSnapshot.getValue(News.class);
                news.setId(FirebaseUtils.getIdNewsCompany(String.valueOf(news.getTime()),country,cityName,company));
                if(news.isActived()){
                    newses.add(news);
                }
                updateListView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_news_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
