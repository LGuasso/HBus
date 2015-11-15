package br.com.expressobits.hbus.dao;

import com.firebase.client.Firebase;

/**@author Rafael Correa
 * @since 15/11/15
 * @
 */
public class FirebaseDAO {

    Firebase firebase;

    public FirebaseDAO(){
        firebase = new Firebase("https://hbus.firebaseio.com/");
    }

    public void setCity(String cityName){
        firebase.child("city").setValue(cityName);
    }
}
