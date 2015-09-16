package br.com.expressobits.hbus;

import android.view.View;

/**
 * @author Rafael
 * @since 25/06/2015.
 * Comunicação entre adapeter de recyceler view e sua fragment/activity
 */
public interface RecyclerViewOnClickListenerHack {
    public void onClickListener(View view, int position);
    public boolean onLongClickListener(View view,int position);
}
