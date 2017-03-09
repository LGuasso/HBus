package br.com.expressobits.hbus.provider;

import android.content.Context;

import java.util.Random;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael Correa
 * @since 09/03/17
 */

public class FastTipsProvider {

    public static String getRandomFastTip(Context context){
        String[] fastTips = context.getResources().getStringArray(R.array.list_fast_tips);
        Random r = new Random();
        return fastTips[r.nextInt(fastTips.length)];
    }
}
