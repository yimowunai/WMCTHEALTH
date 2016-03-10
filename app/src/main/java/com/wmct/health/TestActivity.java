package com.wmct.health;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmct.activity.AboutUsActivity;
import com.wmct.activity.PersonCenter;
import com.wmct.netApi.HealthNetAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class TestActivity extends ActionBarActivity {

    private static final String TAG = "TestActivity";
    private HealthApp healthApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        healthApp = (HealthApp) getApplication();
        setContentView(R.layout.test_layout);
        final TextView textView = (TextView) findViewById(R.id.text);
        final ImageView imageView = (ImageView) findViewById(R.id.iv);
       /* final ImageLoader imageLoader = ImageLoader.build(this);
        Family family = new Family();
        family.setName("wrg");
        family.setPhone("15006401730");
        family.setPwd("qwertyuio");
        family.setAdress("山东济南");
        family.setTerminalid("1231231231231");
        HealthNetAPI.login(family, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {

                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
                if (JsonToBean.isOK(result)) {
                    List<Member> list = JsonToBean.toMemberList(result);

                    for (int i = 0; i < list.size(); i++) {
                        Log.d(TAG, "list.get(" + i + ")=" + list.get(i).toString());
                        if (list.get(i).getImage() != null && !"".equals(list.get(i).getImage())) {
                            imageLoader.bindBitmap(list.get(i).getImage(), imageView);
                        }
                    }

                }

            }
        });*/

        //imageLoader.bindBitmap();



       /*
       //测试发送验证码     成功
        String phone = "15006401730";
       String authcode = "123123";
        HealthNetAPI.msgRegistVerification(phone, authcode, new Request.RequestListener<JSONObject>() {

            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
              String string ="";
                try {
                    string = result.getString("msg");
                    Toast.makeText(TestActivity.this,string,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });*/

  /*
       //测试能否注册  成功
       Family family = new Family();
        family.setName("wrg");
        family.setPhone("15006401730");
        family.setPwd("19911113@w");
        family.setAdress("山东济南");
        family.setTerminalid("1231231231231");
        HealthNetAPI.regist(family, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

        //测试登陆   成功
      /*Family family = new Family();
        family.setName("wrg");
        family.setPhone("15006401730");
        family.setPwd("qwertyuio");
        family.setAddress("山东济南");
        family.setTerminalid("1231231231231");
        HealthNetAPI.login(family, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
      /* //测试新建成员   当gender为0时出现错误
       Member member = new Member();
        member.setMembername("wrg03");
        member.setAge(24);
        member.setGender(0);


        HealthNetAPI.addMember("15006401730", member, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

     /*
     //测试删除成员   成功
      HealthNetAPI.deleteMember(6, "15006401730", new Request.RequestListener<JSONObject>() {
           @Override
           public void onComplete(int stateCode, JSONObject result, String msg) {
               Log.d(TAG,"stateCode=" + stateCode + "\n"+"msg=" + msg + "\n"+"result=" + result.toString());
               textView.append("stateCode=" + stateCode + "\n");
               textView.append("msg=" + msg + "\n");
               textView.append("result=" + result.toString());
           }
       });*/
        //测试成员新增测量数据功能   成功
       /* MeasureData measureData = new MeasureData();
        measureData.setDiastolicpressure(123);
        measureData.setSystolicpressure(70);
        measureData.setHeartrate(70);
        measureData.setHeartstate(0);
        HealthNetAPI.addData(7, measureData, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG,"stateCode=" + stateCode + "\n"+"msg=" + msg + "\n"+"result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
        //上传测量的游客数据结果  成功
       /* MeasureData measureData = new MeasureData();
        measureData.setDiastolicpressure(123);
        measureData.setSystolicpressure(70);
        measureData.setHeartrate(70);
        measureData.setHeartstate(0);
        ;
        measureData.setMeasuretime((int)(new Date().getTime()/1000));
        HealthNetAPI.addVisitData("123123123123", measureData, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG,"stateCode=" + stateCode + "\n"+"msg=" + msg + "\n"+"result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
        //测试查询某成员最近的测量数据（默认为10条） 成功
        /*HealthNetAPI.queryData(7, 2, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
        //测试 查询某成员最后一条测量数据  成功
       /* HealthNetAPI.lasteData(7, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

        //测试用户获取member信息功能  成功
        /*HealthNetAPI.memberInfo(7, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

        //测试用户修改member信息功能
       /* Member member = new Member();
        member.setMemberid(7);
        member.setMembername("wrg04");
        member.setAge(24);
        member.setGender(0);
        HealthNetAPI.modifyMemberInfo(member, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
        //测试重置密码功能    成功
        /*HealthNetAPI.resetPassword("15006401730", "qwertyuiop", new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
        //测试重置密码前的验证功能    成功
       /* HealthNetAPI.msgResetVerification("15006401730", "123211", new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/
        //测试修改密码   成功
        /*HealthNetAPI.modifiPassword("15006401730", "qwertyuio", "19911113@w", new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

        //测试批量上传登陆后未上传的数据  成功。
       /* List<MeasureData> list = new ArrayList<MeasureData>();
        for (int i = 0; i < 3; i++) {
            MeasureData measureData = new MeasureData();
            measureData.setMember(7);
            measureData.setDiastolicpressure(124);
            measureData.setSystolicpressure(70);
            measureData.setHeartrate(70);
            measureData.setHeartstate(0);
            measureData.setMeasuretime((int) (System.currentTimeMillis() / 1000));
            list.add(measureData);
        }
        for (int i = 0; i < 5; i++) {
            MeasureData measureData = new MeasureData();
            measureData.setMember(8);
            measureData.setDiastolicpressure(124);
            measureData.setSystolicpressure(70);
            measureData.setHeartrate(70);
            measureData.setHeartstate(0);
            measureData.setMeasuretime((int) (System.currentTimeMillis() / 1000));
            list.add(measureData);
        }
        HealthNetAPI.batchAddData(list, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

        //测试批量上离线的未上传的数据  成功。
       /* List<MeasureData> list = new ArrayList<MeasureData>();
        for (int i = 0; i < 3; i++) {
            MeasureData measureData = new MeasureData();
            measureData.setTerminalid(healthApp.getTerminalId());
            measureData.setDiastolicpressure(124);
            measureData.setSystolicpressure(70);
            measureData.setHeartrate(70);
            measureData.setHeartstate(0);
            measureData.setMeasuretime((int) (System.currentTimeMillis() / 1000));
            list.add(measureData);
        }
        for (int i = 0; i < 5; i++) {
            MeasureData measureData = new MeasureData();
            measureData.setTerminalid(healthApp.getTerminalId() + "gbb");
            measureData.setDiastolicpressure(124);
            measureData.setSystolicpressure(70);
            measureData.setHeartrate(70);
            measureData.setHeartstate(0);
            measureData.setMeasuretime((int) (System.currentTimeMillis() / 1000));
            list.add(measureData);
        }
        HealthNetAPI.batchAddVisitorData(list, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                Log.d(TAG, "stateCode=" + stateCode + "\n" + "msg=" + msg + "\n" + "result=" + result.toString());
                textView.append("stateCode=" + stateCode + "\n");
                textView.append("msg=" + msg + "\n");
                textView.append("result=" + result.toString());
            }
        });*/

        final File file = new File("./data/back.png");
        if (file.exists()) {
            textView.setText("filename=" + file.getName());
            new Thread() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = new FileInputStream(file);
                        HealthNetAPI.imageUpload(inputStream, file.getName(), 8);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
        /*Log.d(TAG, "path=" + file.getAbsolutePath());
        HealthNetAPI.fileUpload(file.getAbsolutePath(), 8,new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                textView.append(stateCode+"");
                textView.append(msg);
                textView.append(result.toString());
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.action_personal_center:
                intent.setClass(TestActivity.this, PersonCenter.class);
                startActivity(intent);
                break;
            case R.id.action_about_us:
                intent.setClass(TestActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_quit:
                healthApp.cleanLoginInfo();
                finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
