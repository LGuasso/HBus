package br.com.expressobits.hbusmanager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Rafael
 * @since 29/11/16
 */
public class MainTest {

    public static void main(String[] args) throws FileNotFoundException {


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("HBus-908dede63e15.json"))
                .setDatabaseUrl("https://hbus-1206.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);


        /**SendData sendData = new SendData();
        City city = sendData.readData("BR/RS","Cruz Alta");
        sendData.pushCity(city);
        JOptionPane.showMessageDialog(null,"Aguarde!");*/
        HBusManager hBusManager = new HBusManager();


    }
}
