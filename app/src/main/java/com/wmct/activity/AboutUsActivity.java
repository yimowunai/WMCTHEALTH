package com.wmct.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.wmct.fragment.AppInfoFragment;
import com.wmct.fragment.ContactUsFragment;
import com.wmct.fragment.RainbowInfoFragment;
import com.wmct.health.R;
import com.wmct.ui.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AboutUsActivity extends FragmentActivity {
    private final static String TAG = "AboutUsActivity";
    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<String> mDatas = Arrays.asList("关于健康管理", "中国虹计划", "联系我们");
    // private List<String> mDatas = Arrays.asList("短信1", "短信2", "短信3", "短信4");
    private ViewPagerIndicator mIndicator;
    private int mWidth;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aboutus);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("关于我们");
        mToolBar.setNavigationIcon(R.drawable.back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initDatas();
        //监听高度

        //设置Tab上的标题
        // mIndicator.setTabItemTitles(mDatas);
        mIndicator.setVisibleTabCount(3);
        mViewPager.setAdapter(mAdapter);
        //设置关联的ViewPager
        mIndicator.setViewPager(mViewPager, 0);

    }

    private void initDatas() {


        AppInfoFragment appInfoFragment = new AppInfoFragment();
        RainbowInfoFragment rainbowInfoFragment = new RainbowInfoFragment();
        ContactUsFragment contactUsFragment = new ContactUsFragment();
        mTabContents.add(appInfoFragment);
        mTabContents.add(rainbowInfoFragment);
        mTabContents.add(contactUsFragment);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            public int getCount() {
                return mTabContents.size();
            }

            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_vp);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
        mIndicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                mWidth = mIndicator.getWidth();
                if (mWidth != 0) {
                    mIndicator.setTabItemTitles(mDatas);
                }
                mIndicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.i(TAG, "main mWidth------------->" + mWidth);
            }
        });
    }


}
