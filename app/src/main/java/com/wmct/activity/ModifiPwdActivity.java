package com.wmct.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wmct.health.R;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.CheckUtil;
import com.wmct.util.StringUtil;
import com.wmct.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by yimowunai on 2016/1/11.
 */
public class ModifiPwdActivity extends AppCompatActivity {
    public final static String TAG = "ModifiPwdActivity";
    EditText et_input_phone;
    EditText et_input_old_pwd;
    EditText et_input_new_pwd;
    EditText et_input_new_pwd_again;

    Button bt_finish;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifi_pwd_layout);
        initView();
    }

    private void initView() {

        et_input_phone = (EditText) findViewById(R.id.et_input_phone);
        et_input_old_pwd = (EditText) findViewById(R.id.et_input_old_pwd);
        et_input_new_pwd = (EditText) findViewById(R.id.et_input_new_pwd);
        et_input_new_pwd_again = (EditText) findViewById(R.id.et_input_new_pwd_again);
        bt_finish = (Button) findViewById(R.id.bt_finish);

        bt_finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mobileNo = et_input_phone.getText().toString().trim();
                if (!CheckUtil.isMobileNO(mobileNo)) {
                    ToastUtil.showToastShort(ModifiPwdActivity.this, R.string.check_mobileNo_format_error);

                    return;
                }
                //密码
                String oldPwd = et_input_old_pwd.getText().toString().trim();
                String oldCheckResult = CheckUtil.checkPwd(oldPwd);
                if (!oldCheckResult.equals("Correct")) {
                    ToastUtil.showToastShort(ModifiPwdActivity.this, oldCheckResult);

                    return;
                }
                //地址
                String newPwd = et_input_new_pwd.getText().toString().trim();
                String newCheckResult = CheckUtil.checkPwd(oldPwd);
                if (!newCheckResult.equals("Correct")) {
                    ToastUtil.showToastShort(ModifiPwdActivity.this, newCheckResult);

                    return;
                }
                if (!et_input_new_pwd_again.getText().toString().trim().equals(newPwd)) {

                    ToastUtil.showToastLong(ModifiPwdActivity.this, R.string.modifi_dif_input);
                    return;
                }
                HealthNetAPI.modifiPassword(mobileNo, oldPwd, newPwd, new Request.RequestListener<JSONObject>() {
                    @Override
                    public void onComplete(int stateCode, JSONObject result, String msg) {
                        try {
                            Log.i(TAG, "stateCode-------->" + stateCode);
                            Log.i(TAG, "result-------->" + result);
                            if (result.getInt("status") == 1) {
                                //jump
                            }
                            ToastUtil.showToastLong(ModifiPwdActivity.this, StringUtil.ENChangeToCH(result.getString("msg")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }


}

