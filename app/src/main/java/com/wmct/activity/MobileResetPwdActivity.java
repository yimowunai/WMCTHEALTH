package com.wmct.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wmct.health.R;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.CheckUtil;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;
import com.wmct.util.TimeCount;
import com.wmct.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by yimowunai on 2016/1/5.
 */
public class MobileResetPwdActivity extends Activity implements View.OnClickListener {
    public final static String TAG = "MobileResetPwdActivity ";
    EditText et_input_mobile_number;
    EditText et_input_code_number;
    Button bt_get_authcode;
    Button bt_check;

    private String phone;
    private String authcode;

    private Boolean isMobileNoCorrect = false;
    private Boolean isAnthcodeCorrect = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_check_layout);
        initView();
    }

    private void initView() {
        et_input_mobile_number = (EditText) findViewById(R.id.input_phone_number);
        et_input_code_number = (EditText) findViewById(R.id.input_code_number);
        bt_get_authcode = (Button) findViewById(R.id.bt_get_authcode);
        bt_check = (Button) findViewById(R.id.bt_register);
        //点击发送验证码
        bt_get_authcode.setOnClickListener(this);
        //点击验证
        bt_check.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_authcode:
                getAuthcode();
                break;
            case R.id.bt_register:
                checkAuthcode();
                break;

        }
    }

    private void getAuthcode() {

        phone = et_input_mobile_number.getText().toString().trim();
        if (!CheckUtil.isMobileNO(phone)) {
            ToastUtil.showToastLong(this, R.string.check_mobileNo_format_error);
        } else {
            isMobileNoCorrect = true;
            //记时
            final TimeCount time = new TimeCount(bt_get_authcode, 60000, 1000);

            //产生验证码
            authcode = "" + (int) ((Math.random() * 9 + 1) * 100000);
            //发送验证码
            HealthNetAPI.msgResetVerification(phone, authcode, new Request.RequestListener<JSONObject>() {

                        public void onComplete(int stateCode, JSONObject result, String msg) {


                            // et_input_mobile_number.setClickable(false);
                            time.start();
                            Log.i(TAG, "MoblieRegister_result:---------------->" + result);

                            ToastUtil.showToastShort(MobileResetPwdActivity.this, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));


                        }
                    }

            );


        }
    }

    private void checkAuthcode() {
        String inputCode = et_input_code_number.getText().toString().trim();
        if (TextUtils.isEmpty(inputCode)) {
            ToastUtil.showToastShort(this, R.string.input_authcode);
        } else if (inputCode.equals(authcode)) {
            isAnthcodeCorrect = true;
            ToastUtil.showToastShort(this, R.string.check_authcode_correct);
        } else {
            ToastUtil.showToastShort(this, R.string.check_anthcode_error);
        }

        if (isMobileNoCorrect && isAnthcodeCorrect) {
            Intent intent = new Intent();
            intent.putExtra("phone", phone);
            intent.setClass(MobileResetPwdActivity.this, ResetPwdActivity.class);

            Log.i(TAG, "phone------------->" + phone);


            startActivity(intent);
        }
    }
}


