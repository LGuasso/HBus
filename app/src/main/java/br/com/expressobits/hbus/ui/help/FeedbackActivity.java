package br.com.expressobits.hbus.ui.help;

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

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Feedback;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_VERSION_INFO="VERSION_INFO";
    private String versionInfo;
    private EditText editTextFeedback;
    private CheckBox checkBoxFeedback;
    private Spinner spinnerFeedback;
    private LinearLayout linearLayoutSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        versionInfo = getString(R.string.version)+" "+getIntent().getStringExtra(KEY_VERSION_INFO);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(versionInfo);
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
        linearLayoutSendFeedback = (LinearLayout) findViewById(R.id.linear_layout_send_feedback);
        editTextFeedback = (EditText) findViewById(R.id.edit_text_feedback);
        checkBoxFeedback = (CheckBox) findViewById(R.id.checkbox_feedback_includes_system_information);
        spinnerFeedback = (Spinner) findViewById(R.id.spinner_feedback);
        linearLayoutSendFeedback.setOnClickListener(this);
    }

    public void sendFeedback(){
        Feedback feedback = new Feedback();
        //TODO gerar um Long
        feedback.setId(1l);
        feedback.setText(editTextFeedback.getText().toString());
        feedback.setType(getResources().getStringArray(R.array.list_feedback)[spinnerFeedback.getSelectedItemPosition()]);
        if(checkBoxFeedback.isChecked()){
            feedback.setSystemInformation("Hbus example");
        }else{
            feedback.setSystemInformation("Not system info...");
        }
        //TODO insert feedback into datastore database
        //FirebaseDAO dao = new FirebaseDAO("https://hbus.firebaseio.com/");
        //dao.insert(feedback);
        Toast.makeText(this,getString(R.string.send_feedback_with_sucess),Toast.LENGTH_LONG).show();
        finish();
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
