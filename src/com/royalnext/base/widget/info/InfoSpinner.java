package com.royalnext.base.widget.info;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.midland.base.R;
import com.royalnext.base.util.Ran;

public class InfoSpinner extends InfoBase {
    public Spinner spinner;
    public ImageButton btn1;

    public InfoSpinner(LinearLayout main) {
        super(main, R.layout.info_spinner);
        spinner = (Spinner) self.findViewById(R.id.info_spinner);
        spinner.setId(Ran.ranInt());
        btn1 = (ImageButton) self.findViewById(R.id.info_btn1);
        btn1.setId(Ran.ranInt());
    }

    public void setSelections(int resId, Spinner.OnItemSelectedListener listener) {
        String[] selections = self.getContext().getResources().getStringArray(resId);
        setSelections(selections, listener);
    }

    public void setSelections(String[] selections, Spinner.OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(self.getContext(), android.R.layout.simple_spinner_item, selections);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

    public void setSelected(int pos) {
        spinner.setSelection(pos);
    }

    public Object getSelected() {
        return spinner.getSelectedItem();
    }

    public int getSelectedPos() {
        return spinner.getSelectedItemPosition();
    }

}
