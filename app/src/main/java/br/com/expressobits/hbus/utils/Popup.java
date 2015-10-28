package br.com.expressobits.hbus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.OnSettingsListener;

/**
 * Created by Rafael on 16/06/2015.
 */
public class Popup {
    public static void showPopUp(final OnSettingsListener mCallback,View view,final String line,List<String> ways){
        final PopupWindow popupWindow = new PopupWindow(view.getContext());
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.popup_window, null);

        LinearLayout layoutButtons = (LinearLayout) linearLayout.findViewById(R.id.popup_window_buttons_group);

        for(String way : ways){
            final Button button = new Button(view.getContext());
            button.setText(way);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,15,15,15);
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();
                    mCallback.onSettingsDone(line,button.getText().toString());

                }
            });
            layoutButtons.addView(button,params);
        }

        popupWindow.setContentView(linearLayout);
        popupWindow.setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        popupWindow.setBackgroundDrawable(view.getContext().getResources().getDrawable(android.R.color.transparent));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
