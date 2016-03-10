package com.wmct.health;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.wmct.bean.Family;
import com.wmct.bean.Member;

import android_serialport_api.SerialPort;

import com.wmct.db.DataBaseAPI;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;
import com.wmct.util.TerminalIdMaker;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/11 9:28
 * <p/>
 * --------------------------------------
 */
public class HealthApp extends Application {
    private static final String TAG = "HealthApp";
    private Family family;
    private boolean isLogin = false;
    private List<Member> members;
    private DataBaseAPI dataBaseAPI;

    public void setDataBaseAPI(DataBaseAPI dataBaseAPI) {
        this.dataBaseAPI = dataBaseAPI;
    }

    public DataBaseAPI getDataBaseAPI() {

        return dataBaseAPI;
    }

    private SerialPort mSerialPort;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "application create");
        dataBaseAPI = new DataBaseAPI(this);
        initLogin();
        initAd();
    }

    private void initAd() {
        HealthNetAPI.downAdvertisement();
    }


    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Open the serial port */
            File file0 = new File("/dev/ttyUSB0");
            File file1 = new File("/dev/ttyUSB1");
            if (file0.exists()) {
                mSerialPort = new SerialPort(file0, 9600, 0);
            } else if (file1.exists()) {
                mSerialPort = new SerialPort(file1, 9600, 0);
            }
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    private void initLogin() {
        family = getLoginFamily();
        if (null != family && !StringUtil.isEmpty(family.getPhone()) && !StringUtil.isEmpty(family.getPwd())) {
            if (isNetworkConnected(this)) {
                Log.d(TAG, "initLogin");
                HealthNetAPI.login(family, new Request.RequestListener<JSONObject>() {
                    @Override
                    public void onComplete(int stateCode, JSONObject result, String msg) {
                        if (JsonToBean.isOK(result)) {
                            isLogin = true;
                        }
                    }
                });
            }
        }
    }

    @SuppressLint("NewApi")
    public boolean isFristLogin(String phone) {
        SharedPreferences sp = getSharedPreferences("LoginRecord", Context.MODE_PRIVATE);
        //重要：在往sharespreferences里放set集合时，一定要新建一个set集合
        // 详情见http://www.lugeek.com/2014/05/15/android-sharedpreference-StringSet/
        Set<String> families = new HashSet<String>(sp.getStringSet("families", new HashSet<String>()));

        if (families.size() > 0 && families != null) {
            for (String family : families) {
                Log.d(TAG, "old family record =" + family);
            }
        }
        if (families.contains(phone)) {
            return false;
        } else {
            families.add(phone);
            SharedPreferences.Editor editor = sp.edit();
            editor.putStringSet("families", families);
            if (families.size() > 0 && families != null) {
                for (String family : families) {
                    Log.d(TAG, "new family record =" + family);
                }
            }
            editor.apply();
            Log.d(TAG, "apply completed");
        }
        return true;
    }

    public void saveFamilyInfo(Family family) {
        this.isLogin = true;
        this.family = family;
        setProperty("phone", family.getPhone());
        setProperty("name", family.getName());
        setProperty("address", family.getAddress());
        setProperty("password", family.getPwd());
    }

    public void cleanLoginInfo() {
        this.isLogin = false;
        removeProperty("password", "name", "address");
    }

    private void removeProperty(String... keys) {
        AppConfig.getAppConfig(this).remove(keys);
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void logOut() {
        cleanLoginInfo();
        this.isLogin = false;
    }

    private Family getLoginFamily() {
        Family family = new Family();
        family.setName(getProperty("name"));
        family.setPhone(getProperty("phone"));
        family.setPwd(getProperty("password"));
        family.setTerminalid(getTerminalId());
        family.setAddress(getProperty("address"));
        Log.d(TAG, family.toString());
        return family;
    }

    public Family getFamily() {
        return family;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String getTerminalId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtil.isEmpty(uniqueID)) {
            uniqueID = TerminalIdMaker.getTerminalid(this);
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    private String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }


}
