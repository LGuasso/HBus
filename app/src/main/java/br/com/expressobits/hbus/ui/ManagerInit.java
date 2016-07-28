package br.com.expressobits.hbus.ui;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.tour.TourActivity;

/**
 * @author Rafael Correa
 * @since 02/11/15.
 */
public class ManagerInit {


    private static final String TAG = "Init";

    public static void manager(final Context context){
        /**FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {*/
                    boolean isTour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TourActivity.TAG, true);
                    Log.e("TESTE",isTour+" isTour");
                    if(isTour){
                        Intent tourIntent = new Intent(context,TourActivity.class);
                        tourIntent.putExtra(TourActivity.STARTER_MODE,true);
                        context.startActivity(tourIntent);
                    }else if(PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG, SelectCityActivity.NOT_CITY).equals(SelectCityActivity.NOT_CITY)){
                        Intent selectCityIntent = new Intent(context,SelectCityActivity.class);
                        selectCityIntent.putExtra(SelectCityActivity.STARTER_MODE,true);
                        context.startActivity(selectCityIntent);
                    }else {
                        // User is signed in
                        //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        context.startActivity(mainIntent);
                    }
                /*} else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent loginIntent = new Intent(context,LoginActivity.class);
                    context.startActivity(loginIntent);
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);*/
    }
}
