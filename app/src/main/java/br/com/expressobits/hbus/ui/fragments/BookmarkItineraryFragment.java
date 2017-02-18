package br.com.expressobits.hbus.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemBookmarkItineraryAdapter;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;

/**
 * A simple {@link Fragment} subclass.
 * @author Rafael Correa
 * @since 06/07/2015
 */
public class BookmarkItineraryFragment extends Fragment implements RecyclerViewOnClickListenerHack,
        ChooseWayDialogListener,View.OnClickListener{

    public static final String TAG = "FavoritesItineraryFragment";
    public String selectedItem;
    private RecyclerView recyclerViewLines;
    private List<Itinerary> itineraries;
    OnSettingsListener mCallback;
    LinearLayout linearLayoutEmptyList;
    LinearLayoutManager llmUseful;
    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSettingsListener) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " precisa implementar OnSettingsListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListViews();
        updateEmptyListView();
    }

    private void updateEmptyListView() {
        if(recyclerViewLines.getAdapter().getItemCount()<1){
            recyclerViewLines.setVisibility(View.GONE);
            linearLayoutEmptyList.setVisibility(View.VISIBLE);
        }else{
            recyclerViewLines.setVisibility(View.VISIBLE);
            linearLayoutEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_itinerary, container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initListViews(view);
        initEmptyList(view);
    }

    private void initEmptyList(View view) {
        linearLayoutEmptyList = (LinearLayout)view.findViewById(R.id.list_empty);
        linearLayoutEmptyList.setOnClickListener(this);

    }


    private void initListViews(View view){
        recyclerViewLines = (RecyclerView) view.findViewById(R.id.list_lines);
        recyclerViewLines.setHasFixedSize(true);
        recyclerViewLines.setSelected(true);
        llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewLines.setLayoutManager(llmUseful);

    }

    private void updateListViews() {
        BookmarkItineraryDAO dao  = new BookmarkItineraryDAO(getActivity());
        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
        itineraries = dao.getItineraries(cityId);
        ItemBookmarkItineraryAdapter adapter = new ItemBookmarkItineraryAdapter(this.getActivity(), itineraries);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewLines.setAdapter(adapter);
        dao.close();
    }





    @Override
    public void onClickListener(final View view, int position) {

        switch (view.getId()){
            case R.id.buttonLookTime:
                Itinerary itinerary = itineraries.get(position);
                ((MainActivity)getActivity()).onCreateDialogChooseWay(itinerary);
                break;
            case R.id.buttonRemove:
                selectedItem = itineraries.get(position).getId();


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_alert_title_confirm_remove);
                builder.setNegativeButton(android.R.string.no, null);
                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    BookmarkItineraryDAO dao = new BookmarkItineraryDAO(getActivity());
                    Itinerary itinerary1 = dao.getItinerary(selectedItem);
                    dao.removeFavorite(itinerary1);
                    BookmarkItineraryFragment.this.initListViews(BookmarkItineraryFragment.this.view);
                    updateListViews();
                    updateEmptyListView();
                    String result = getActivity().getResources().getString(R.string.delete_itinerary_with_sucess, itinerary1.getName());
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(MainActivity.DEBUG, false)) {
                        Toast.makeText(getContext(),result, Toast.LENGTH_LONG).show();
                    }
                    Snackbar.make(BookmarkItineraryFragment.this.view,result, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    dao.close();
                });
                builder.show();

                break;


        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {

        return false;
    }


    @Override
    public void onItemClick(String company,String itineraryId,String way) {
        mCallback.onSettingsDone(company,itineraryId,way);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_empty:
                mCallback.addFragment(ItinerariesFragment.TAG);
                break;
        }
    }
}


