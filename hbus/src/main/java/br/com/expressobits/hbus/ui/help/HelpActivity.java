package br.com.expressobits.hbus.ui.help;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.dialog.VersionInfoDialogFragment;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_action_information_version) {
            VersionInfoDialogFragment versionInfoDialogFragment = new VersionInfoDialogFragment();
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(HelpActivity.this, "Error PackageManager NameNotFoundException", Toast.LENGTH_SHORT).show();
            }
            String version = pInfo.versionName;
            Bundle bundle = new Bundle();
            bundle.putString(VersionInfoDialogFragment.KEY_VERSION_INFO,version);
            versionInfoDialogFragment.setArguments(bundle);
            versionInfoDialogFragment.show(getSupportFragmentManager(), VersionInfoDialogFragment.TAG);
        }
        if(id == R.id.menu_action_send_feedback){
            startActivity(new Intent(this, FeedbackActivity.class));
        }

        if(id == R.id.menu_action_see_on_play_store){
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                //TODO ERROR imlementar erro
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        return false;
    }
}
