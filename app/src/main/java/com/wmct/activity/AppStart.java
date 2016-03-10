package com.wmct.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.wmct.bean.Member;
import com.wmct.health.*;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.service.UploadDataService;
import com.wmct.util.FileUtils;
import com.wmct.util.ImageResizer;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/11 15:13
 * <p/>
 * --------------------------------------
 */
public class AppStart extends Activity {
    private static final String TAG = "AppStart";
    private HealthApp healthApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        healthApp = (HealthApp) getApplication();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float a = getResources().getDisplayMetrics().density;
        float b = getResources().getDisplayMetrics().densityDpi;
        float c = getResources().getDisplayMetrics().scaledDensity;

        int width = dm.widthPixels;//宽度height = dm.heightPixels
        int height = dm.heightPixels;

        Log.d(TAG, "scaledDensity=" + c + "densityDpi=" + b + "density = " + a + "width = " + width + ", height = " + height);
        View view = View.inflate(this, R.layout.activity_start, null);
        ImageView welcome = (ImageView) view.findViewById(R.id.iv);

        check(welcome, width, height);
        setContentView(view);
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if (healthApp.isNetworkConnected(AppStart.this)) {
                    Log.d(TAG, "start UploadDataService");
                    Intent intent = new Intent(AppStart.this, UploadDataService.class);
                    startService(intent);
                }


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void redirectTo() {
        if (healthApp.isLogin()) {
            HealthNetAPI.findAllMembers(healthApp.getFamily(), new Request.RequestListener<JSONObject>() {
                @Override
                public void onComplete(int stateCode, JSONObject result, String msg) {
                    if (JsonToBean.isOK(result)) {
                        List<Member> members = JsonToBean.toMemberList(result);
                        healthApp.setMembers(members);
                        Intent intent = new Intent(AppStart.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(AppStart.this, VisitorMeasureActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        } else {
            Intent intent = new Intent(AppStart.this, VisitorMeasureActivity.class);
            startActivity(intent);
            finish();
        }

    }

    class a implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    @SuppressLint("NewApi")
    private void check(ImageView view, int width, int height) {
        String path = FileUtils.getAppCache(this, "welcome");
        List<File> files = FileUtils.listPathFiles(path);
        if (!files.isEmpty()) {
            File f = files.get(0);
            Log.d(TAG, f.getName());
            long[] time = getTime(f.getName());
            long today = StringUtil.getToday();
            if (time[0] <= today && time[1] >= today) {
                ImageResizer imageResizer = new ImageResizer();
                Bitmap bitmap = imageResizer.decodeSampledBitmapFromFile(f, width, height);
                view.setImageBitmap(bitmap);
                Log.d(TAG, " view.setBackground");
            }
        }
    }

    private long[] getTime(String name) {
        long[] res = new long[2];
        String time = name.substring(0, name.lastIndexOf("."));
        String[] t = time.split("-");
        res[0] = Long.parseLong(t[0]);
        if (t.length >= 2) {
            res[1] = Long.parseLong(t[1]);
        } else {
            res[1] = res[0];
        }
        return res;
    }

}
