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
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;

import org.json.JSONObject;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/19 10:06
 * <p/>
 * --------------------------------------
 */
public class ModifyPasswordFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ModifyPasswordFragment";
    private Activity mActivity;
    private HealthApp healthApp;
    private Family family;
    private TextView familyName;
    private EditText oldPasswordEdit;
    private EditText newPasswordEdit;
    private EditText newPasswordConfirmEdit;
    private Button submit;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        healthApp = (HealthApp) mActivity.getApplication();
        family = healthApp.getFamily();
        Log.d(TAG, "ModifyPasswordFragment onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modifypwd_fragment_layout, null);
        initView(view);
        initData();
        initEvent();
        Log.d(TAG, "ModifyPasswordFragment onCreateView");
        return view;
    }

    private void initData() {
        familyName.setText(family.getName());
    }

    private void initEvent() {
        submit.setOnClickListener(this);
    }

    private void initView(View view) {
        oldPasswordEdit = (EditText) view.findViewById(R.id.oldPassword_edit);
        newPasswordEdit = (EditText) view.findViewById(R.id.newPassword_edit);
        newPasswordConfirmEdit = (EditText) view.findViewById(R.id.newPasswordConfirm_edit);
        familyName = (TextView) view.findViewById(R.id.modifyPwdName_text);
        submit = (Button) view.findViewById(R.id.modifyPwd_submit);
    }

    @Override
    public void onClick(View v) {
        String oldPassword = oldPasswordEdit.getText().toString().trim();
        final String newPassword = newPasswordEdit.getText().toString().trim();
        String newPasswordConfirm = newPasswordConfirmEdit.getText().toString().trim();
        if (StringUtil.isEmpty(oldPassword)) {

            return;
        }
        if (StringUtil.isEmpty(newPassword)) {

            return;
        }
        if (StringUtil.isEmpty(newPasswordConfirm)) {

            return;
        }
        if (!newPassword.equals(newPasswordConfirm)) {

            return;
        }
        if (!family.getPwd().equals(oldPassword)) {

            return;
        }
        HealthNetAPI.modifiPassword(family.getPhone(), oldPassword, newPassword, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                if (JsonToBean.isOK(result)) {
                    family.setPwd(newPassword);
                    healthApp.setProperty("password", newPassword);
                    resetPasswordEdit();
                }
            }
        });


    }

    private void resetPasswordEdit() {
        oldPasswordEdit.setText("");
        newPasswordEdit.setText("");
        newPasswordConfirmEdit.setText("");
    }
}
