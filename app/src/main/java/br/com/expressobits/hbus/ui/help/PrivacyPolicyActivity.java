package br.com.expressobits.hbus.ui.help;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import br.com.expressobits.hbus.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webViewPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        this.webViewPrivacyPolicy = (WebView) findViewById(R.id.webViewPrivacyPolicy);
        webViewPrivacyPolicy.loadUrl(getString(R.string.site_privacy_policy));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
