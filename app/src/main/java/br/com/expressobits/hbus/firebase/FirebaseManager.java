package br.com.expressobits.hbus.firebase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @author 13/06/16.
 */
public class FirebaseManager {

    private static final String TAG = "FirebaseManager";

    public static void loadBusesForItinerary(Itinerary itinerary){
        new AsyncTask<Itinerary,Void,Void>(){

            @Override
            protected Void doInBackground(Itinerary... params) {
                Itinerary itinerary = params[0];
                final HashMap<String,Code> codes = new HashMap<>();

                final String country = FirebaseUtils.getCountry(itinerary.getId());
                final String city = FirebaseUtils.getCityName(itinerary.getId());
                final String company = FirebaseUtils.getCompany(itinerary.getId());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference busTable = database.getReference(FirebaseUtils.BUS_TABLE);
                DatabaseReference countryRef = busTable.child(country);
                DatabaseReference cityRef = countryRef.child(city);
                DatabaseReference companyRef = cityRef.child(company);
                DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
                for(String way:itinerary.getWays()){
                    DatabaseReference wayRef = itineraryRef.child(way);
                    for(TypeDay typeDay:TypeDay.values()){
                        DatabaseReference typeday = wayRef.child(typeDay.toString());
                        typeday.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Bus bus = dataSnapshot.getValue(Bus.class);
                                Log.e("BUS",bus.toString());

                                Code code = new Code();
                                code.setId(FirebaseUtils.getIdCode(country,city,company,bus.getCode()));
                                code.setName(bus.getCode());
                                if(codes.containsKey(code.getName())){
                                    Log.e("CODE","Contains "+code.toString());
                                }else{
                                    codes.put(code.getName(),code);
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference codeTable = database.getReference(FirebaseUtils.CODE_TABLE);
                                    DatabaseReference countryRef = codeTable.child(country);
                                    DatabaseReference cityRef = countryRef.child(city);
                                    DatabaseReference companyRef = cityRef.child(company);
                                    DatabaseReference codeRef = companyRef.child(code.getName());
                                    codeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Code code1 = dataSnapshot.getValue(Code.class);
                                            if(code1!=null){
                                                Log.e("CODE",code1.toString());
                                            }else{
                                                Log.e("CODE","code is null!");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
                return null;
            }
        }.execute(itinerary);


    }
}
