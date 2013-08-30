package com.midland.base.widget.list;

import android.view.View;

public interface ListRow {
    public View getView(View convertView);

    public String getViewTypeName();
}
