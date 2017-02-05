package br.com.expressobits.hbus.push;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.utils.FirebaseUtils;

import java.util.List;

/**
 * @author Rafael Correa
 * @since 09/12/16
 */

public class SendBusToFirebase {

    public static final String TABLE_NAME = FirebaseUtils.BUS_TABLE;

    public static void sendToFirebase(City city, Company company, Itinerary itinerary,List<Bus> buses){
        DatabaseReference cityRef = getDatabaseReference(city);
        DatabaseReference companyRef = cityRef.child(company.getName());
        DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
        itineraryRef.removeValue((databaseError, databaseReference) -> {

            for(int j=0;j<buses.size();j++){
                Bus bus = buses.get(j);
                DatabaseReference wayRef = itineraryRef.child(bus.getWay());
                DatabaseReference typedayRef = wayRef.child(bus.getTypeday());
                DatabaseReference busRef = typedayRef.child(String.valueOf(bus.getTime()));
                busRef.setValue(bus);
                //publishProgress((int) ((j+1 / buses.size()) * 100));
            }
        });
    }


    public static DatabaseReference getDatabaseReference(City city) {
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(TABLE_NAME);
        DatabaseReference countryRef = ref.child(city.getCountry());
        DatabaseReference cityRef = countryRef.child(city.getName());
        return cityRef;
    }

    public static void removeAllValues(City city,Company company,RemoveListener removeListener){
        DatabaseReference cityRef = getDatabaseReference(city);
        DatabaseReference companyRef = cityRef.child(company.getName());
        companyRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr) {
                if(removeListener!=null){
                    removeListener.removeSucess("Remove all "+TABLE_NAME+" values of "+city.getName());
                }
                System.out.println("REMOVED ALL VALUES");
            }
        });
    }
}
