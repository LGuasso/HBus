package br.com.expressobits.hbus.application;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.ui.MainActivity;

/**
 * Created by rafael on 15/09/15.
 */
public class TPCDataBaseInit extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(Void... params) {
                doOneThing();
                return null;
            }
            protected void onPostExecute(Boolean result) {
                /**AsyncTask doAnotherThing =
                        new AsyncTask<Void, Void, Boolean>() {
                            protected Boolean doInBackground(Void... params) {
                                doYetAnotherThing();
                                return true;
                            }
                            private void doYetAnotherThing() {

                            }
                        };
                doAnotherThing.execute();**/
                call();
            }
            private void doOneThing() {
                new LinhaFile(TPCDataBaseInit.this).init();
            }
        }.execute();

    }


    private void call(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
