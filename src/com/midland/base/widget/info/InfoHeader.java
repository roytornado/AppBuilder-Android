package com.midland.base.widget.info;

import android.widget.LinearLayout;

import com.midland.base.R;

public class InfoHeader extends InfoBase {

    public InfoHeader(LinearLayout main) {
        super(main, R.layout.info_header);
    }

    public InfoHeader(LinearLayout main, int res) {
        super(main, R.layout.info_header);
        info_label.setTextAppearance(main.getContext(), res);
    }
}
