package com.wmct.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wmct.bean.Family;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.StringUtil;
import com.wmct.util.ToastUtil;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/18 19:40
 * <p/>
 * --------------------------------------
 */
public class FamilyFragment extends Fragment {
    private static final String TAG = "FamilyFragment";
    private Activity mActivity;
    private TextView familyNameText;
    private TextView familyAddressText;
    private TextView terminalIdText;
    private TextView familyPhoneText;
    //private EditText familyNameEdit;
    private Family family;
    private HealthApp healthApp;
    //private Button edit;
    //private Button submit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.family_fragment_layout, null);
        initView(view);
        Log.d(TAG, "FamilyFragment onCreateView");
        // initEvent();
        return view;
    }

    /*private void initEvent() {
        edit.setOnClickListener(this);
        submit.setOnClickListener(this);
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        healthApp = (HealthApp) activity.getApplication();
        family = healthApp.getFamily();
        Log.d(TAG, "FamilyFragment onAttach");
    }

    private void initView(View view) {
        familyNameText = (TextView) view.findViewById(R.id.familyName_text);
        familyAddressText = (TextView) view.findViewById(R.id.familyAddress_text);
        terminalIdText = (TextView) view.findViewById(R.id.terminalId_text);
        familyPhoneText = (TextView) view.findViewById(R.id.familyPhone_text);
        // familyNameEdit = (EditText) view.findViewById(R.id.familyName_edit);
        familyNameText.setText(family.getName());
        familyAddressText.setText(family.getAddress());
        terminalIdText.setText(family.getTerminalid());
        familyPhoneText.setText(family.getPhone());
        //edit = (Button) view.findViewById(R.id.family_edit);
        // submit = (Button) view.findViewById(R.id.family_submit);
    }
   /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.family_edit:
                //Toast.makeText(mActivity, "点击了编辑", Toast.LENGTH_LONG).show();
                submit.setVisibility(View.VISIBLE);
                changeToEditMode();
                break;
            case R.id.family_submit:
                saveMemberInfo();
                changeToDisplayMode();
                break;
        }
    }

 private void changeToDisplayMode() {
        familyNameEdit.setVisibility(View.GONE);
        familyNameText.setVisibility(View.VISIBLE);
    }

    private void saveMemberInfo() {
        String familyName = familyNameEdit.getText().toString().trim();
        if(StringUtil.isEmpty(familyName)){
            ToastUtil.showToastShort(mActivity, R.string.input_username);
            return;
        }
        family.setName(familyName);
        HealthNetAPI.
    }

    private void changeToEditMode() {
        familyNameEdit.setVisibility(View.VISIBLE);
        familyNameText.setVisibility(View.GONE);
}*/
}
