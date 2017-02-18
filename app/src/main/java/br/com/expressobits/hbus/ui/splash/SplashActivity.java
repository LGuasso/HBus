package br.com.expressobits.hbus.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.ManagerInit;
import br.com.expressobits.hbus.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity{

    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(() -> mAuth.addAuthStateListener(mAuthListener), SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = this::verifyUser;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void verifyUser(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            // User is signed in
            Log.d("login", "onAuthStateChanged:signed_in:" + user.getUid());
            ManagerInit.manager(SplashActivity.this);
            SplashActivity.this.finish();

        }else {
            // User is signed out
            Log.d("login", "onAuthStateChanged:signed_out");
            Intent loginIntent = new Intent(SplashActivity.this,LoginActivity.class);
            SplashActivity.this.startActivity(loginIntent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }

        mAuth.removeAuthStateListener(mAuthListener);
    }
}

