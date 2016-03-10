package com.wmct.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wmct.health.*;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.CheckUtil;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;
import com.wmct.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by yimowunai on 2016/1/5.
 */
public class ResetPwdActivity extends AppCompatActivity {

    private static final String TAG = "ResetPwdActivity";
    EditText et_reset_pwd;
    Button bt_restet_pwd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd_layout);
        initView();
    }

    private void initView() {
        et_reset_pwd = (EditText) findViewById(R.id.et_reset_pwd);
        bt_restet_pwd = (Button) findViewById(R.id.bt_reset_pwd);
        bt_restet_pwd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                resetPwd();
            }
        });
    }

    private void resetPwd() {
        String pwd = et_reset_pwd.getText().toString().trim();
        String checkResult = CheckUtil.checkPwd(pwd);
        if (!checkResult.equals("Correct")) {
            ToastUtil.showToastShort(this, checkResult);

        } else {
            final Intent intent = getIntent();
            String phone = intent.getStringExtra("phone");
            Log.d(TAG,"要重置的账号是："+phone);
            HealthNetAPI.resetPassword(phone, pwd, new Request.RequestListener<JSONObject>() {
                @Override
                public void onComplete(int stateCode, JSONObject result, String msg) {
                    if (JsonToBean.isOK(result)) {
                        intent.setClass(ResetPwdActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    ToastUtil.showToastLong(ResetPwdActivity.this, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));

                }
            });
        }
    }
}
