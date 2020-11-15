package br.com.expressobits.hbus.push;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 09/12/16
 */

public class SendCityToFirebase{

    public static final String TABLE_NAME = FirebaseUtils.CITY_TABLE;

    public static void sendToFirebase(City city){
        DatabaseReference cityRef = getDatabaseReference(city);
        cityRef.setValue(city, (DatabaseError de, DatabaseReference dr) -> {
            System.out.println("PUSH "+TABLE_NAME+" "+city.getName());
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

    public static void removeAllValues(City city){
        DatabaseReference cityRef = getDatabaseReference(city);
        cityRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr) {
                System.out.println("REMOVED ALL VALUES");
            }
        });
    }

}
