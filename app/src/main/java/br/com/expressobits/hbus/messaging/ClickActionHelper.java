package br.com.expressobits.hbus.messaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * implement code in
 * http://stackoverflow.com/questions/38111339/firebase-console-how-to-specify-click-action-for-notifications
 * @author Rafael Correa
 * @since 18/02/17
 */

public class ClickActionHelper {

    private static final String TAG = "ClickActionHelper";

    public static void startActivity(String className, Bundle extras, Context context){
        Class cls;
        try {
            cls = Class.forName(className);
            Intent i = new Intent(context, cls);
            i.putExtras(extras);
            context.startActivity(i);
        }catch(ClassNotFoundException e){
            Log.e(TAG,"means you made a wrong input in firebase console");
        }

    }
}