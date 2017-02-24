package br.com.expressobits.hbus.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.provider.ItinerarySearchableProvider;
import br.com.expressobits.hbus.ui.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class ItinerarySearchableActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private List<Itinerary> itinerariesSearchList;
    private ItemItineraryAdapter itemItineraryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_searchable);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState != null){
             /**mList = savedInstanceState.getParcelableArrayList("mList");
             itinerariesSearchList = savedInstanceState.getParcelableArrayList("itinerariesSearchList");*/
        }else{
             itinerariesSearchList = new ArrayList<>();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.itineraryRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        itemItineraryAdapter = new ItemItineraryAdapter(this, itinerariesSearchList);
        itemItineraryAdapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(itemItineraryAdapter);

        handleSearch(getIntent());
    }

    private void loadItinerariesFromFirebase(String itineraryQuery,final String country, final String city, final String company) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference itinerariesTableRef = database.getReference(FirebaseUtils.ITINERARY_TABLE);
        DatabaseReference countryRef = itinerariesTableRef.child(country);
        DatabaseReference cityRef = countryRef.child(city);
        DatabaseReference companyRef = cityRef.child(company);
        Query query = companyRef.orderByKey().startAt(itineraryQuery).endAt(itineraryQuery+"\uf8ff");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Itinerary itinerary = dataSnapshot.getValue(Itinerary.class);
                itinerary.setId(FirebaseUtils.getIdItinerary(country, city, company, itinerary.getName()));
                addItinerary(itinerary);
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
        };
        query.addChildEventListener(childEventListener);

    }

    private void addItinerary(Itinerary itinerary) {
        itinerariesSearchList.add(itinerary);
        itemItineraryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }


    public void handleSearch(Intent intent) {
        itinerariesSearchList.clear();
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String q = intent.getStringExtra(SearchManager.QUERY);

            getSupportActionBar().setTitle(q);
            loadItinerariesFromFirebase(q,"BR/RS","Santa Maria","SIMSM");
            //filterItineraries(q);

            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    ItinerarySearchableProvider.AUTHORITY,
                    ItinerarySearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(q, null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /**outState.putParcelableArrayList("mList", (ArrayList<Itinerary>) mList);
         outState.putParcelableArrayList("itinerariesSearchList", (ArrayList<Itinerary>) itinerariesSearchList);*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchable_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) item.getActionView();
        } else {
            searchView = (SearchView) MenuItemCompat.getActionView(item);
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.itinerary_search_hint));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        /**else if( id == R.id.action_delete ){
         SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
         SearchableProvider.AUTHORITY,
         SearchableProvider.MODE);

         searchRecentSuggestions.clearHistory();

         Toast.makeText(this, "Cookies removidos", Toast.LENGTH_SHORT).show();
         }*/

        return true;
    }


    @Override
    public void onClickListener(View view, int position) {

    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
