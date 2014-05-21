package com.royalnext.base.demo.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.midland.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.widget.list.ListEvent;
import com.royalnext.base.widget.list.ListRow;

public class DemoAppRow implements ListRow {
    static class ViewHolder {
        TextView name;
        ImageButton action;
        View mainView;
    }

    public DemoAppModel target;
    private ListEvent event;

    public DemoAppRow(DemoAppModel target, ListEvent event) {
        this.target = target;
        this.event = event;
    }

    @Override
    public String getViewTypeName() {
        return "DemoAppRow";
    }

    @Override
    public View getView(View convertView) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(BaseApp.me).inflate(R.layout.info_input_horizontal, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.info_text);
            holder.action = (ImageButton) convertView.findViewById(R.id.info_input_action_btn);
            holder.mainView = convertView;
            convertView.setTag(holder);
        }
        fill(convertView);
        return convertView;
    }

    public void fill(View view) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(target.name);
        holder.action.setVisibility(View.VISIBLE);
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "INSTALL");
                event.onRowBtnClick(view, DemoAppRow.this, bundle);
            }
        });
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "VIEW");
                event.onRowBtnClick(view, DemoAppRow.this, bundle);
            }
        });
    }
}
