package com.royalnext.base.widget.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListRowAdapter extends BaseAdapter {
    private ArrayList<ListRow> rows;
    private ArrayList<String> typeList;
    private ListEvent event;

    public ListRowAdapter(ListView list, ArrayList<ListRow> _rows, ListEvent _event, ArrayList<String> typeList) {
        rows = _rows;
        event = _event;
        this.typeList = typeList;
        for (ListRow row : rows) {
            String rowId = row.getViewTypeName();
            if (!typeList.contains(rowId)) {
                typeList.add(rowId);
            }
        }
        if (list != null && event != null) {
            list.setOnItemClickListener(listClick);
        }
    }

    final OnItemClickListener listClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                ListRow row = (ListRow) getItem(position);
                event.onListRowClick(view, position, row);
            }catch (Exception e){

            }
        }
    };

    @Override
    public int getViewTypeCount() {
        return typeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < typeList.size(); i++) {
            if (typeList.get(i).equals((rows.get(position).getViewTypeName()))) {
                return i;
            }
        }
        return 0;
    }

    public int getCount() {
        return rows.size();
    }

    public Object getItem(int position) {
        return rows.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = rows.get(position).getView(convertView);
        return view;
    }
}
