package com.wmct.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.InputStream;


import android.os.AsyncTask;
import android.widget.TextView;

import com.wmct.adapter.HeadIconAdapter;
import com.wmct.bean.Member;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;
import com.wmct.util.ToastUtil;

import org.json.JSONObject;

/**
 * Created by yimowunai on 2016/1/19.
 */
public class ChoiceHeadIconActivity extends Activity {
    private final static String TAG = "ChoiceHeadIconActivity";
    GridView gd_choice_head;
    TextView tx_skip;
    Integer imgRes[] = new Integer[]{R.raw.head_1, R.raw.head_2, R.raw.head_3, R.raw.head_4, R.raw.head_5, R.raw.head_6, R.raw.head_7, R.raw.head_8, R.raw.head_9};
    HealthApp healthApp;
    Long memberid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_head);
        healthApp = (HealthApp) getApplication();
        Intent intent = getIntent();
        memberid = intent.getExtras().getLong("memberid");
        initView();
    }

    private void initView() {
        gd_choice_head = (GridView) findViewById(R.id.gd_head_choice);
        tx_skip = (TextView) findViewById(R.id.tx_skip);
        tx_skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });
        gd_choice_head.setAdapter(new HeadIconAdapter(ChoiceHeadIconActivity.this, imgRes));
        gd_choice_head.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!healthApp.isNetworkConnected(ChoiceHeadIconActivity.this)) {
                    ToastUtil.showToastLong(ChoiceHeadIconActivity.this, "当前无网络连接，请检查网络，或选择跳过");

                } else {
                    new UpLoadIconTask().execute(position);
                }

            }
        });
    }


    class UpLoadIconTask extends AsyncTask<Integer, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            Resources resources = getApplication().getResources();
            InputStream inputStream = resources.openRawResource(imgRes[params[0]]);
            String iconName = resources.getResourceEntryName(imgRes[params[0]]) + ".png";
            Log.i(TAG, "iconName--------->" + iconName + "  memberid------------>" + memberid);
            return HealthNetAPI.imageUpload(inputStream, iconName, memberid);
        }

        protected void onPostExecute(JSONObject result) {
            if (JsonToBean.isOK(result)) {
                Member member = healthApp.getDataBaseAPI().queryMember(memberid);
                String url = JsonToBean.getUrl(result);
                Log.d(TAG, url);
                if (member != null) {
                    member.setImage(url);
                    healthApp.getDataBaseAPI().updateMember(member);
                }
                finish();
            }
            ToastUtil.showToastShort(ChoiceHeadIconActivity.this, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));
            super.onPostExecute(result);
        }
    }

}
