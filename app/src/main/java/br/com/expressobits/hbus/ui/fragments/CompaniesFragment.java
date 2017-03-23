package br.com.expressobits.hbus.ui.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.provider.CompanyContract;
import br.com.expressobits.hbus.provider.ScheduleContentProvider;
import br.com.expressobits.hbus.ui.CompanyDetailsActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.TimesActivity;
import br.com.expressobits.hbus.ui.adapters.CursorRecyclerViewAdapter;
import br.com.expressobits.hbus.ui.adapters.ItemCompanyCursorRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompaniesFragment extends Fragment implements RecyclerViewOnClickListenerHack,LoaderManager.LoaderCallbacks<Cursor>{

    /**public final int offset = 1;
    private int page = 0;

    private boolean loadingMore = false;*/

    public static final String TAG = "CompaniesFragment";
    private FastScrollRecyclerView recyclerViewCompanies;
    private ProgressBar progressBar;


    public CompaniesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_companies, container, false);
        initViews(view);
        return view;

    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){

        ItemCompanyCursorRecyclerViewAdapter mAdapter = new
                ItemCompanyCursorRecyclerViewAdapter( getActivity(), null );

        mAdapter.setRecyclerViewOnClickListenerHack(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerViewCompanies = (FastScrollRecyclerView) view.findViewById(R.id.recyclerViewCompanies);
        recyclerViewCompanies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCompanies.setAdapter(mAdapter);
        //recyclerViewCompanies.setHasFixedSize(true);


        /**recyclerViewCompanies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();


                int maxPositions = layoutManager.getItemCount();

                if (lastVisibleItemPosition == maxPositions - 1) {
                    if (loadingMore)
                        return;

                    loadingMore = true;
                    page++;
                    getActivity().getSupportLoaderManager().restartLoader(0, null, CompaniesFragment.this);
                }
            }
        });*/

        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

    }


    @Override
    public void onClickListener(View view, int position) {
        Intent intent;
        String id = ((CursorRecyclerViewAdapter)recyclerViewCompanies.getAdapter()).getStringItemId(position);
        Log.d(TAG,id);
        intent = new Intent(getContext(), CompanyDetailsActivity.class);
        intent.putExtra(TimesActivity.ARGS_COUNTRY, SQLConstants.getCountryFromBusId(id));
        intent.putExtra(TimesActivity.ARGS_CITY, SQLConstants.getCityFromBusId(id));
        intent.putExtra(TimesActivity.ARGS_COMPANY,SQLConstants.getCompanyFromBusId(id));
        startActivity(intent);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        recyclerViewCompanies.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        switch (id) {
            case 0:
                CursorLoader cursorLoader = new CursorLoader(
                        getActivity(),
                        ScheduleContentProvider.urlForAllItems(),
                        null,
                        null,
                        null,
                        null);
                Log.d(TAG,"return cursorloader "+cursorLoader.getUri());
                return cursorLoader;
            default:
                throw new IllegalArgumentException("no id handled!");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recyclerViewCompanies.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        switch (loader.getId()){
            case 0:
                //Toast.makeText(getActivity(),"loading more "+page,Toast.LENGTH_LONG).show();
                Cursor cursor =
                        ((ItemCompanyCursorRecyclerViewAdapter)recyclerViewCompanies.getAdapter()).getCursor();

                //fill all exisitng in adapter
                MatrixCursor mx = new MatrixCursor(CompanyContract.COLS);
                fillMx(cursor, mx);

                fillMx(data, mx);

                ((ItemCompanyCursorRecyclerViewAdapter) recyclerViewCompanies.getAdapter()).swapCursor(mx);
                break;
            default:
                throw new IllegalArgumentException("no loader id handled!");
        }

    }

    private void fillMx(Cursor data, MatrixCursor mx) {
        if (data == null)
            return;

        data.moveToPosition(-1);
        while (data.moveToNext()) {
            mx.addRow(new Object[]{
                    data.getString(data.getColumnIndex(CompanyContract.Company._ID)),
                    data.getString(data.getColumnIndex(CompanyContract.Company.COLUMN_NAME_NAME)),
                    data.getString(data.getColumnIndex(CompanyContract.Company.COLUMN_NAME_EMAIL)),
                    data.getString(data.getColumnIndex(CompanyContract.Company.COLUMN_NAME_WEBSITE)),
                    data.getString(data.getColumnIndex(CompanyContract.Company.COLUMN_NAME_PHONENUMBER)),
                    data.getString(data.getColumnIndex(CompanyContract.Company.COLUMN_NAME_ADDRESS))
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
