package br.com.expressobits.hbus.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.util.EmailUtils;

public class FeedbackActivity extends AppCompatActivity {

    public static final String KEY_VERSION_INFO="VERSION_INFO";
    private String versionInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        versionInfo = getString(R.string.version)+" "+getIntent().getStringExtra(KEY_VERSION_INFO);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(versionInfo);
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
            EmailUtils.sendEmail(this,"Feedback HBUS "+versionInfo,getString(R.string.write_your_feedback_here),"expressobits@gmail.com");
            return true;
        }
        return false;

    }
}
