package br.com.expressobits.hbus.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author Rafael
 * @since 03/01/16
 */
public class EmailUtils {
    /**
     * Send any email
     * @param context
     * @param subject
     * @param text
     * @param email
     */
    public static void sendEmail(Context context,String subject,String text,String email){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/txt");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setData( Uri.parse("mailto: " +email));
        context.startActivity(intent);
    }
}
