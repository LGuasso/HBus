package br.com.expressobits.hbus.push;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * @author Rafael Correa
 * @since 09/12/16
 */

public class SendCodeToFirebase {

    public static final String TABLE_NAME = FirebaseUtils.CODE_TABLE;

    public static void sendToFirebase(City city, Company company,Code code){
        DatabaseReference cityRef = getDatabaseReference(city);
        DatabaseReference companyRef = cityRef.child(company.getName());
        DatabaseReference codeRef = companyRef.child(code.getName());
        codeRef.setValue(code, (DatabaseError de, DatabaseReference dr) -> {
            System.out.println("PUSH "+TABLE_NAME+" "+code.getName());
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
