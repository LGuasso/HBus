package br.com.expressobits.hbus.push;

import br.com.expressobits.hbus.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Rafael Correa
 * @since 14/12/16
 */
public class CountryFromFirebase {

    public static final String TABLE_NAME = FirebaseUtils.CITY_TABLE;

    public static DatabaseReference getDatabaseReference(String country) {
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(TABLE_NAME);
        DatabaseReference countryRef = ref.child(country);
        return countryRef;
    }
}
