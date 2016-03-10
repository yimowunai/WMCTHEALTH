package com.wmct.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wmct.adapter.CityListAdapter;
import com.wmct.bean.CityListItem;
import com.wmct.bean.Family;
import com.wmct.bean.Member;
import com.wmct.db.CityDBManager;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yimowunai on 2016/1/4.
 */
public class UserInfoRegisterActivity extends Activity {

    public final static String TAG = "UserInfoRegActivity";
    EditText et_input_username;
    EditText et_input_pwd;
    private CityDBManager dbm;
    private SQLiteDatabase db;
    private Spinner spn_province = null;
    private Spinner spn_city = null;
    private Spinner spn_district = null;
    private String province = null;
    private String city = null;
    private String district = null;
    private String addr = null;
    private HealthApp healthApp;
    Button bt_login;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        healthApp = (HealthApp) getApplication();
        initView();
        initSpProvince();

    }


    private void initView() {
        et_input_username = (EditText) findViewById(R.id.et_input_username);
        et_input_pwd = (EditText) findViewById(R.id.et_input_pwd);

        spn_province = (Spinner) findViewById(R.id.spn_province);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        spn_district = (Spinner) findViewById(R.id.spn_district);
        spn_province.setPrompt("请选择省");
        spn_city.setPrompt("选择城市");
        spn_district.setPrompt("选择地区");

        spn_province.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
        spn_city.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
        spn_district.setOnItemSelectedListener(new SpinnerOnSelectedListener3());

        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            submitUserInfo();
                                        }
                                    }
        );

    }

    private void submitUserInfo() {//用户名
        String name = et_input_username.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToastShort(this, R.string.input_username);

            return;
        }
        //密码
        String pwd = et_input_pwd.getText().toString().trim();
        String checkResult = CheckUtil.checkPwd(pwd);
        if (!checkResult.equals("Correct")) {
            ToastUtil.showToastShort(this, checkResult);

            return;
        }
        //地址
        if (TextUtils.isEmpty(addr)) {
            ToastUtil.showToastShort(this, R.string.input_addr);

            return;
        }
        //手机号
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");

        Log.i(TAG, "phone------------->" + phone);
        //终端号

        final Family family = new Family();
        family.setName(name);
        family.setPwd(pwd);
        family.setAddress(addr);
        family.setPhone(phone);
        family.setTerminalid(healthApp.getTerminalId());
        Log.i(TAG, "family--------->" + family);
        HealthNetAPI.regist(family, new Request.RequestListener<JSONObject>() {
            public void onComplete(int stateCode, JSONObject result, String msg) {

                Log.i(TAG, "UserInfoReg_result----------->" + result);

                if (JsonToBean.isOK(result)) {
                    Intent intent = new Intent();
                    intent.setClass(UserInfoRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                ToastUtil.showToastLong(UserInfoRegisterActivity.this, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));


            }

        });
    }

    public void initSpProvince() {
        dbm = new CityDBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<CityListItem> list = new ArrayList<CityListItem>();

        try {
            String sql = "select * from province";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name);
                cityListItem.setPcode(code);
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            CityListItem cityListItem = new CityListItem();
            cityListItem.setName(name);
            cityListItem.setPcode(code);
            list.add(cityListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        CityListAdapter cityListAdapter = new CityListAdapter(this, list);
        spn_province.setAdapter(cityListAdapter);

    }

    public void initSpCity(String pcode) {
        dbm = new CityDBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<CityListItem> list = new ArrayList<CityListItem>();

        try {
            String sql = "select * from city where pcode='" + pcode + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name);
                cityListItem.setPcode(code);
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            CityListItem cityListItem = new CityListItem();
            cityListItem.setName(name);
            cityListItem.setPcode(code);
            list.add(cityListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        CityListAdapter cityListAdapter = new CityListAdapter(this, list);
        spn_city.setAdapter(cityListAdapter);

    }

    public void initSpDistrict(String pcode) {
        dbm = new CityDBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<CityListItem> list = new ArrayList<CityListItem>();

        try {
            String sql = "select * from district where pcode='" + pcode + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name);
                cityListItem.setPcode(code);
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            CityListItem cityListItem = new CityListItem();
            cityListItem.setName(name);
            cityListItem.setPcode(code);
            list.add(cityListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        CityListAdapter cityListAdapter = new CityListAdapter(this, list);
        spn_district.setAdapter(cityListAdapter);

    }

    class SpinnerOnSelectedListener1 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            province = ((CityListItem) adapterView.getItemAtPosition(position)).getName().trim();
            String pcode = ((CityListItem) adapterView.getItemAtPosition(position)).getPcode();

            initSpCity(pcode);
            initSpDistrict(pcode);


        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }

    class SpinnerOnSelectedListener2 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            city = ((CityListItem) adapterView.getItemAtPosition(position)).getName().trim();
            String pcode = ((CityListItem) adapterView.getItemAtPosition(position)).getPcode();
            initSpDistrict(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }

    class SpinnerOnSelectedListener3 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            district = ((CityListItem) adapterView.getItemAtPosition(position)).getName().trim();

            addr = province + city + district;
            Log.i(TAG, "addr------------>" + addr);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            // TODO Auto-generated method stub
        }
    }
}
