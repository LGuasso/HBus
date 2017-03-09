package br.com.expressobits.hbus.ui.help;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Feedback;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_VERSION_INFO="VERSION_INFO";
    private EditText editTextFeedback;
    private EditText editTextEmail;
    private CheckBox checkBoxFeedback;
    private Spinner spinnerFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String versionInfo = getString(R.string.version) + " " + getIntent().getStringExtra(KEY_VERSION_INFO);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(versionInfo);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_action_send) {
            sendFeedback();
            //EmailUtils.sendEmail(this,"Feedback HBUS "+versionInfo,getString(R.string.write_your_feedback_here),"expressobits@gmail.com");
            return true;
        }
        return false;

    }

    public void initComponents(){
        LinearLayout linearLayoutSendFeedback = (LinearLayout) findViewById(R.id.linear_layout_send_feedback);
        editTextFeedback = (EditText) findViewById(R.id.edit_text_feedback);
        checkBoxFeedback = (CheckBox) findViewById(R.id.checkbox_feedback_includes_system_information);
        spinnerFeedback = (Spinner) findViewById(R.id.spinner_feedback);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email_feedback);
        linearLayoutSendFeedback.setOnClickListener(this);
    }

    public void sendFeedback(){
        Feedback feedback = new Feedback();
        feedback.setMessage(editTextFeedback.getText().toString());
        feedback.setType(spinnerFeedback.getSelectedItemPosition());
        feedback.setEmail(editTextEmail.getText().toString());
        if(checkBoxFeedback.isChecked()){
            ArrayList<String> informations = new ArrayList<>();
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Error PackageManager NameNotFoundException", Toast.LENGTH_SHORT).show();
            }
            if(pInfo!=null){
                informations.add("Version app name="+pInfo.versionName);
                informations.add("Version app code="+pInfo.versionCode);
            }
            informations.add("Version android release="+ Build.VERSION.RELEASE);
            informations.add("Device="+ Build.DEVICE);
            informations.add("Dispĺay="+ Build.DISPLAY);
            informations.add("Type="+ Build.TYPE);

            feedback.setSystemInformation(informations);
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DatabaseReference feedbackReference = reference.child("feedback");
        DatabaseReference databaseReference = feedbackReference.push();
        databaseReference.setValue(feedback).addOnCompleteListener(
                task -> Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.feedback_was_sent_with_sucess),
                        Toast.LENGTH_LONG).show()
        ).addOnFailureListener(
                e -> Toast.makeText(getApplicationContext(),
                        getString(R.string.feedback_failed_to_send_feedback),
                        Toast.LENGTH_LONG).show());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_layout_send_feedback:
                sendFeedback();
                break;
        }
    }
}
