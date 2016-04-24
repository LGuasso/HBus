package br.com.expressobits.hbus.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * http://stackoverflow.com/questions/26296270/alarmmanager-not-trigger-after-the-phone-reset?lq=1
 * @author Rafael Correa
 * @since 21/04/16
 */
public class BootReceiver extends BroadcastReceiver{
    private static final String BOOT_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON =
            "android.intent.action.QUICKBOOT_POWERON";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BOOT_COMPLETED) ||
                action.equals(QUICKBOOT_POWERON)) {
            Intent service = new Intent(context, BootService.class);
            context.startService(service);
        }
    }
}
