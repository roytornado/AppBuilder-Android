package com.midland.base.widget.info;

import android.text.method.KeyListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.midland.base.R;
import com.midland.base.util.Ran;

public class InfoInputH extends InfoInputBase {
    public ImageButton action_btn;

    public InfoInputH(LinearLayout main) {
        super(main, R.layout.info_input_horizontal);
        action_btn = (ImageButton) self.findViewById(R.id.info_input_action_btn);
        action_btn.setId(Ran.ranInt());
    }

    public void setKeyListener(KeyListener l) {
        input_box.setKeyListener(l);
    }

}
