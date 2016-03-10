package com.wmct.apptest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener, View.OnTouchListener {
    Toolbar mToolBar;
    private final static String TAG = "MainActivity";

    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private int memberSize = 4;
    private RadioGroup radioGroup;
    private Button edit;
    private Button submit;
    // private List<String> mDatas = Arrays.asList("关于健康管理", "中国虹计划", "联系我们");
    // private List<String> mDatas = Arrays.asList("短信1", "短信2", "短信3", "短信4");
    //private int mWidth;
    private TextView familyManager;
    private TextView modifyPassword;
    private TextView memberManager;
    private boolean familyIsPressed = false;
    private boolean memberIsPressed = false;
    private boolean modifyIsPressed = false;
    private boolean isModifing = false;
    private ArrayList<Fragment> mTabContents = new ArrayList<Fragment>(memberSize);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);  //设置全屏
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("个人中心");
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setOnMenuItemClickListener(onMenuItemClickListener);
        mToolBar.inflateMenu(R.menu.menu_main); //显示菜单  但是不能显示图标
        mToolBar.canShowOverflowMenu();  //目前没有效果

        initView();
        initDatas();
        // mToolBar.
        //setSupportActionBar(mToolBar);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isModifing) {
                    resetMemberInfo();
                    isModifing = false;
                    submit.setVisibility(View.INVISIBLE);
                }
                resetRadioButtonBackground();

                ImageView imageView = (ImageView) radioGroup.getChildAt(position);
                imageView.setBackground(getResources().getDrawable(R.drawable.radiobutton_choosed));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (memberSize > 0) {
            mViewPager.setCurrentItem(0);
            radioGroup.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.radiobutton_choosed));
        }


        edit.setOnClickListener(this);
        submit.setOnClickListener(this);
        familyManager.setOnTouchListener(this);
        modifyPassword.setOnTouchListener(this);
        memberManager.setOnTouchListener(this);
    }


    private void resetMemberInfo() {
        for (int i = 0; i < memberSize; i++) {
            Log.d(TAG, "i =" + i);
            MemberInfoFragment fragment = (MemberInfoFragment) mTabContents.get(i);
            if (fragment.isModifing()) {
                fragment.changeToDisplayMode();
                fragment.setIsModifing(false);
            }
        }
    }

    private void resetRadioButtonBackground() {
        for (int i = 0; i < memberSize; i++) {
            radioGroup.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.memberimage));
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.members);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        edit = (Button) findViewById(R.id.family_or_member_edit);
        submit = (Button) findViewById(R.id.family_or_member_submit);
        familyManager = (TextView) findViewById(R.id.family_manager);
        modifyPassword = (TextView) findViewById(R.id.modify_pwd);
        memberManager = (TextView) findViewById(R.id.member_manager);
    }

    private void initDatas() {
        for (int i = 0; i < memberSize; i++) {
            MemberInfoFragment fragment = new MemberInfoFragment();
            ImageView imageView = new ImageView(MainActivity.this);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(10, 10);
            layoutParams.setMargins(20, 0, 20, 0);
            imageView.setBackground(getResources().getDrawable(R.drawable.memberimage));
            imageView.setLayoutParams(layoutParams);
            radioGroup.addView(imageView);
            mTabContents.add(fragment);
        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }

            @Override
            public int getCount() {
                return mTabContents.size();
            }
        };


    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String msg = "";
            switch (item.getItemId()) {
                case R.id.action_edit:
                    msg += "Click edit";
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if (!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int position = mViewPager.getCurrentItem();
        MemberInfoFragment fragment = (MemberInfoFragment) mTabContents.get(position);
        switch (v.getId()) {
            case R.id.family_or_member_edit:
                Toast.makeText(MainActivity.this, "点击了编辑", Toast.LENGTH_LONG).show();
                isModifing = true;
                submit.setVisibility(View.VISIBLE);
                Log.d(TAG, "编辑的position =" + position);
                fragment.setIsModifing(true);
                fragment.changeToEditMode();
                break;
            case R.id.family_or_member_submit:
                fragment.saveMemberInfo();
                submit.setVisibility(View.INVISIBLE);
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {

            case R.id.family_manager:
                resetManagerState();
                familyManager.setPressed(true);
                //Toast.makeText(MainActivity.this, "family_manager TRUE", Toast.LENGTH_LONG).show();
                return true;

            case R.id.modify_pwd:
                resetManagerState();
                modifyPassword.setPressed(true);
                //Toast.makeText(MainActivity.this, "modify_pwd TRUE", Toast.LENGTH_LONG).show();
                return true;

            case R.id.member_manager:
                resetManagerState();
                memberManager.setPressed(true);
                //Toast.makeText(MainActivity.this, "member_manager TRUE", Toast.LENGTH_LONG).show();
                return true;
            default:
                //Toast.makeText(MainActivity.this, "return false", Toast.LENGTH_LONG).show();
                return false;
        }

    }

    private void resetManagerState() {
        familyManager.setPressed(false);
        modifyPassword.setPressed(false);
        memberManager.setPressed(false);
    }
}
