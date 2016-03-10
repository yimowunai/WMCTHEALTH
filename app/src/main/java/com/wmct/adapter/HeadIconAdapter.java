package com.wmct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.wmct.health.R;


/**
 * Created by yimowunai on 2016/1/19.
 */
public class HeadIconAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Integer images[];
    Context context;

    public HeadIconAdapter(Context context, Integer[] images) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.images = images;
    }

    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_layout, null);
            convertView.setLayoutParams(new GridView.LayoutParams(80,80));
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.img_head_icon);
        img.setImageResource(images[position]);

        img.setImageResource(images[position]);
        return convertView;
    }
}
