package com.royalnext.base.widget.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.royalnext.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.Ran;

public abstract class InfoBase {
    public static int margin = 0;
    protected View self;
    protected TextView info_label;

    public InfoBase(LinearLayout main, int resId) {
        this(main, resId, BaseApp.dpToPx(margin));
    }

    public InfoBase(LinearLayout main, int resId, int vMargin) {
        LayoutInflater li = (LayoutInflater) main.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        self = li.inflate(resId, null, false);
        info_label = (TextView) self.findViewById(R.id.info_label);
        if (info_label != null) {
            info_label.setId(Ran.ranInt());
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.bottomMargin = vMargin;
        params.topMargin = vMargin;
        main.addView(self, params);
    }

    public void setLabel(String src) {
        info_label.setText(src);
    }

    public View getView() {
        return self;
    }

    public TextView getLabel() {
        return info_label;
    }

    public void setVisibility(int flag) {
        self.setVisibility(flag);
    }


}
