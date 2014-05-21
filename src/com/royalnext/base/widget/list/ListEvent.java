package com.royalnext.base.widget.list;

import android.os.Bundle;
import android.view.View;

public interface ListEvent {
    public void onListRowClick(View view, int position, ListRow row);

    public void onRowBtnClick(View view, ListRow row, Bundle bundle);

}
