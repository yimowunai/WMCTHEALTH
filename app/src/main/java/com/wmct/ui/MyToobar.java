package com.wmct.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wmct.activity.LoginActivity;
import com.wmct.health.R;


/**
 * Created by lang on 2016/1/16.
 */
public class MyToobar extends Toolbar {

    private LayoutInflater mInflater;

    private View mView;
    private Button mButton;
    private Context mContext;
//    private ImageButton mRightImageButton;

    public MyToobar(Context context) {
        this(context, null);
    }

    public MyToobar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToobar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //oast.makeText(getContext(), "登录", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });

        /*if(attrs != null){
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(
                    getContext(),attrs,R.styleable.MyToobar,defStyleAttr,0);
            Drawable rightIcon = a.getDrawable(R.styleable.MyToobar_rightButtonIcon);
            if(rightIcon != null){
                setRightButtonIcon(rightIcon);

            }
            a.recycle();
        }
        mRightImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "登录", Toast.LENGTH_LONG).show();
            }
        });*/
    }

   /* private void setRightButtonIcon(Drawable rightIcon) {
        if(mRightImageButton !=null){
            mRightImageButton.setImageDrawable(rightIcon);
        }
    }*/

    private void initView() {

        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

//            mRightImageButton = (ImageButton) mView.findViewById(R.id.toolbar_rightButton);

            mButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }
    }
}
