package br.com.expressobits.hbus.ui.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.dao.NewsReadDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.provider.HotTipsProvider;
import br.com.expressobits.hbus.ui.FragmentManagerListener;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemHomeAdapter;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.help.HelpActivity;
import br.com.expressobits.hbus.ui.model.Header;
import br.com.expressobits.hbus.ui.model.HotTip;
import br.com.expressobits.hbus.ui.news.NewsDetailsActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * A simple {@link Fragment} subclass.
 * @author Rafael Correa
 * @since 06/07/2015
 */
public class HomeFragment extends Fragment implements RecyclerViewOnClickListenerHack,
        ChooseWayDialogListener,View.OnClickListener{

    public static final String TAG = "HomeFragment";
    public String selectedItem;
    private RecyclerView recyclerView;
    private List<Object> items = new ArrayList<>();
    FragmentManagerListener mCallback;
    LinearLayoutManager llmUseful;
    private String cityId;
    View view;
    private String country;
    private String cityName;

    private Header headerTips;
    private Header headerLastUnreadNews;
    private Header headerBookmark;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentManagerListener) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " precisa implementar OnSettingsListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        country = FirebaseUtils.getCountry(cityId);
        cityName = FirebaseUtils.getCityName(cityId);
        pullDataItens();
        if(((MainActivity)mCallback).getSupportActionBar()!=null){
            ((MainActivity)mCallback).setActionBarTitle(getString(R.string.app_name));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite_itinerary, container,false);
        headerTips = new Header(getString(R.string.common_tips));
        headerLastUnreadNews = new Header(getString(R.string.unread_news));
        headerBookmark = new Header(getString(R.string.bookmarks));
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initListViews(view);
        setHasOptionsMenu(true);
    }

    private void initListViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list_lines);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);
        ItemHomeAdapter adapter = new ItemHomeAdapter(this.getActivity(), items);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);

    }

    private boolean getBookmarkedItineraries(){
        BookmarkItineraryDAO dao  = new BookmarkItineraryDAO(getActivity());
        List<Itinerary> itineraries = dao.getItineraries(cityId);
        dao.close();
        if(itineraries.size()>0){
            items.add(0,headerBookmark);
            items.addAll(itineraries);
            return true;
        }else {
            return false;
        }
    }

    private void getLastNews(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newsTableRef = database.getReference(FirebaseUtils.NEWS_TABLE);
        DatabaseReference cityTableRef = newsTableRef.child(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = cityTableRef.child(country);
        cityName = FirebaseUtils.getCityName(cityId);
        DatabaseReference cityRef = countryRef.child(cityName);
        Query recentNewsQuery = cityRef.limitToLast(1);
        recentNewsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                News news = dataSnapshot.getValue(News.class);
                country = FirebaseUtils.getCountry(cityId);
                news.setId(FirebaseUtils.getIdNewsCity(String.valueOf(news.getTime()), country, cityName));

                if(news.isActived()){
                    NewsReadDAO newsReadDAO = new NewsReadDAO(getContext());
                    if(!newsReadDAO.isExist(news)){
                        items.add(0,headerLastUnreadNews);
                        items.add(1,news);
                        updateListViews();
                    }
                    newsReadDAO.close();
                }
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

    private void pullDataItens(){
        items.clear();
        if(getBookmarkedItineraries()){
            getLastNews();
            if(HotTipsProvider.isViewRateUs(getActivity())){
                items.add(0,HotTipsProvider.getRateUsHotTip(getActivity()));
                items.add(0,headerTips);
            }else if(HotTipsProvider.isViewBetaProgram(getActivity())){
                items.add(0,HotTipsProvider.getBetaProgramHotTip(getActivity()));
                items.add(0,headerTips);
            }

        }else{
            items.add(headerTips);
            items.add(HotTipsProvider.getGetStartedHotTip(getActivity()));
        }

        updateListViews();

    }

    private void updateListViews() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_home_fragment, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo( searchManager.getSearchableInfo( getActivity().getComponentName() ) );
        searchView.setQueryHint( getResources().getString(R.string.itinerary_search_hint) );
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClickListener(final View view, int position) {

        if (items.get(position) instanceof Itinerary) {
            Itinerary itinerary = (Itinerary)items.get(position);
            switch (view.getId()){
                case R.id.buttonLookTime:
                    ((MainActivity)getActivity()).onCreateDialogChooseWay(itinerary);
                    break;
                case R.id.buttonRemove:
                    selectedItem = itinerary.getId();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.dialog_alert_title_confirm_remove);
                    builder.setNegativeButton(android.R.string.no, null);
                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        BookmarkItineraryDAO dao = new BookmarkItineraryDAO(getActivity());
                        Itinerary itinerary1 = dao.getItinerary(selectedItem);
                        dao.removeFavorite(itinerary1);
                        items.remove(itinerary);
                        updateListViews();
                        String result = getActivity().getResources().getString(R.string.delete_itinerary_with_sucess, itinerary1.getName());
                        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(MainActivity.DEBUG, false)) {
                            Toast.makeText(getContext(),result, Toast.LENGTH_LONG).show();
                        }
                        Snackbar.make(HomeFragment.this.view,result, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        dao.close();
                    });
                    builder.show();
                    break;
            }

        }else if(items.get(position) instanceof News){
            News news = (News)items.get(position);
            Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
            intent.putExtra(NewsDetailsActivity.ARGS_NEWS_ID,news.getId());
            getContext().startActivity(intent);
        }else if(items.get(position) instanceof HotTip){
            HotTip hottip = (HotTip)(items.get(position));
            int type = hottip.getType();
            if(type == HotTipsProvider.TYPE_GETSTARTED){
                mCallback.addFragment(ItinerariesFragment.TAG);
            }else if(type == HotTipsProvider.TYPE_BETA_PROGRAM){
                if(view.getId() == R.id.buttonHotTip1){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.beta_community_link))));
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(HotTipsProvider.DISMISS_VIEW_BETA_PROGRAM,false);
                    editor.apply();
                    items.remove(position);
                    items.remove(0);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }else if(view.getId() == R.id.buttonHotTip2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.dialog_alert_title_confirm_no_beta);
                    builder.setNegativeButton(android.R.string.no, null);
                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(HotTipsProvider.DISMISS_VIEW_BETA_PROGRAM, false);
                        editor.apply();
                        items.remove(position);
                        items.remove(headerTips);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    });
                    builder.show();
                }

            }else if(type == HotTipsProvider.TYPE_RATE_US){
                if(view.getId() == R.id.buttonHotTip1){
                    HelpActivity.openAppInPlayStore(getActivity());
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(HotTipsProvider.DISMISS_VIEW_RATE_US,false);
                    editor.apply();
                    items.remove(position);
                    items.remove(0);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }else if(view.getId() == R.id.buttonHotTip2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.dialog_alert_title_confirm_rate_us);
                    builder.setNegativeButton(android.R.string.no,null);
                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(HotTipsProvider.DISMISS_VIEW_RATE_US,false);
                        editor.apply();
                        items.remove(position);
                        items.remove(headerTips);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    });
                    builder.show();
                }
            }

        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {

        return false;
    }


    @Override
    public void onItemClick(String country,String city,String company,String itineraryId,String way) {
        AppManager.onSettingsDone(getActivity(),country,city,company,itineraryId,way);
    }

    @Override
    public void onClick(View v) {
    }
}


