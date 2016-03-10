package com.wmct.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wmct.activity.PersonCenter;
import com.wmct.bean.Member;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.net.Image.ImageLoader;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.util.GenderConversion;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;
import com.wmct.util.ToastUtil;

import org.json.JSONObject;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/15 17:35
 * <p/>
 * --------------------------------------
 */
public class MemberInfoFragment extends Fragment {
    private static final String TAG = "MemberInfoFragment";

    public final static int CHECK_MAN = 0;
    public final static int CHECK_WOMAN = 1;
    public final static int CHECK_SECRET = 2;
    public final static int NO_CHECK = 3;
    private PersonCenter mActivity;
    private EditText memberNameEdit;
    private RadioGroup memberGenderEdit;
    private EditText memberAgeEdit;
    private TextView memberNameText;
    private TextView memberGenderText;
    private TextView memberAgeText;
    private TextView memberPhoneText;
    private TextView memberIdText;
    private ImageView memberImage;
    ImageLoader imageLoader;
    private Member member;
    private boolean isModifing = false;
    private HealthApp healthApp;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_info, null);
        initView(view);
        Log.d(TAG, "MemberInfoFragment onCreateView");
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (PersonCenter) activity;
        Bundle bundle = getArguments();
        member = (Member) bundle.getSerializable("member");
        healthApp = (HealthApp) mActivity.getApplication();
        imageLoader = ImageLoader.build(mActivity);
        Log.d(TAG, member.toString());
    }

    private void initView(View view) {
        memberImage = (ImageView) view.findViewById(R.id.memberImage);
        memberNameEdit = (EditText) view.findViewById(R.id.memberName_edit);
        memberAgeEdit = (EditText) view.findViewById(R.id.memberAge_edit);
        memberGenderEdit = (RadioGroup) view.findViewById(R.id.memberGender_edit);
        memberNameText = (TextView) view.findViewById(R.id.memberName_text);
        memberGenderText = (TextView) view.findViewById(R.id.memberGender_text);
        memberAgeText = (TextView) view.findViewById(R.id.memberAge_text);
        memberPhoneText = (TextView) view.findViewById(R.id.familyPhone_text);
        memberIdText = (TextView) view.findViewById(R.id.memberId_text);
        memberNameText.setText(member.getMembername());
        memberAgeText.setText(member.getAge() + "");
        memberGenderText.setText(GenderConversion.toString(member.getGender()));
        memberPhoneText.setText(member.getFamilyPhone());
        memberIdText.setText(member.getMemberid() + "");
        if (!StringUtil.isEmpty(member.getImage())) {
            imageLoader.bindBitmap(member.getImage(), memberImage);
        }

    }

    public void saveMemberInfo() {
        String memberName = memberNameEdit.getText().toString().trim();
        String memberAge = memberAgeEdit.getText().toString().trim();
        if (StringUtil.isEmpty(memberName)) {
            ToastUtil.showToastShort(mActivity, R.string.input_username);
            return;
        }
        if (StringUtil.isEmpty(memberAge)) {
            ToastUtil.showToastShort(mActivity, R.string.input_age);
            return;
        }

        int memberGender = getCheckGender();
        Log.i(TAG, "memberGender-------------->" + memberGender);
        if (memberGender == NO_CHECK) {
            ToastUtil.showToastShort(mActivity, R.string.no_check_gender);
            return;
        }
        member.setMembername(memberName);
        member.setAge(Integer.parseInt(memberAge));
        member.setGender(memberGender);
        HealthNetAPI.modifyMemberInfo(member, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                if (JsonToBean.isOK(result)) {
                    memberNameText.setText(member.getMembername());
                    memberAgeText.setText(member.getAge() + "");
                    memberGenderText.setText(GenderConversion.toString(member.getGender()));
                    healthApp.getDataBaseAPI().updateMember(member);
                    changeToDisplayMode();
                    mActivity.memberFragment.submit.setVisibility(View.INVISIBLE);
                    //mActivity.returnDisplayMode();
                }
                ToastUtil.showToastLong(mActivity, StringUtil.ENChangeToCH(JsonToBean.getMsg(result)));

            }
        });
    }

    public void changeToEditMode() {
        Log.d(TAG, "changeToEditMode");
        memberNameText.setVisibility(View.GONE);
        memberGenderText.setVisibility(View.GONE);
        memberAgeText.setVisibility(View.GONE);
        memberNameEdit.setVisibility(View.VISIBLE);
        memberAgeEdit.setVisibility(View.VISIBLE);
        memberGenderEdit.setVisibility(View.VISIBLE);
    }

    public void changeToDisplayMode() {
        Log.d(TAG, "changeToDisplayMode");
        memberNameText.setVisibility(View.VISIBLE);
        memberGenderText.setVisibility(View.VISIBLE);
        memberAgeText.setVisibility(View.VISIBLE);
        memberNameEdit.setVisibility(View.GONE);
        memberAgeEdit.setVisibility(View.GONE);
        memberGenderEdit.setVisibility(View.GONE);
        Log.d(TAG, "changeToDisplayMode finish");
    }

    private int getCheckGender() {
        switch (memberGenderEdit.getCheckedRadioButtonId()) {
            case R.id.memberGender_man:
                return CHECK_MAN;
            case R.id.memberGender_woman:
                return CHECK_WOMAN;
            case R.id.memberGender_secret:
                return CHECK_SECRET;

        }

        return NO_CHECK;
    }

    public void setIsModifing(boolean isModifing) {
        this.isModifing = isModifing;
    }

    public boolean isModifing() {
        return isModifing;
    }

}
