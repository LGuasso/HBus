package br.com.expressobits.hbus.dao;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

import br.com.expressobits.hbus.dao.CityContract;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * @author Rafael
 * @since 09/01/16.
 */
public class FirebaseDAO{

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
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    //TODO implemented connection
    public boolean isConnected(){
        Firebase connected = firebaseRef.child(".info/connected");
        connected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
            }
            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        return true;
    }


    /**
     * Save in firebase home reference into especific city reference
     * @param cities
     */
    public void saveCities(List<City> cities){
        firebaseRef.child(CityContract.City.TABLE_NAME).removeValue();
        for (City city:cities){
            insert(city);
        }

    }

    /**
     * Save in firebase home reference into especific city reference
     * @param itineraries
     */
    public void saveItineraries(List<Itinerary> itineraries){
        firebaseRef.child(ItineraryContract.Itinerary.TABLE_NAME).removeValue();
        for (Itinerary itinerary:itineraries){
            insert(itinerary);
        }

    }

    /**
     * Save in firebase home reference into especific city reference
     * @param codes
     */
    public void saveCodes(List<Code> codes){
        firebaseRef.child(CodeContract.Code.TABLE_NAME).removeValue();
        for (Code code:codes){
            insert(code);
        }
    }

    /**
     * Save in firebase home reference into especific city reference
     * @param buses
     */
    public void saveBuses(List<Bus> buses){
        firebaseRef.child(BusContract.Bus.TABLE_NAME).removeValue();
        for (Bus bus:buses){
            insert(bus);
        }
    }

    public void insert(City city){
        firebaseRef.child(CityContract.City.TABLE_NAME).child(city.getId().toString()).setValue(city);
    }

    public void insert(Itinerary itinerary){
        firebaseRef.child(ItineraryContract.Itinerary.TABLE_NAME).child(new Long(1l).toString()).setValue(itinerary);
    }

    public void insert(Code code){
        firebaseRef.child(CodeContract.Code.TABLE_NAME).child(code.getId().toString()).setValue(code);
    }

    public void insert(Bus bus){
        firebaseRef.child(BusContract.Bus.TABLE_NAME).child(bus.getId().toString()).setValue(bus);
    }

    public void getCities(ChildEventListener childEventListener){
        Firebase ref = firebaseRef.child(CityContract.City.TABLE_NAME);
        ref.addChildEventListener(childEventListener);

    }

}
