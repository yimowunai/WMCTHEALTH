package com.wmct.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.wmct.bean.Family;
import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;
import com.wmct.db.DataBaseAPI;
import com.wmct.health.HealthApp;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.JsonToBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/11 20:20
 * <p/>
 * --------------------------------------
 */
public class UploadDataService extends Service {
    private static final String TAG = "UploadDataService";
    private static final int DEFAULT_QUERY_DATA_SIZE = 10;
    private HealthApp healthApp;

    @Override
    public void onCreate() {
        super.onCreate();
        healthApp = (HealthApp) getApplication();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "UploadDataService start");
        if (healthApp.isLogin()) {
           /* Family family = healthApp.getFamily();
            List<Member> members = healthApp.getMembers();
            if (healthApp.isFristLogin(family.getPhone())) {
                //向数据库写入家庭的数据

                if (members != null && members.size() > 0) {

                    for (int i = 0; i < members.size(); i++) {
                        Member member = members.get(i);
                        //向数据库写入成员的数据


                        HealthNetAPI.queryData(member.getMemberid(), DEFAULT_QUERY_DATA_SIZE, new Request.RequestListener<JSONObject>() {
                            @Override
                            public void onComplete(int stateCode, JSONObject result, String msg) {
                                //下载成功后，将测量数据写入数据库中。

                            }
                        });
                    }
                }
            } else {*/
            //查找登陆的未上传的数据  如果存在的话  就上传数据(一条一条的上传)
            List<MeasureData> list = healthApp.getDataBaseAPI().queryAllNot_uploaded();
            if (list != null && list.size() > 0) {
                HealthNetAPI.batchAddData(list, new Request.RequestListener<JSONObject>() {
                    @Override
                    public void onComplete(int stateCode, JSONObject result, String msg) {
                        if (JsonToBean.isOK(result)) {
                            healthApp.getDataBaseAPI().deleteNot_uploadedAll();
                            Log.d(TAG, "上传成员数据成功，并删除本地数据库");
                            stopSelf();
                            Log.d(TAG, "UploadDataService stop");
                        }
                    }
                });
                /*for (int i = 0; i < list.size(); i++) {
                    MeasureData measureData = list.get(i);
                    HealthNetAPI.addData(measureData.getMemberId(), measureData, new Request.RequestListener<JSONObject>() {
                        @Override
                        public void onComplete(int stateCode, JSONObject result, String msg) {
                            if (JsonToBean.isOK(result)) {
                            }
                        }
                    });
                }*/
            } else {
                stopSelf();
                Log.d(TAG, "UploadDataService stop");
            }

        } else {
            //查找离线的未上传的数据 如果存在的话  就上传数据（一条一条的上传）
            List<MeasureData> list = healthApp.getDataBaseAPI().queryAllVistordata();
            if (list != null && list.size() > 0) {
                HealthNetAPI.batchAddVisitorData(list, new Request.RequestListener<JSONObject>() {
                    @Override
                    public void onComplete(int stateCode, JSONObject result, String msg) {
                        if (JsonToBean.isOK(result)) {
                            healthApp.getDataBaseAPI().deleteVisitordataAll();
                            Log.d(TAG, "上传游客数据成功，并删除本地数据库");
                            stopSelf();
                            Log.d(TAG, "UploadDataService stop");
                        }
                    }
                });
              /*  for (int i = 0; i < list.size(); i++) {
                    MeasureData measureData = list.get(i);
                    HealthNetAPI.addVisitData(measureData, new Request.RequestListener<JSONObject>() {
                        @Override
                        public void onComplete(int stateCode, JSONObject result, String msg) {
                            if (JsonToBean.isOK(result)) {

                            }
                        }
                    });
                }*/
            } else {
                stopSelf();
                Log.d(TAG, "UploadDataService stop");
            }


        }
        return super.onStartCommand(intent, flags, startId);
    }
}
