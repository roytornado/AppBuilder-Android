package com.midland.base.demo.list;

import android.os.Bundle;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.midland.base.R;
import com.midland.base.activity.BaseActivity;
import com.midland.base.widget.list.ListEvent;
import com.midland.base.widget.list.ListRow;
import com.midland.base.widget.list.ListRowAdapter;

import java.util.ArrayList;

public class MultiTypeListDemo extends BaseActivity {
    PullToRefreshListView listView;
    ArrayList<ListRow> rows = new ArrayList<ListRow>();
    ListRowAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.demo_activity_list);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        ArrayList<String> rowTypeList = new ArrayList<String>();
        rowTypeList.add("DemoSectionHeader");
        rowTypeList.add("DemoAppRow");
        listAdapter = new ListRowAdapter(listView.getRefreshableView(), rows, event, rowTypeList);
        listView.setAdapter(listAdapter);
    }

    private ListEvent event = new ListEvent() {
        public void onListRowClick(View view, int position, ListRow row) {
        }

        public void onRowBtnClick(View view, ListRow row, Bundle bundle) {
            if (row instanceof DemoSectionHeader) {
                String tag = (String) view.getTag();
                if (tag.equals("UPDATE_ALL")) {
                    // Do something
                }
            }
            if (row instanceof DemoAppRow) {
                String tag = bundle.getString("tag");
                if (tag.equals("VIEW")) {
                    DemoAppModel target = ((DemoAppRow) row).target;
                    // Do something for app
                }
            }
        }
    };

    protected void processDataFromServer() {
        rows.clear();
        // Section 1
        rows.add(new DemoSectionHeader("Section 1", event));
        for (int i = 0; i < 10; i++) {
            DemoAppModel model = new DemoAppModel();
            model.name = "name: " + i;
            rows.add(new DemoAppRow(model, event));
        }

        // Section 2
        rows.add(new DemoSectionHeader("Section 2", event));
        for (int i = 0; i < 10; i++) {
            DemoAppModel model = new DemoAppModel();
            model.name = "name: " + i;
            rows.add(new DemoAppRow(model, event));
        }
        listAdapter.notifyDataSetChanged();
    }
}
