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
public class ContactUsFragment extends Fragment {
    public final static String TAG = "AppInfoFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactus, null);
        return view;
    }
}
