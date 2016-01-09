package br.com.expressobits.hbus;

import android.content.Context;
import android.util.Log;

import com.firebase.client.Firebase;

import java.util.List;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.model.City;

/**
 * @author Rafael
 * @since 09/01/16.
 */
public class FirebaseDAO {

    private static final String TAG = "Firebase";
    private Firebase firebaseRef;

    public FirebaseDAO(String initialRef){
        firebaseRef = new Firebase(initialRef);
    }

    /**
     * Define context in firebase
     * Attention IMPORTANT!
     * @param context
     */
    public static void setContext(Context context){
        Firebase.setAndroidContext(context);
    }

    /**
     * Save in firebase home reference into especific city reference
     * @param cities
     */
    public void saveCities(List<City> cities){
        for (City city:cities){
            Log.d(TAG, "Salvando no firebase cidade " + city);
            if(firebaseRef==null){
                Log.d(TAG,"Firebase reference null");
            }
            if(city==null){
                Log.d(TAG,"city reference null");
            }
            if(city.getId()==null){
                Log.d(TAG,"getid reference null");
            }
            firebaseRef.child(CityContract.City.TABLE_NAME).child(city.getId().toString()).setValue(city);
        }

    }
}
