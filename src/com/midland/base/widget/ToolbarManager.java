package com.midland.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midland.base.R;

import java.util.Vector;

public class ToolbarManager {
    private class ToolbarItem {
        public String tag;
        public int img;
        public int label;
        public OnClickListener listener;
        public View main;
    }

    public LinearLayout section_tool_bar;
    private Vector<ToolbarItem> items = new Vector<ToolbarItem>();

    public ToolbarManager(LinearLayout section_tool_bar) {
        super();
        this.section_tool_bar = section_tool_bar;
    }

    public void add(int img, int label, OnClickListener listener, String tag) {
        ToolbarItem item = new ToolbarItem();
        item.tag = tag;
        item.img = img;
        item.label = label;
        item.listener = listener;
        items.add(item);
    }

    public void add(int img, int label, OnClickListener listener) {
        ToolbarItem item = new ToolbarItem();
        item.tag = "";
        item.img = img;
        item.label = label;
        item.listener = listener;
        items.add(item);
    }

    public View findByTag(String tag) {
        for (ToolbarItem item : items) {
            if (item.tag.length() > 0 && item.tag.equals(tag)) {
                return item.main;
            }
        }
        return null;
    }

    public void free() {
        for (ToolbarItem item : items) {
            item.listener = null;
            item.main = null;
        }
        items.clear();
    }

    public void build() {
        LayoutInflater li = (LayoutInflater) section_tool_bar.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (items.size() == 1) {
            fill();
            for (ToolbarItem item : items) {
                View main = li.inflate(R.layout.tool_bar_item, null, false);
                item.main = main;
                ImageView img = (ImageView) main.findViewById(R.id.toolbar_item_img);
                TextView label = (TextView) main.findViewById(R.id.toolbar_item_label);
                main.setBackgroundResource(R.drawable.btn_tran);
                main.setOnClickListener(item.listener);
                main.setTag(item.tag);
                if (item.img > -1) {
                    img.setImageResource(item.img);
                } else {
                    img.setVisibility(View.GONE);
                }
                if (item.label > -1) {
                    label.setText(item.label);
                } else {
                    label.setVisibility(View.GONE);
                }
                section_tool_bar.addView(main, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            }
        } else if (items.size() > 1) {
            fill();
            for (ToolbarItem item : items) {
                View main = li.inflate(R.layout.tool_bar_item, null, false);
                item.main = main;
                ImageView img = (ImageView) main.findViewById(R.id.toolbar_item_img);
                TextView label = (TextView) main.findViewById(R.id.toolbar_item_label);
                main.setBackgroundResource(R.drawable.btn_tran);
                main.setOnClickListener(item.listener);
                main.setTag(item.tag);
                if (item.img > -1) {
                    img.setImageResource(item.img);
                } else {
                    img.setVisibility(View.GONE);
                }
                if (item.label > -1) {
                    label.setText(item.label);
                } else {
                    label.setVisibility(View.GONE);
                }
                section_tool_bar.addView(main, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                fill();
            }
        } else {

        }
    }

    private void fill() {
        View dummy = new View(section_tool_bar.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        section_tool_bar.addView(dummy, params);
    }
}
