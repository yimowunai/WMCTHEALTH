package com.wmct.ui;

import android.content.Context;

import dmax.dialog.SpotsDialog;

/**
 * Created by lang on 2016/1/11.
 */
public class RemindDialog {

    private Context mContext;
    private SpotsDialog mDialog;
    public RemindDialog(Context context){
        mContext = context;
        mDialog = new SpotsDialog(mContext,"正在测量中，请您稍等...");
    }
    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }


    public void setLoadMessage(String remindMessage){
        mDialog.setMessage(remindMessage);
    }

}
