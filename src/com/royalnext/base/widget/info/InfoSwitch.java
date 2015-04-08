package com.royalnext.base.widget.info;

import android.widget.LinearLayout;
import android.widget.Switch;

import com.royalnext.base.R;
import com.royalnext.base.util.Ran;

public class InfoSwitch extends InfoBase {
    public Switch switchView;

    public InfoSwitch(LinearLayout main) {
        super(main, R.layout.info_switch);
        switchView = (Switch) self.findViewById(R.id.info_switch);
        switchView.setId(Ran.ranInt());
    }
}
