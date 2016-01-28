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
import br.com.expressobits.hbus.model.Feedback;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael
 * @since 09/01/16.
 */
public class FirebaseDAO{

    private static final String TAG = "Firebase";
    private Firebase firebaseRef;
    public static String reference = "https://hbus.firebaseio.com/";

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
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
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
    public void saveItineraries(City city,List<Itinerary> itineraries){
        firebaseRef.child(ItineraryContract.Itinerary.TABLE_NAME).removeValue();
        for (Itinerary itinerary:itineraries){
            insert(city,itinerary);
        }

    }

    /**
     * Save in firebase home reference into especific city reference
     * @param codes
     */
    public void saveCodes(City city,List<Code> codes){
        firebaseRef.child(CodeContract.Code.TABLE_NAME).removeValue();
        for (Code code:codes){
            insert(city,code);
        }
    }

    /**
     * Save in firebase home reference into especific city reference
     * @param buses
     */
    public void saveBuses(City city,Itinerary itinerary,String way,String typeday,List<Bus> buses){
        firebaseRef.child(BusContract.Bus.TABLE_NAME).removeValue();
        for (Bus bus:buses){
            insert(city,itinerary,bus,way,typeday);
        }
    }

    public void insert(City city){
        Firebase reference = firebaseRef.child(CityContract.City.TABLE_NAME).child(city.getCountry()).child(city.getName());
        reference.setValue(city);
    }

    public void insert(City city,Itinerary itinerary){
        Firebase reference = firebaseRef.child(ItineraryContract.Itinerary.TABLE_NAME).child(city.getCountry()).child(city.getName()).child(itinerary.getName());
        reference.setValue(itinerary);
    }

    public void insert(City city,Code code){
        Firebase reference = firebaseRef.child(CodeContract.Code.TABLE_NAME).child(city.getCountry()).child(city.getName()).child(code.getName());
        reference.setValue(code);
    }

    public void insert(City city,Itinerary itinerary,Bus bus,String way,String typeday){
        Firebase reference = firebaseRef.child(BusContract.Bus.TABLE_NAME).child(city.getCountry())
                .child(city.getName())
                .child(itinerary.getName())
                .child(way)
                .child(typeday)
                .child(bus.getTime());
        reference.setValue(bus);
    }

    public void insert(Feedback feedback){
        firebaseRef.child(FeedbackContract.Feedback.TABLE_NAME).child(feedback.getId().toString()).setValue(feedback);
    }

    public void getCities(ChildEventListener childEventListener,String country){
        Firebase ref = firebaseRef.child(CityContract.City.TABLE_NAME).child(country);
        ref.addChildEventListener(childEventListener);
    }

    public void getItineraries(ChildEventListener childEventListener){
        Firebase ref = firebaseRef.child(ItineraryContract.Itinerary.TABLE_NAME);
        ref.addChildEventListener(childEventListener);
    }

    public void getItineraries(ValueEventListener valueEventListener,City city){
        Firebase ref = firebaseRef.child(ItineraryContract.Itinerary.TABLE_NAME).child(city.getCountry()).child(city.getName());
        ref.addValueEventListener(valueEventListener);
    }

    public void getCodes(ValueEventListener valueEventListener,City city){
        Firebase ref = firebaseRef.child(CodeContract.Code.TABLE_NAME).child(city.getCountry()).child(city.getName());;
        ref.addValueEventListener(valueEventListener);
    }

    public void getBus(ValueEventListener valueEventListener,City city,Itinerary itinerary,String way,String typeday){
        Firebase ref = firebaseRef.child(BusContract.Bus.TABLE_NAME).child(city.getCountry()).child(city.getName())
                .child(itinerary.getName()).child(way).child(typeday);
        ref.addValueEventListener(valueEventListener);
    }

    public void removeAllValues(){
        firebaseRef.child("/").removeValue();
    }


}
