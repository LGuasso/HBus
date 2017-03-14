package br.com.expressobits.hbus.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.ManagerInit;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.provider.FastTipsProvider;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.util.ZipUtils;
import br.com.expressobits.hbus.utils.StringUtils;

import static br.com.expressobits.hbus.R.string.city;

public class DownloadScheduleActivity extends Activity implements OnPausedListener<FileDownloadTask.TaskSnapshot>,OnProgressListener<FileDownloadTask.TaskSnapshot>,OnFailureListener,OnSuccessListener<FileDownloadTask.TaskSnapshot> {

    private TextView textViewStatusLoading;
    private TextView textViewStatusMessage;
    private boolean starterMode;
    private boolean updateMode;
    private String cityId;


    public static final int DATABASE_VERSION = 1;

    public static final String STARTER_MODE = "starterMode";
    public static final String UPDATE_MODE = "updateMode";
    public static final String DATABASE_LAST_UPDATE_PREFERENCE_KEY = "br.com.expressobits.hbus.database_last_update";
    private File zipFile;
    private File localFile;
    private DatabaseReference connectedRef;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadParams();
        setContentView(R.layout.activity_download_schedule);

        TextView textViewFastTip = (TextView) findViewById(R.id.textViewFastTip);
        textViewFastTip.setText(FastTipsProvider.getRandomFastTip(this));

        textViewStatusLoading = (TextView) findViewById(R.id.textViewStatusLoading);
        textViewStatusMessage = (TextView) findViewById(R.id.textViewStatusMessage);
        if(updateMode){
            textViewStatusMessage.setText(getString(R.string.download_schedule_update_message));
        }
        downloadDatabase(SQLConstants.getCountryFromBusId(cityId),SQLConstants.getCityFromBusId(cityId));
    }

    private void downloadDatabase(String country,String city){

        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference fileRef = storageRef.child(SQLConstants.DATABASE_PATTERN_NAME).
                            child(country).child(city).child(
                            StringUtils.getNameDatabaseZipFile(country,city,DATABASE_VERSION));

                    zipFile = getDatabasePath(
                            StringUtils.getNameDatabaseZipFile(country,city,DATABASE_VERSION));

                    localFile = getDatabasePath(
                            StringUtils.getNameDatabase(country,city,DATABASE_VERSION));
                    FileDownloadTask fileDownloadTask = fileRef.getFile(zipFile);
                    fileDownloadTask.addOnSuccessListener(DownloadScheduleActivity.this);
                    fileDownloadTask.addOnFailureListener(DownloadScheduleActivity.this);
                    fileDownloadTask.addOnProgressListener(DownloadScheduleActivity.this);
                    fileDownloadTask.addOnPausedListener(DownloadScheduleActivity.this);
                } else {
                    Toast.makeText(DownloadScheduleActivity.this,getString(R.string.no_have_connection_with_internet),Toast.LENGTH_LONG).show();
                    errorInDownload();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        };
        connectedRef.addValueEventListener(valueEventListener);



    }

    private void loadParams(){
        starterMode = getIntent().getBooleanExtra(STARTER_MODE,false);
        updateMode = getIntent().getBooleanExtra(UPDATE_MODE,false);
        cityId = PreferenceManager.getDefaultSharedPreferences(this).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY);
    }

    private void finishTask(){
        saveDatabaseLastUpdate();
        if(starterMode){
            ManagerInit.manager(this);
        }
        finish();
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        Toast.makeText(DownloadScheduleActivity.this,exception.getMessage(),Toast.LENGTH_LONG).show();
        textViewStatusLoading.setText(getString(R.string.download_schedule_failed_download));
        errorInDownload();
    }

    @Override
    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

        try {
            ZipUtils.unzip(zipFile,localFile);
            if(zipFile.delete()){
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finishTask();
    }

    @Override
    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
        int percent = (int)(taskSnapshot.getBytesTransferred()*100/taskSnapshot.getTotalByteCount());
        textViewStatusLoading.setText(getString(R.string.common_percent_download,percent));
    }

    @Override
    public void onPaused(FileDownloadTask.TaskSnapshot taskSnapshot) {
        int percent = (int)(taskSnapshot.getBytesTransferred()*100/taskSnapshot.getTotalByteCount());
        Toast.makeText(DownloadScheduleActivity.this,"Paused in "+percent+"%",Toast.LENGTH_LONG).show();
    }

    /**
     * Save long in preference for last update to schedule database in city key preference
     */
    private void saveDatabaseLastUpdate(){
        final SharedPreferences sharedPref = this.getSharedPreferences(
                DATABASE_LAST_UPDATE_PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(cityId,System.currentTimeMillis());
        editor.apply();
    }

    private void errorInDownload(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);
        editor.apply();
        if(starterMode){
            ManagerInit.manager(this);
            finish();
        }else{
            Intent intent = new Intent(this,SelectCityActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectedRef.removeEventListener(valueEventListener);
    }
}
