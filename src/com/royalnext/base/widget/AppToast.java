package com.royalnext.base.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.midland.base.R;

public class AppToast extends Toast {

    private TextView text;

    public AppToast(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.widget_toast, null);
        text = (TextView) layout.findViewById(R.id.text);
        setGravity(Gravity.BOTTOM, 0, 100);
        setDuration(Toast.LENGTH_LONG);
        setView(layout);
    }

    @Override
    public void setText(CharSequence s) {
        text.setText(s);
    }

    @Override
    public void setText(int resId) {
        text.setText(resId);
    }

}
