package com.royalnext.base.widget.info;

import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midland.base.R;
import com.royalnext.base.util.Ran;

public abstract class InfoTextBase extends InfoBase {
    public TextView info_text;

    public InfoTextBase(LinearLayout main, int resId) {
        super(main, resId);
        info_text = (TextView) self.findViewById(R.id.info_text);
        info_text.setId(Ran.ranInt());
    }

    public void setText(String src) {
        info_text.setText(src);
    }

    public String getText() {
        return info_text.getText().toString();
    }

    public void setIcon(int res) {
        final Drawable icon = self.getResources().getDrawable(res);
        icon.setBounds(0, 0, 30, 30);
        info_text.setCompoundDrawables(null, null, icon, null);
    }

    public void link() {
        Linkify.addLinks(info_text, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
    }

    @Override
    public String toString() {
        return info_label.getText() + ": " + info_text.getText();
    }

}
