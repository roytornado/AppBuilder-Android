package com.midland.base.widget.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midland.base.R;
import com.midland.base.app.BaseApp;
import com.midland.base.util.Ran;

public abstract class InfoBase {
    protected View self;
    protected TextView info_label;

    public InfoBase(LinearLayout main, int resId) {
        LayoutInflater li = (LayoutInflater) main.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        self = li.inflate(resId, null, false);
        info_label = (TextView) self.findViewById(R.id.info_label);
        if (info_label != null) {
            info_label.setId(Ran.ranInt());
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.bottomMargin = BaseApp.dpToPx(8);
        params.topMargin = BaseApp.dpToPx(8);
        main.addView(self, params);
    }

    public void setLabel(String src) {
        info_label.setText(src);
    }

    public View getView() {
        return self;
    }

    public void setVisibility(int flag) {
        self.setVisibility(flag);
    }



}
