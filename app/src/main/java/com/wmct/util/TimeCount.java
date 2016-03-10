package com.wmct.util;

import android.os.CountDownTimer;
import android.widget.Button;

import com.wmct.health.R;


/**
 * Created by yimowunai on 2016/1/6.
 */
public class TimeCount extends CountDownTimer {
    Button button;

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TimeCount(Button button, long millisInFuture, long countDownInterval) {
        this(millisInFuture, countDownInterval);
        this.button = button;
    }

    public void onTick(long millisUntilFinished) {
        button.setClickable(false);//防止重复点击
        button.setText(millisUntilFinished / 1000 + "s");
    }

    @Override
    public void onFinish() {
        button.setText(R.string.get_auth_code);
        button.setClickable(true);
    }
}