package br.com.expressobits.hbus.application;

import android.content.Context;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael Correa
 * @since 25/03/16
 * Gerenciador do aplicativo.
 */
public class AppManager {
    public static int countCloseTimesActivity = 0;

    public static boolean countTimesActivity(Context context){
        if(countCloseTimesActivity>=context.getResources().getInteger(R.integer.counts_close_times_activity)){
            countCloseTimesActivity = 0;
            return true;
        }else {
            countCloseTimesActivity++;
            return false;
        }
    }

}
