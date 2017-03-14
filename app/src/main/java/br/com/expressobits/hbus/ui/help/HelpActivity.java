package br.com.expressobits.hbus.ui.help;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.dialog.VersionInfoDialogFragment;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (findViewById(R.id.appBar) != null) {
                (findViewById(R.id.appBar)).setElevation(4);
            }
        }

        View linearLayoutHelpContent = findViewById(R.id.linearLayoutHelpContent);
        linearLayoutHelpContent.setOnClickListener(this);

        View linearLayoutDeveloperProfile = findViewById(R.id.linearLayoutDeveloperProfile);
        linearLayoutDeveloperProfile.setOnClickListener(this);

        View linearLayoutSendFeedback = findViewById(R.id.linearLayoutSendFeedback);
        linearLayoutSendFeedback.setOnClickListener(this);

        View linearLayoutImagesAndIconsProfile = findViewById(R.id.linearLayoutImagesAndIconsProfile);
        linearLayoutImagesAndIconsProfile.setOnClickListener(this);

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
            startSendFeedback();
        }

        if(id == R.id.menu_action_privacy_policy){
            openPrivacyPolicy();
        }

        if(id == R.id.menu_action_see_on_play_store){
            openAppInPlayStore(this);
        }
        return false;
    }

    public static void openAppInPlayStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void startSendFeedback() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(HelpActivity.this, "Error PackageManager NameNotFoundException", Toast.LENGTH_SHORT).show();
        }
        String version = pInfo.versionName;
        Intent intent = new Intent(this,FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.KEY_VERSION_INFO,version);
        startActivity(intent);
    }

    private void starHelpContent() {
        /*Intent intent = new Intent(this,HelpContentActivity.class);
        startActivity(intent);*/
    }
    public void openPrivacyPolicy() {
        Intent privacyPolicyIntent = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(privacyPolicyIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayoutHelpContent:
                starHelpContent();
                break;

            case R.id.linearLayoutSendFeedback:
                startSendFeedback();
                break;

            case R.id.linearLayoutDeveloperProfile:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Rafinha2108")));
                break;

            case R.id.linearLayoutImagesAndIconsProfile:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.flaticon.com/authors/freepik")));
                break;
        }
    }
}
