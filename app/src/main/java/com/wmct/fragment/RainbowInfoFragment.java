package com.wmct.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wmct.health.R;


/**
 * Created by yimowunai on 2016/1/14.
 */
public class RainbowInfoFragment extends Fragment {
    public final static String TAG = "RainbowInfoFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rainbowinfo, null);
        return view;
    }
}
