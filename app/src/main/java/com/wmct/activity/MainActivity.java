package com.wmct.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wmct.adapter.MemberAdapter;
import com.wmct.bean.Family;
import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.ui.ChartView;
import com.wmct.ui.ChartViewHeart;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SerialPortActivity {
    private static final String TAG = "MainActivity";
    private HealthApp healthApp;
    private Family family;
    private List<Member> members;
    private Member checkedMember;
    private MemberAdapter memberAdapter;
    private ListView lv_member;
    private TextView tv_time, tv_high_data, tv_low_data, tv_heart_data;
    private TextView this_time_measure_display, last_time_measure_display;
    private ImageView img_result;
    private ChartView chart_xueya;
    private ChartViewHeart chart_heart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity onCreate");
        setContentView(R.layout.activity_main);
        healthApp = (HealthApp) getApplication();
        family = healthApp.getFamily();
        members = healthApp.getDataBaseAPI().queryAllMember(family);
        initActionBar();
        initView();
        if (members != null && members.size() > 0) {
            checkedMember = members.get(0);
            MeasureData initMeasureData = healthApp.getDataBaseAPI().querylastMeasureData(checkedMember);
            List<MeasureData> initMeasureDatas = healthApp.getDataBaseAPI().query10MeasureData(checkedMember);
            if (initMeasureData != null) {
                setRightTopView(initMeasureData);
            }
            if (initMeasureDatas != null) {
                setRightBottomView(initMeasureDatas);
            }

        } else {
            members = new ArrayList<>();
        }
        memberAdapter = new MemberAdapter(this, members);
        setlistview();

        //lv_member.setSelection(0);
        if (members != null && members.size() > 0) {
            int position = lv_member.getFirstVisiblePosition();
            Log.d(TAG, "position = " + position);
            lv_member.requestFocus();
            lv_member.setSelection(position);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity onResume");
        members = healthApp.getDataBaseAPI().queryAllMember(family);
        if (members != null && members.size() > 0) {
            memberAdapter = new MemberAdapter(this, members);
            lv_member.setAdapter(memberAdapter);
            Log.d(TAG, members.toString());
            memberAdapter.notifyDataSetInvalidated();
        }

    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo6);
        toolbar.setTitle(family.getName());
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            Intent intent = new Intent();

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_personal_center:
                        intent.setClass(MainActivity.this, PersonCenter.class);
                        startActivity(intent);
                        break;
                    case R.id.action_about_us:
                        intent.setClass(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_quit:
                        healthApp.logOut();
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        members = healthApp.getDataBaseAPI().queryAllMember(family);
        if (members != null && members.size() > 0) {
            Log.d(TAG, "mainActivity onRestart");
            memberAdapter = new MemberAdapter(this, members);
            Log.d(TAG, members.toString());
            lv_member.setAdapter(memberAdapter);
            memberAdapter.notifyDataSetInvalidated();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        lv_member = (ListView) findViewById(R.id.lv_members);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_high_data = (TextView) findViewById(R.id.tv_high_data);
        tv_low_data = (TextView) findViewById(R.id.tv_low_data);
        tv_heart_data = (TextView) findViewById(R.id.tv_heart_data);
        img_result = (ImageView) findViewById(R.id.img_result);
        this_time_measure_display = (TextView) findViewById(R.id.this_time_measure);
        last_time_measure_display = (TextView) findViewById(R.id.last_time_measure);

        chart_xueya = (ChartView) findViewById(R.id.chart_xueya);
        chart_heart = (ChartViewHeart) findViewById(R.id.chart_heart);
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_addmember, null);
        Button bt_addmember = (Button) footerView.findViewById(R.id.bt_add);
        bt_addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启添加成员的activity
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplication(), "点击了添加新成员", Toast.LENGTH_LONG).show();
            }
        });
       /* if (members != null && members.size() > 0) {
            lv_member.addFooterView(footerView);
        } else {
            lv_member.addHeaderView(footerView);
        }*/
        lv_member.addFooterView(footerView);
    }

    private void setlistview() {
        lv_member.setAdapter(memberAdapter);
        lv_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = members.get(position);
                checkedMember = member;
                MeasureData measureData = healthApp.getDataBaseAPI().querylastMeasureData(member);
                List<MeasureData> measureDatas = healthApp.getDataBaseAPI().query10MeasureData(member);
                if (measureData != null) {
                    this_time_measure_display.setVisibility(View.GONE);
                    last_time_measure_display.setVisibility(View.VISIBLE);
                    setRightTopView(measureData);
                } else {
                    measureData = new MeasureData();
                    setRightTopView(measureData);
                }
                if (measureDatas != null) {
                    setRightBottomView(measureDatas);
                } else {
                    measureDatas = new ArrayList<>();
                    setRightBottomView(measureDatas);
                }
                memberAdapter.setSelectIndex(position);
                memberAdapter.notifyDataSetChanged();
                Log.i(TAG, "点击的位置------listview------->" + position);
               /* if (position != 0) {
                    lv_member.getChildAt(0).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if (lv_member.getTag() != null) {
                    ((View) lv_member.getTag()).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                lv_member.setTag(view);
                view.setBackgroundColor(Color.parseColor("#e7e4e4"));*/
            }
        });
    }

    private void setRightTopView(MeasureData checkedmemberdata) {
        String highbp = "";
        String lowbp = "";
        String heartbp = "";
        String measuretime = "";
        if (checkedmemberdata.getHeartrate() != 0) {
            highbp = checkedmemberdata.getSystolicpressure() + "";
            lowbp = checkedmemberdata.getDiastolicpressure() + "";
            heartbp = checkedmemberdata.getHeartrate() + "";
            measuretime = checkedmemberdata.getMeasuretime() + "000";
        }
        tv_high_data.setText(highbp);
        tv_low_data.setText(lowbp);
        tv_heart_data.setText(heartbp);
        tv_time.setText(StringUtil.TimeTranstoString(measuretime));
        if (highbp != "") {
            if (Integer.valueOf(highbp) > 160 && Integer.valueOf(lowbp) > 95) {
                img_result.setImageResource(R.drawable.highbp);
                Log.i(TAG, "result------------------->" + highbp + "--" + lowbp + "--" + "高血压");
            } else if (Integer.valueOf(highbp) > 140 && Integer.valueOf(lowbp) > 90) {
                img_result.setImageResource(R.drawable.borderlinebp);
                Log.i(TAG, "result------------------->" + highbp + "--" + lowbp + "--" + "临界高血压");
            } else if (Integer.valueOf(highbp) > 120 && Integer.valueOf(lowbp) > 80) {
                img_result.setImageResource(R.drawable.normalbp);
                Log.i(TAG, "result------------------->" + highbp + "--" + lowbp + "--" + "正常血压");
            } else if (Integer.valueOf(highbp) > 90 && Integer.valueOf(lowbp) > 60) {
                img_result.setImageResource(R.drawable.idealbp);
                Log.i(TAG, "result------------------->" + highbp + "--" + lowbp + "--" + "理想血压");
            } else {
                img_result.setImageResource(R.drawable.lowbp);
                Log.i(TAG, "result------------------->" + highbp + "--" + lowbp + "--" + "低血压");
            }
        } else {
            img_result.setImageResource(R.drawable.beforemeasure);
        }
    }

    private void setRightBottomView(List<MeasureData> measureDatas) {
        String[] highdatas = get10HighData(measureDatas);
        String[] lowdatas = get10LowData(measureDatas);
        String[] heartdatas = get10HeartData(measureDatas);
        if (highdatas != null) {
            Log.i(TAG, "------------------------------------setchartview");
            Log.i(TAG, "------------------------------------setchartview" + heartdatas.length);
            chart_heart.SetInfo(heartdatas);
            chart_heart.HeartData_count = heartdatas.length;
            chart_xueya.SetInfo(highdatas, lowdatas);
            chart_xueya.XueYaData_count = heartdatas.length;

            chart_xueya.invalidate();
            chart_heart.invalidate();
        } else {
            chart_heart.HeartData_count = 0;
            chart_xueya.XueYaData_count = 0;
            chart_heart.invalidate();
            chart_xueya.invalidate();
        }
    }



   /* private String[] getLastMeasureData(Member member) {
        MeasureData lastmeasuredata = healthApp.getDataBaseAPI().querylastMeasureData(member);
        String[] lastdata;
        if (lastmeasuredata != null) {
            lastdata = new String[4];
            lastdata[0] = lastmeasuredata.getSystolicpressure() + "";
            lastdata[1] = lastmeasuredata.getDiastolicpressure() + "";
            lastdata[2] = lastmeasuredata.getHeartrate() + "";
            lastdata[3] = lastmeasuredata.getMeasuretime() + "000";
        } else {
            lastdata = new String[]{"", "", "", ""};
        }
        return lastdata;
    }*/

    private String[] get10HighData(List<MeasureData> measureDatas) {
        String[] highdatas = null;
        if (measureDatas.size() > 0) {
            highdatas = new String[measureDatas.size()];
            for (int i = 0; i < measureDatas.size(); i++) {
                highdatas[measureDatas.size() - 1 - i] = measureDatas.get(i).getSystolicpressure() + "";
            }
        }
        return highdatas;
    }

    private String[] get10LowData(List<MeasureData> measureDatas) {
        String[] lowdatas = null;
        if (measureDatas.size() > 0) {
            lowdatas = new String[measureDatas.size()];
            for (int i = 0; i < measureDatas.size(); i++) {
                lowdatas[measureDatas.size() - 1 - i] = measureDatas.get(i).getDiastolicpressure() + "";
            }
        }
        return lowdatas;

    }

    private String[] get10HeartData(List<MeasureData> measureDatas) {
        String[] heartdatas = null;
        if (measureDatas.size() > 0) {
            heartdatas = new String[measureDatas.size()];
            for (int i = 0; i < measureDatas.size(); i++) {
                heartdatas[measureDatas.size() - 1 - i] = measureDatas.get(i).getHeartrate() + "";
            }
        }
        return heartdatas;
    }

  /*  public Member getCheckedMember() {
        return checkedMember;
    }

    public void setCheckedMember(Member checkedMember) {
        this.checkedMember = checkedMember;
    }*/

    @Override
    protected void performMeasureData(int hightPresure, int lowPresure, int heartRate, int heartState) {
        final MeasureData measureData = new MeasureData();
        measureData.setTerminalid(healthApp.getTerminalId());
        measureData.setMemberId(checkedMember.getMemberid());
        measureData.setDiastolicpressure(lowPresure);
        measureData.setSystolicpressure(hightPresure);
        measureData.setHeartrate(heartRate);
        if (heartState == 85) {
            measureData.setHeartstate(0);
        } else {
            measureData.setHeartstate(1);
        }
        measureData.setMeasuretime(StringUtil.getCurrentTime());
        this_time_measure_display.setVisibility(View.VISIBLE);
        last_time_measure_display.setVisibility(View.GONE);
        setRightTopView(measureData);
        HealthNetAPI.addData(checkedMember.getMemberid(), measureData, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                if (JsonToBean.isOK(result)) {
                    Log.d(TAG, "上传数据成功");
                } else {
                    healthApp.getDataBaseAPI().insertNot_uploaded(measureData);
                }
                healthApp.getDataBaseAPI().insertMeasureData(measureData);
            }
        });
        Log.i(TAG, "hightPresure---------->" + hightPresure);
        Log.i(TAG, "lowPresure---------->" + lowPresure);
        Log.i(TAG, "heartRate---------->" + heartRate);
    }


}
