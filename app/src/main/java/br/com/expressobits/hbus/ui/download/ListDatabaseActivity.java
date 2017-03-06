package br.com.expressobits.hbus.ui.download;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.ItemFileDabaseAdapter;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.StringUtils;

import static br.com.expressobits.hbus.ui.DownloadScheduleActivity.DATABASE_VERSION;

public class ListDatabaseActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack{


    private static final String TAG = "ListDatabaseActivity";
    private List<File> listFile = new ArrayList<>();
    private RecyclerView recyclerViewDatabases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule_database);
        initViews();
        getDatabasesList(SelectCityActivity.DEFAULT_COUNTRY);
    }

    private void initViews(){
        initToolbar();
        recyclerViewDatabases = (RecyclerView) findViewById(R.id.recyclerViewDatabases);
        recyclerViewDatabases.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDatabases.setLayoutManager(linearLayoutManager);
        ItemFileDabaseAdapter itemFileDabaseAdapter = new ItemFileDabaseAdapter(this,listFile);
        itemFileDabaseAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewDatabases.setAdapter(itemFileDabaseAdapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addFile(File file){
        listFile.add(file);
        recyclerViewDatabases.getAdapter().notifyDataSetChanged();
    }

    private void getDatabasesList(String country){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference citiesTableRef = database.getReference(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = citiesTableRef.child(country);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotCity : dataSnapshot.getChildren()) {
                    City city = dataSnapshotCity.getValue(City.class);
                    city.setId(FirebaseUtils.getIdCity(country, city.getName()));
                    File localFile = getDatabasePath(
                            StringUtils.getNameDatabase(city.getCountry(),city.getName(),DATABASE_VERSION));
                        Log.i(TAG,localFile.getName());
                    if(localFile.exists()){
                        Log.i(TAG,"\tfile exists! file size "+localFile.length());
                        addFile(localFile);
                    }else {
                        Log.i(TAG,"\tfile no exists! file size ");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**imageViewNetworkError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);*/
            }
        };
        countryRef.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean removeDatabaseFile(File file){
        return file.delete();
    }

    @Override
    public void onClickListener(View view, int position) {
        if(view.getId()==R.id.imageButtonDeleteDatabase){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String cityId = sharedPreferences.getString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);
            AlertDialog.Builder builder = new AlertDialog.Builder(ListDatabaseActivity.this);
            builder.setIcon(R.drawable.ic_delete_grey_600_24dp);
            builder.setMessage(R.string.dialog_alert_message_confirm_remove_database);
            builder.setTitle(R.string.dialog_alert_title_confirm_remove_database);
            builder.setNegativeButton(android.R.string.no, null);
            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                File file = listFile.get(position);
                if(file.getName().equals(
                        StringUtils.getNameDatabase(
                                SQLConstants.getCountry(cityId),
                                SQLConstants.getCity(cityId),
                                DATABASE_VERSION))){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ListDatabaseActivity.this);
                    builder1.setTitle(R.string.list_database_dialog_title_is_not_possible_to_remove);
                    builder1.setMessage(R.string.dialog_alert_message_no_delete_database_in_use);
                    builder1.show();
                }else{
                    if(removeDatabaseFile(listFile.get(position))){
                        listFile.remove(file);
                        recyclerViewDatabases.getAdapter().notifyDataSetChanged();
                        Toast.makeText(ListDatabaseActivity.this,R.string.list_database_database_deleted_successfully,Toast.LENGTH_LONG).show();
                    }
                }

            });
            builder.show();

        }
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
