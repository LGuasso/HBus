package br.com.expressobits.hbus.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.software.shell.fab.ActionButton;

import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemFavoriteItineraryAdapter;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogFragment;
import br.com.expressobits.hbus.ui.dialog.ChooseWayDialogListener;

/**
 * A simple {@link Fragment} subclass.
 * @author Rafael Correa
 * @since 06/07/2015
 */
public class FavoritesItineraryFragment extends Fragment implements RecyclerViewOnClickListenerHack,ChooseWayDialogListener{

    public String selectedItem;
    RecyclerView recyclerViewLines;
    private ActionButton actionButton;
    private List<Itinerary> itineraries;
    OnSettingsListener mCallback;
    LinearLayout linearLayoutEmptyList;
    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Este código serve para nos certificarmos que a Activity que contém o Fragment
        // implementa a interface do callback. Se não, lança uma exceção
        try {
            mCallback = (OnSettingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " precisa implementar OnSettingsListener");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerViewLines.getAdapter().getItemCount()<1){
            recyclerViewLines.setVisibility(View.GONE);
            linearLayoutEmptyList.setVisibility(View.VISIBLE);
        }else{
            recyclerViewLines.setVisibility(View.VISIBLE);
            linearLayoutEmptyList.setVisibility(View.GONE);
        }
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.app_name), "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_itinerary, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initListViews(view);
        initFAB(view);
        initEmptyList(view);
        initAdView(view);
    }

    private void initEmptyList(View view) {
        linearLayoutEmptyList = (LinearLayout)view.findViewById(R.id.list_empty);

    }


    private void initListViews(View view){
        recyclerViewLines = (RecyclerView) view.findViewById(R.id.list_lines);
        recyclerViewLines.setHasFixedSize(true);
        recyclerViewLines.setSelected(true);


        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewLines.setLayoutManager(llmUseful);



        BusDAO dao  = new BusDAO(getActivity());
        itineraries = dao.getItineraries(true);
        ItemFavoriteItineraryAdapter adapter = new ItemFavoriteItineraryAdapter(this.getActivity(), itineraries);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewLines.setAdapter(adapter);
        dao.close();

    }

    public void initAdView(View view){
        AdView mAdView = (AdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void initFAB(View view){
        actionButton = (ActionButton) view.findViewById(R.id.fab_button);
        actionButton.playShowAnimation();
        actionButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
        actionButton.setButtonColorPressed(getActivity().getResources().getColor(R.color.colorPrimaryDark));

        actionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        actionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        actionButton.setImageResource(R.drawable.ic_add_white_24dp);
        actionButton.setOnClickListener((View.OnClickListener) getActivity());
    }


    @Override
    public void onClickListener(View view, int position) {

        switch (view.getId()){
            case R.id.buttonLookTime:
                selectedItem = itineraries.get(position).getName();
                List<String> ways = itineraries.get(position).getSentidos();
                if(ways.size()>1) {
                    ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
                    chooseWayDialogFragment.setParameters(this,selectedItem,ways);
                    chooseWayDialogFragment.show(this.getFragmentManager(),ChooseWayDialogFragment.TAG);
                }else{
                    mCallback.onSettingsDone(selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_circular)).get(0));
                }
                break;
            case R.id.buttonRemove:
                selectedItem = itineraries.get(position).getName();

                BusDAO dao = new BusDAO(getActivity());
                Itinerary itinerary = dao.getItinerary(selectedItem);
                itinerary.setFavorite(false);
                dao.update(itinerary);
                this.initListViews(this.view);
                //TODO dialog confirmation
                //Toast.makeText(this.getActivity(),String.format(getResources().getString(R.string.delete_itinerary_with_sucess),itinerary.getName()),Toast.LENGTH_LONG).show();
                dao.close();
                break;
        }

    }

    @Override
    public boolean onLongClickListener(View view, int position) {

        return false;
    }


    @Override
    public void onItemClick(String itinerary,String way) {
        mCallback.onSettingsDone(itinerary,way);
    }
}


