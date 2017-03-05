package br.com.expressobits.hbus.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.ManagerInit;
import br.com.expressobits.hbus.dao.SQLConstants;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.StringUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadParams();
        setContentView(R.layout.activity_download_schedule);
        textViewStatusLoading = (TextView) findViewById(R.id.textViewStatusLoading);
        textViewStatusMessage = (TextView) findViewById(R.id.textViewStatusMessage);
        if(updateMode){
            textViewStatusMessage.setText(getString(R.string.download_schedule_update_message));
        }
        downloadDatabase(SQLConstants.getCountryFromBusId(cityId),SQLConstants.getCityFromBusId(cityId));
    }

    private void downloadDatabase(String country,String city){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child(SQLConstants.DATABASE_PATTERN_NAME).
                child(country).child(city).child(
                StringUtils.getNameDatabase(country,city,DATABASE_VERSION));

        File localFile = getDatabasePath(
                StringUtils.getNameDatabase(country,city,DATABASE_VERSION));
        FileDownloadTask fileDownloadTask = fileRef.getFile(localFile);
        fileDownloadTask.addOnSuccessListener(this);
        fileDownloadTask.addOnFailureListener(this);
        fileDownloadTask.addOnProgressListener(this);
        fileDownloadTask.addOnPausedListener(this);

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
        textViewStatusLoading.setText("Download with failed!");
    }

    @Override
    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
        finishTask();
    }

    @Override
    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
        int percent = (int)(taskSnapshot.getBytesTransferred()*100/taskSnapshot.getTotalByteCount());
        textViewStatusLoading.setText(getString(R.string.common_percent,String.valueOf(percent)));
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


}
