package br.com.expressobits.hbus.push;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Rafael Correa
 * @since 09/12/16
 */

public class SendNewsToFirebase {

    public static final String TABLE_NAME = FirebaseUtils.NEWS_TABLE;

    public static void sendToFirebase(City city,News news){
        DatabaseReference cityRef = getDatabaseReference(city);
        DatabaseReference newsRef = cityRef.child(String.valueOf(news.getTime()));
        newsRef.setValue(news, (DatabaseError de, DatabaseReference dr) -> {
            System.out.println("PUSH "+TABLE_NAME+" "+news.getTime());
        });
    }

    public static void sendToFirebase(News news){
        DatabaseReference topRef = getDatabaseReference();
        DatabaseReference newsRef = topRef.child(String.valueOf(news.getTime()));
        newsRef.setValue(news, (DatabaseError de, DatabaseReference dr) -> {
            System.out.println("PUSH "+TABLE_NAME+" "+news.getTime());
        });
    }


    public static DatabaseReference getDatabaseReference(City city) {
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(TABLE_NAME);
        DatabaseReference cityTableRef = ref.child(FirebaseUtils.CITY_TABLE);
        DatabaseReference countryRef = cityTableRef.child(city.getCountry());
        DatabaseReference cityRef = countryRef.child(city.getName());
        return cityRef;
    }

    public static DatabaseReference getDatabaseReference() {
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(TABLE_NAME);

        DatabaseReference generalTableRef = ref.child(FirebaseUtils.GENERAL);
        return generalTableRef;
    }


    /**public static void removeAllValues(City city){
        DatabaseReference cityRef = getDatabaseReference(city);
        cityRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr) {
                System.out.println("REMOVED ALL VALUES");
            }
        });
    }*/
}
