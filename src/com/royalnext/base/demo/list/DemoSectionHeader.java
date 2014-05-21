package com.royalnext.base.demo.list;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.midland.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.widget.list.ListEvent;
import com.royalnext.base.widget.list.ListRow;

public class DemoSectionHeader implements ListRow {
    static class ViewHolder {
        TextView header;
    }

    private ListEvent event;
    private String target;

    public DemoSectionHeader(String target, ListEvent event) {
        this.target = target;
        this.event = event;
    }

    @Override
    public String getViewTypeName() {
        return "DemoSectionHeader";
    }

    @Override
    public View getView(View convertView) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(BaseApp.me).inflate(R.layout.info_input_horizontal, null);
            holder = new ViewHolder();
            holder.header = (TextView) convertView.findViewById(R.id.info_text);
            convertView.setTag(holder);
        }
        fill(convertView);
        return convertView;
    }

    public void fill(View view) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.header.setText(Html.fromHtml("<i>" + target + "</i>"));
    }
}
