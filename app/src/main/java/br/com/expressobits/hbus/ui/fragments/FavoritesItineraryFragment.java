package br.com.expressobits.hbus.ui.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.software.shell.fab.ActionButton;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemFavoriteItineraryAdapter;
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

    public static final String TAG = "FavoritesItineraryFragment";
    public String selectedItem;
    RecyclerView recyclerViewLines;
    private List<Itinerary> itineraries;
    OnSettingsListener mCallback;
    LinearLayout linearLayoutEmptyList;
    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Este c�digo serve para nos certificarmos que a Activity que cont�m o Fragment
        // implementa a interface do callback. Se n�o, lan�a uma exce��o
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
        updateEmptyListView();
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.app_name), "");
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



        FavoriteDAO dao  = new FavoriteDAO(getActivity());
        itineraries = dao.getItineraries();
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
        //TODO procurar saber quando descenmos a lista
        ActionButton actionButton = (ActionButton) view.findViewById(R.id.fab_button);
        actionButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
        actionButton.setButtonColorPressed(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        actionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        actionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_add_white_24dp);
        actionButton.setOnClickListener((View.OnClickListener) getActivity());
    }


    @Override
    public void onClickListener(View view, int position) {

        switch (view.getId()){
            case R.id.buttonLookTime:
                selectedItem = itineraries.get(position).getName();
                List<String> ways = itineraries.get(position).getWays();
                if(ways.size()>1) {
                    ChooseWayDialogFragment chooseWayDialogFragment = new ChooseWayDialogFragment();
                    chooseWayDialogFragment.setParameters(this,selectedItem,ways);
                    chooseWayDialogFragment.show(this.getFragmentManager(),ChooseWayDialogFragment.TAG);
                }else{
                    mCallback.onSettingsDone(selectedItem, ways.get(0));
                }
                break;
            case R.id.buttonRemove:
                selectedItem = itineraries.get(position).getName();


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_alert_title_confirm_remove);
                builder.setNegativeButton(android.R.string.no, null);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavoriteDAO dao = new FavoriteDAO(getActivity());
                        Itinerary itinerary = dao.getItinerary(selectedItem);
                        dao.removeFavorite(itinerary);
                        FavoritesItineraryFragment.this.initListViews(FavoritesItineraryFragment.this.view);
                        mCallback.onRemoveFavorite();
                        updateEmptyListView();
                        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(MainActivity.DEBUG, false)) {
                            Toast.makeText(getContext(), String.format(getResources().getString(R.string.delete_itinerary_with_sucess), itinerary.getName()), Toast.LENGTH_LONG).show();
                        }
                        dao.close();
                    }
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
    public void onItemClick(String itinerary,String way) {
        mCallback.onSettingsDone(itinerary,way);
    }
}


