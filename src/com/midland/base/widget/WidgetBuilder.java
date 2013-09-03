package com.midland.base.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.midland.base.R;
import com.midland.base.app.BaseApp;

public class WidgetBuilder {

    public static LinearLayout linearLayoutH(Context context) {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.HORIZONTAL);
        return view;
    }

    public static LinearLayout linearLayoutV(Context context) {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
    }

    public static Button buildBtn(LinearLayout layout, Button button, int width, int height, float weight, int stringRes, OnClickListener listener) {
        return buildBtn(layout, button, width, height, weight, 8, stringRes, listener);
    }

    public static Button buildBtn(LinearLayout layout, Button button, int width, int height, float weight, int margin, int stringRes, OnClickListener listener) {
        button.setText(BaseApp.me.getString(stringRes));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(margin, margin, margin, margin);
        if (weight > 0) {
            params.weight = weight;
        }
        params.gravity = Gravity.CENTER;
        layout.addView(button, params);
        button.setOnClickListener(listener);
        return button;
    }

    public static Button btnBlue() {
        Button view = new Button(BaseApp.me);
        view.setBackgroundResource(R.drawable.btn_blue);
        view.setTextAppearance(BaseApp.me, R.style.btn_normal);
        return view;
    }

    public static Button btnRed() {
        Button view = new Button(BaseApp.me);
        view.setBackgroundResource(R.drawable.btn_red);
        view.setTextAppearance(BaseApp.me, R.style.btn_normal);
        return view;
    }

    public static void showPopupDialog(Context c, String msg) {
        Builder alert_dialog = new AlertDialog.Builder(c);
        alert_dialog.setTitle(c.getString(R.string.app_name));
        alert_dialog.setCancelable(false);
        alert_dialog.setMessage(msg);
        alert_dialog.setNegativeButton(c.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert_dialog.show();
    }

    public static void showPopupDialog(Context c, String msg, String btn, final DialogInterface.OnClickListener btnListener) {
        Builder alert_dialog = new AlertDialog.Builder(c);
        alert_dialog.setTitle(c.getString(R.string.app_name));
        alert_dialog.setCancelable(false);
        alert_dialog.setMessage(msg);
        alert_dialog.setNeutralButton(btn, btnListener);
        alert_dialog.show();
    }
}
