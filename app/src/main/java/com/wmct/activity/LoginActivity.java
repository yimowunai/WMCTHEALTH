package com.wmct.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wmct.bean.Family;
import com.wmct.bean.Member;
import com.wmct.health.*;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.service.DownDataService;
import com.wmct.util.CheckUtil;
import com.wmct.util.JsonToBean;
import com.wmct.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by yimowunai on 2016/1/5.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    public final static String TAG = "LoginActivity";

    private HealthApp healthApp;
    EditText et_phone;
    EditText et_pwd;
    Button bt_login;
    TextView tx_register;
    TextView tx_Reset_pwd;
    TextView tx_exit;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_login_layout);
        healthApp = (HealthApp) getApplication();
        initView();
    }

    private void initView() {

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.setText(healthApp.getFamily().getPhone());
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_login = (Button) findViewById(R.id.bt_login);

        tx_register = (TextView) findViewById(R.id.tx_register);
        tx_Reset_pwd = (TextView) findViewById(R.id.tx_Reset_pwd);
        tx_exit = (TextView) findViewById(R.id.tx_exit);

        tx_exit.setOnClickListener(this);
        tx_register.setOnClickListener(this);
        tx_Reset_pwd.setOnClickListener(this);
        bt_login.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_register:
                startJumpTo(MoblieRegisterActivity.class);
                break;
            case R.id.tx_Reset_pwd:
                startJumpTo(MobileResetPwdActivity.class);
                break;
            case R.id.tx_exit:
                finish();
                break;
            case R.id.bt_login:
                login();
                break;
        }
    }

    private void startJumpTo(Class c) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        startActivity(intent);

    }

    public void login() {

        String phone = et_phone.getText().toString().trim();
        if (!CheckUtil.isMobileNO(phone)) {
            ToastUtil.showToastLong(this, R.string.check_mobileNo_format_error);
            return;
        }
        String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToastShort(this, R.string.input_pwd);
            return;
        }
        final Family family = new Family();
        family.setPhone(phone);
        family.setPwd(pwd);
        family.setTerminalid(healthApp.getTerminalId());
        HealthNetAPI.loginold(family, new Request.RequestListener<JSONObject>() {

            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.i(TAG, "Login_result----------->" + result);

                if (JsonToBean.isOK(result)) {
                    try {
                        JSONObject jsonObject = result.getJSONObject("family");
                        String name = jsonObject.getString("name");
                        family.setName(name);
                        String address = jsonObject.getString("address");
                        family.setAddress(address);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    healthApp.saveFamilyInfo(family);
                    List<Member> members = JsonToBean.toMemberList(result);
                    Log.d(TAG, members.toString());
                    healthApp.setMembers(members);
                    VisitorMeasureActivity.instance.finish();
                    Intent intent = new Intent(LoginActivity.this, DownDataService.class);
                    startService(intent);
                    startJumpTo(MainActivity.class);
                    finish();
                }
                // ToastUtil.showToastLong(LoginActivity.this, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));

            }
        });

    }


}
