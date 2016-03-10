package com.wmct.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.wmct.bean.Family;
import com.wmct.bean.Member;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.net.Image.ImageLoader;
import com.wmct.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/18 20:54
 * <p/>
 * --------------------------------------
 */
public class MemberFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "MemberFragment";
    //private RadioGroup radioGroup;
    private LinearLayout membersImageList;
    private Button edit;
    public Button submit;
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private int memberSize;
    private Activity mActivity;
    private HealthApp healthApp;
    private Family family;
    private List<Member> members;
    private boolean isModifing = false;
    private List<Fragment> mTabContents = new ArrayList<>();
    ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_fragment_layout, null);
        initView(view);
        Log.d(TAG, "MemberFragment onCreateView");
        initData();
        initEvent();
        return view;
    }

    private void initEvent() {
        edit.setOnClickListener(this);
        submit.setOnClickListener(this);
        //radioGroup.setOnClickListener(this);
        membersImageList.setOnClickListener(this);
       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //resetRadioButtonBackground();
                //group.getChildAt(checkedId).setPressed(true);
                mViewPager.setCurrentItem(checkedId);
            }
        });*/

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
                Log.d(TAG, "position =" + position);
                resetRadioButtonBackground();
                //radioGroup.getChildAt(mViewPager.getCurrentItem()).setSelected(true);
                membersImageList.getChildAt(mViewPager.getCurrentItem()).setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (memberSize > 0) {
            mViewPager.setCurrentItem(0);
            resetRadioButtonBackground();
            membersImageList.getChildAt(0).setSelected(true);
            //radioGroup.getChildAt(0).setSelected(true);
            Log.d(TAG, "mViewPager.getCurrentItem()" + mViewPager.getCurrentItem());
            Log.d(TAG, "重新设置");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        healthApp = (HealthApp) mActivity.getApplication();
        family = healthApp.getFamily();
        members = healthApp.getDataBaseAPI().queryAllMember(family);
        if (members != null) {
            memberSize = members.size();
        } else {
            memberSize = 0;
        }
        imageLoader = ImageLoader.build(mActivity);
        Log.d(TAG, "MemberFragment onAttach");
    }

    private void initData() {
        mTabContents.clear();
        for (int i = 0; i < memberSize; i++) {
            MemberInfoFragment fragment = new MemberInfoFragment();
            Member member = members.get(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("member", member);
            fragment.setArguments(bundle);
            ImageView imageView = new ImageView(mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
            layoutParams.setMargins(0, 10, 0, 10);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setBackground(getResources().getDrawable(R.drawable.memberimage));
            imageView.setOnClickListener(new ImageViewClickListener());
            imageView.setLayoutParams(layoutParams);
            imageView.setId(i);
            if (StringUtil.isEmpty(member.getImage())) {
                imageView.setImageResource(R.drawable.ic_launcher);
            } else {
                imageLoader.bindBitmap(member.getImage(), imageView);
            }
            membersImageList.addView(imageView);
            //radioGroup.addView(imageView);
            mTabContents.add(fragment);
        }
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }

            @Override
            public int getCount() {
                return mTabContents.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

    }

    private class ImageViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            resetRadioButtonBackground();
            //radioGroup.getChildAt(id).setSelected(true);
            membersImageList.getChildAt(id).setSelected(true);
            mViewPager.setCurrentItem(id);
        }
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.members);
        //radioGroup = (RadioGroup) view.findViewById(R.id.members_image_list);
        membersImageList = (LinearLayout) view.findViewById(R.id.members_image_list);
        edit = (Button) view.findViewById(R.id.member_edit);
        submit = (Button) view.findViewById(R.id.member_submit);
        if (memberSize <= 0) {
            edit.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int position = mViewPager.getCurrentItem();
        MemberInfoFragment fragment = (MemberInfoFragment) mTabContents.get(position);
        switch (v.getId()) {
            case R.id.member_edit:
                //Toast.makeText(mActivity, "点击了编辑", Toast.LENGTH_LONG).show();
                isModifing = true;
                submit.setVisibility(View.VISIBLE);
                Log.d(TAG, "编辑的position =" + position);
                fragment.setIsModifing(true);
                fragment.changeToEditMode();
                break;
            case R.id.member_submit:
                fragment.saveMemberInfo();

                break;

        }
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
            //radioGroup.getChildAt(i).setSelected(false);
            membersImageList.getChildAt(i).setSelected(false);
        }
    }
}
