package com.wmct.apptest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/15 17:35
 * <p/>
 * --------------------------------------
 */
public class MemberInfoFragment extends Fragment {
    private static final String TAG = "MemberInfoFragment";
    private EditText memberNameEdit;
    private EditText memberGenderEdit;
    private EditText memberAgeEdit;
    private TextView memberNameText;
    private TextView memberGenderText;
    private TextView memberAgeText;
    private boolean isModifing = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_info, null);
        initView(view);
        Log.d(TAG, "MemberInfoFragment onCreateView");
        return view;
    }


    private void initView(View view) {
        memberNameEdit = (EditText) view.findViewById(R.id.memberName_edit);
        memberAgeEdit = (EditText) view.findViewById(R.id.memberAge_edit);
        memberGenderEdit = (EditText) view.findViewById(R.id.memberGender_edit);
        memberNameText = (TextView) view.findViewById(R.id.memberName_text);
        memberGenderText = (TextView) view.findViewById(R.id.memberGender_text);
        memberAgeText = (TextView) view.findViewById(R.id.memberAge_text);
    }

    public void saveMemberInfo(){
        String memberName = memberNameEdit.getText().toString().trim();
        String memberAge = memberAgeEdit.getText().toString().trim();
        String memberGender = memberGenderEdit.getText().toString().trim();

    }

    public void changeToEditMode() {
        Log.d(TAG, "changeToEditMode");
        memberNameText.setVisibility(View.GONE);
        memberGenderText.setVisibility(View.GONE);
        memberAgeText.setVisibility(View.GONE);
        memberNameEdit.setVisibility(View.VISIBLE);
        memberAgeEdit.setVisibility(View.VISIBLE);
        memberGenderEdit.setVisibility(View.VISIBLE);
    }

    public void changeToDisplayMode() {
        Log.d(TAG, "changeToDisplayMode");
        memberNameText.setVisibility(View.VISIBLE);
        memberGenderText.setVisibility(View.VISIBLE);
        memberAgeText.setVisibility(View.VISIBLE);
        memberNameEdit.setVisibility(View.GONE);
        memberAgeEdit.setVisibility(View.GONE);
        memberGenderEdit.setVisibility(View.GONE);
        Log.d(TAG, "changeToDisplayMode finish");
    }

    public void setIsModifing(boolean isModifing) {
        this.isModifing = isModifing;
    }

    public boolean isModifing() {
        return isModifing;
    }

}
