package com.wmct.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wmct.fragment.FamilyFragment;
import com.wmct.fragment.MemberFragment;
import com.wmct.fragment.ModifyPasswordFragment;
import com.wmct.health.HealthApp;
import com.wmct.health.R;


public class PersonCenter extends FragmentActivity implements View.OnTouchListener {
    Toolbar mToolBar;
    private final static String TAG = "PersonCenter";
    private HealthApp healthApp;
    private TextView familyManager;
    private TextView modifyPassword;
    private TextView memberManager;
    public MemberFragment memberFragment;
    private FamilyFragment familyFragment;
    private ModifyPasswordFragment modifyPasswordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);  //设置全屏
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_person_center);
        healthApp = (HealthApp) getApplication();
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("个人中心");
        mToolBar.setNavigationIcon(R.drawable.back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //mToolBar.setLogo(R.drawable.ic_launcher);
        //mToolBar.setOnMenuItemClickListener(onMenuItemClickListener);
        //mToolBar.inflateMenu(R.menu.menu_main); //显示菜单  但是不能显示图标
        //mToolBar.canShowOverflowMenu();  //目前没有效果

        initView();
        initDatas();
        // mToolBar.
        //setSupportActionBar(mToolBar);

        familyManager.setPressed(true);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.family_or_member_fragment, familyFragment);
        transaction.commit();
        familyManager.setOnTouchListener(this);
        modifyPassword.setOnTouchListener(this);
        memberManager.setOnTouchListener(this);
    }


    private void initView() {
        familyManager = (TextView) findViewById(R.id.family_manager);
        modifyPassword = (TextView) findViewById(R.id.modify_pwd);
        memberManager = (TextView) findViewById(R.id.member_manager);
        memberFragment = new MemberFragment();
        familyFragment = new FamilyFragment();
        modifyPasswordFragment = new ModifyPasswordFragment();
    }

    private void initDatas() {


    }

   /* private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
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
                Toast.makeText(TestActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };*/


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {

            case R.id.family_manager:
                resetManagerState();
                familyManager.setPressed(true);
                transaction.replace(R.id.family_or_member_fragment, familyFragment);
                transaction.commit();
                //Toast.makeText(TestActivity.this, "family_manager TRUE", Toast.LENGTH_LONG).show();
                return true;

            case R.id.modify_pwd:
                resetManagerState();
                modifyPassword.setPressed(true);
                transaction.replace(R.id.family_or_member_fragment, modifyPasswordFragment);
                transaction.commit();
                //Toast.makeText(TestActivity.this, "modify_pwd TRUE", Toast.LENGTH_LONG).show();
                return true;

            case R.id.member_manager:
                resetManagerState();
                memberManager.setPressed(true);
                transaction.replace(R.id.family_or_member_fragment, memberFragment);
                transaction.commit();
                //Toast.makeText(TestActivity.this, "member_manager TRUE", Toast.LENGTH_LONG).show();
                return true;
            default:
                //Toast.makeText(TestActivity.this, "return false", Toast.LENGTH_LONG).show();
                return false;
        }

    }

    private void resetManagerState() {
        familyManager.setPressed(false);
        modifyPassword.setPressed(false);
        memberManager.setPressed(false);
    }
}
