package br.com.expressobits.hbus;

import android.view.View;

/**
 * @author Rafael
 * @since 25/06/2015.
 * Comunicação entre adapter de recyceler view e sua fragment/activity
 */
public interface RecyclerViewOnClickListenerHack {
    void onClickListener(View view, int position);
    boolean onLongClickListener(View view, int position);
}
