package br.com.expressobits.hbus.provider;

import android.content.Context;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.application.AppManager;
import br.com.expressobits.hbus.ui.model.HotTip;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * @author Rafael Correa
 * @since 12/03/17
 */

public class HotTipsProvider {

    public static int TYPE_GETSTARTED = 0;
    public static int TYPE_RATE_US = 1;
    public static int TYPE_BETA_PROGRAM = 4;

    public static String DISMISS_VIEW_BETA_PROGRAM = "dismiss_view_program";
    public static String DISMISS_VIEW_RATE_US = "dismiss_rate_use";


    public static boolean isViewBetaProgram(Context context){
        //TODO remote config
        return (getDefaultSharedPreferences(context).getBoolean(DISMISS_VIEW_BETA_PROGRAM,true)
                && AppManager.getOpenAppCount(context) >= context.getResources().getInteger(R.integer.counts_view_beta_program));
    }

    public static boolean isViewRateUs(Context context){
        //TODO remote config
        return (getDefaultSharedPreferences(context).getBoolean(DISMISS_VIEW_RATE_US,true)
                && AppManager.getOpenAppCount(context) >= context.getResources().getInteger(R.integer.counts_view_rate_us));
    }


    public static HotTip getGetStartedHotTip(Context context){
        return new HotTip(
                TYPE_GETSTARTED,
                context.getString(R.string.get_started),
                context.getString(R.string.search_for_your_favorite),
                R.drawable.ic_big_bus,
                context.getString(R.string.itineraries)

        );
    }

    public static HotTip getBetaProgramHotTip(Context context){
        return new HotTip(
                TYPE_BETA_PROGRAM,
                context.getString(R.string.hot_tip_become_a_tester),
                context.getString(R.string.hot_tip_turn_an_application_beta_tester_and_test_new_features),
                R.drawable.ic_flask,
                context.getString(R.string.hot_tip_come_on),
                context.getString(R.string.hot_tip_not_now)
        );
    }

    public static HotTip getRateUsHotTip(Context context){
        return new HotTip(
                TYPE_RATE_US,
                context.getString(R.string.hot_tip_rate_us),
                context.getString(R.string.hot_tip_did_you_like_our_app),
                R.drawable.ic_medal,
                context.getString(R.string.hot_tip_come_on),
                context.getString(R.string.hot_tip_not_now)
        );
    }
}
