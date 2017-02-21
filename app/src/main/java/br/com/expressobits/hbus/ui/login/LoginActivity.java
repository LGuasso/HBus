package br.com.expressobits.hbus.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.ManagerInit;
import br.com.expressobits.hbus.ui.help.PrivacyPolicyActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textViewSignInAnonymously;
    private TextView textViewPrivacyPolicy;
    private TextView textViewLoading;
    private RelativeLayout progressBarLayout;
    private ViewGroup mainLayout;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        overridePendingTransition(R.animator.fade_in,R.animator.fade_out);

        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        // ...
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        textViewSignInAnonymously = (TextView) findViewById(R.id.textViewSignInAnonymously);
        mainLayout = (ViewGroup) findViewById(R.id.layoutMain);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        textViewLoading = (TextView) findViewById(R.id.textViewStatusLoading);
        textViewPrivacyPolicy = (TextView) findViewById(R.id.textViewPrivacyPolicy);
        textViewLoading.setText("");
        textViewSignInAnonymously.setOnClickListener(this);
        textViewPrivacyPolicy.setOnClickListener(this);
        mainLayout.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.INVISIBLE);

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        textViewLoading.setText(getString(R.string.loading_sign_google));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                textViewLoading.setText(getString(R.string.google_sign_sucess));
            } else {
                // Google Sign In failed, update UI appropriately
                // ...


                progressBarLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Google Sign In failed",Toast.LENGTH_LONG).show();
                Log.w(TAG,"Google Sign In failed " +result.getStatus().getStatusMessage());
                Log.w(TAG,"Google Sign In failed " +result.getStatus().toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            mainLayout.setVisibility(View.VISIBLE);
                            progressBarLayout.setVisibility(View.INVISIBLE);
                        }else{
                            ManagerInit.manager(LoginActivity.this);
                            LoginActivity.this.finish();

                        }
                        // ...
                    }
                });
    }


    public void signInAnonymously(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            mainLayout.setVisibility(View.VISIBLE);
                            progressBarLayout.setVisibility(View.INVISIBLE);
                        }else{

                            ManagerInit.manager(LoginActivity.this);
                            LoginActivity.this.finish();
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.textViewSignInAnonymously:
                signInAnonymously();
                break;
            case R.id.textViewPrivacyPolicy:
                openPrivacyPolicy();
                break;

        }
        progressBarLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void openPrivacyPolicy() {
        Intent privacyPolicyIntent = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(privacyPolicyIntent);
    }
}
