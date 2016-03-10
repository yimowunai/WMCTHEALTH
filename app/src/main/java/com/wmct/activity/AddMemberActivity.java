package com.wmct.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.wmct.bean.Member;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.CheckUtil;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;
import com.wmct.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by yimowunai on 2016/1/15.
 */
public class AddMemberActivity extends Activity implements View.OnClickListener {
    public final static String TAG = "AddMemberActivity";
    private HealthApp healthApp;
    public final static int CHECK_MAN = 0;
    public final static int CHECK_WOMAN = 1;
    public final static int CHECK_SECRET = 2;
    public final static int NO_CHECK = 3;
    TextView tx_exit;
    EditText et_member_name;
    EditText et_member_age;
    RadioGroup rd_container;
    RadioButton rb_man;
    RadioButton rb_woman;
    RadioButton rb_secret;
    Button bt_next_step;
    String memberName = null;
    String memberAge = null;
    int memberGender = NO_CHECK;

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addmember_layout);
        healthApp = (HealthApp) getApplication();
        initView();
        setFinishOnTouchOutside(false);
        super.onCreate(savedInstanceState);
    }

    private void initView() {

        et_member_age = (EditText) findViewById(R.id.et_member_age);
        et_member_name = (EditText) findViewById(R.id.et_member_name);
        rd_container = (RadioGroup) findViewById(R.id.rd_container);
        bt_next_step = (Button) findViewById(R.id.bt_next_step);
        tx_exit = (TextView) findViewById(R.id.tx_exit);
        tx_exit.setOnClickListener(this);
        bt_next_step.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next_step:
                if (healthApp.isNetworkConnected(AddMemberActivity.this)) {
                    doNextStep();
                }
                break;
            case R.id.tx_exit:
                finish();
                break;
        }
    }

    private void doNextStep() {
        final Member member = new Member();
        memberName = et_member_name.getText().toString().trim();
        if (StringUtil.isEmpty(memberName)) {
            ToastUtil.showToastShort(AddMemberActivity.this, R.string.input_member_name);
            return;
        }
        memberAge = et_member_age.getText().toString().trim();
        if (StringUtil.isEmpty(memberAge)) {

            ToastUtil.showToastShort(AddMemberActivity.this, R.string.input_member_age);
            return;
        } else if (!CheckUtil.checkMemberAge(memberAge)) {
            ToastUtil.showToastShort(AddMemberActivity.this, R.string.age_format_error);
            return;
        }
        memberGender = getCheckGender();
        Log.i(TAG, "memberGender-------------->" + memberGender);
        if (memberGender == NO_CHECK) {
            ToastUtil.showToastShort(AddMemberActivity.this, R.string.no_check_gender);
            return;
        }
        member.setMembername(memberName);
        member.setGender(memberGender);
        member.setAge(Integer.parseInt(memberAge));

        HealthNetAPI.addMember(healthApp.getFamily().getPhone(), member, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.i(TAG, "result---------->" + result);
                if (JsonToBean.isOK(result)) {
                    member.setFamilyPhone(healthApp.getFamily().getPhone());
                    try {
                        long id = result.getLong("id");
                        member.setMemberid(id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    healthApp.getDataBaseAPI().insertMember(member);
                    Intent intent = new Intent(AddMemberActivity.this, ChoiceHeadIconActivity.class);
                    intent.putExtra("memberid", member.getMemberid());
                    startActivity(intent);
                    finish();
                }
                ToastUtil.showToastShort(AddMemberActivity.this, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));

            }

        });

    }


    private int getCheckGender() {
        switch (rd_container.getCheckedRadioButtonId()) {
            case R.id.rb_man:
                return CHECK_MAN;
            case R.id.rb_woman:
                return CHECK_WOMAN;
            case R.id.rb_secret:
                return CHECK_SECRET;

        }

        return NO_CHECK;
    }
}


