package com.royalnext.base.widget.list;

import android.view.View;

public interface ListRow {
    public View getView(View convertView);

    public String getViewTypeName();
}
