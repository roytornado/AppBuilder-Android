package com.royalnext.base.widget.list;

import android.content.Context;
import android.view.View;

public interface ListRow {
    public View getView(View convertView);

    public String getViewTypeName();

    public interface ListRowContext {
        public View getView(Context context, View convertView);
    }
}
