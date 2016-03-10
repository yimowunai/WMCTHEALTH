package com.wmct.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wmct.bean.Family;
import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;
import com.wmct.health.HealthApp;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.JsonToBean;

import org.json.JSONObject;

import java.util.List;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/13 9:13
 * <p/>
 * --------------------------------------
 */
public class DownDataService extends Service {
    private static final String TAG = "DownDataService";
    private static final int DEFAULT_QUERY_DATA_SIZE = 10;
    private HealthApp healthApp;

    @Override
    public void onCreate() {
        healthApp = (HealthApp) getApplication();
        Log.d(TAG, "DownDataService started");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Family family = healthApp.getFamily();
        Log.d(TAG, "family Phone=" + family.getPhone());
        if (healthApp.isFristLogin(family.getPhone())) {
            List<Member> members = healthApp.getMembers();
            Log.d(TAG, "members size=" + members.size());
            //向数据库写入家庭的数据
            if (members != null && members.size() > 0) {

                for (int i = 0; i < members.size(); i++) {
                    Member member = members.get(i);
                    //向数据库写入成员的数据
                    healthApp.getDataBaseAPI().insertMember(member);
                    Log.d(TAG, member.toString());

                    HealthNetAPI.queryData(member.getMemberid(), DEFAULT_QUERY_DATA_SIZE, new Request.RequestListener<JSONObject>() {
                        @Override
                        public void onComplete(int stateCode, JSONObject result, String msg) {
                            //查询成功后，将测量数据写入数据库中。
                            Log.d(TAG, result.toString());
                            if (JsonToBean.isOK(result)) {
                                List<MeasureData> list = JsonToBean.toMeasureDataList(result);
                                Log.d(TAG, "member " + "=" + result.toString());
                                healthApp.getDataBaseAPI().batchInsertMesureData(list);
                                Log.d(TAG, "测量数据写入本地数据库");
                            }

                        }
                    });
                }
            }
        }

        stopSelf();
        Log.d(TAG, "DownDataService stop");
        return super.onStartCommand(intent, flags, startId);
    }
}
