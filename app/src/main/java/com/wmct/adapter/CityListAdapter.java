package com.wmct.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wmct.bean.CityListItem;

import java.util.List;

public class CityListAdapter extends BaseAdapter {

    private Context context;
    private List<CityListItem> myList;

    public CityListAdapter(Context context, List<CityListItem> myList) {
        this.context = context;
        this.myList = myList;
    }

    public int getCount() {
        return myList.size();
    }

    public Object getItem(int position) {
        return myList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CityListItem cityListItem = myList.get(position);
        return new MyAdapterView(this.context, cityListItem);
    }

    class MyAdapterView extends LinearLayout {
        public static final String LOG_TAG = "MyAdapterView";

        public MyAdapterView(Context context, CityListItem cityListItem) {
            super(context);
            this.setOrientation(HORIZONTAL);

            LayoutParams params = new LayoutParams(200, LayoutParams.WRAP_CONTENT);
            params.setMargins(1, 1, 1, 1);

            TextView name = new TextView(context);
            name.setText(cityListItem.getName());
            addView(name, params);

            LayoutParams params2 = new LayoutParams(200, LayoutParams.WRAP_CONTENT);
            params2.setMargins(1, 1, 1, 1);

            TextView pcode = new TextView(context);
            pcode.setText(cityListItem.getPcode());
            addView(pcode, params2);
            pcode.setVisibility(GONE);

        }

    }

}