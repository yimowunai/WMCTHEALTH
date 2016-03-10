/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wmct.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.wmct.bean.MeasureData;
import com.wmct.health.HealthApp;
import com.wmct.health.R;
import com.wmct.net.Request;
import com.wmct.netApi.HealthNetAPI;
import com.wmct.ui.RemindDialog;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;

import org.json.JSONObject;

import android_serialport_api.SerialPort;

public class VisitorMeasureActivity extends Activity {
    private static final String TAG = "VisitorMeasureActivity";
    public static VisitorMeasureActivity instance = null;
    private HealthApp healthApp;
    private USBBroadcastReceiver mReiceiver;
    protected SerialPort mSerialPort = null;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private File file0;
    private File file1;

    private int hightPressure, lowPressure, heartRate, heartState;
    private ArrayList<Integer> results = new ArrayList<>();
    private int DataCount = 0;
    private int FDCount = 0;
    private int FECount = 0;
    private int ErrCount = 0;
    private RemindDialog mRemindDialog;

    private TextView mHPresure;
    private TextView mLPresure;
    private TextView mHRate;
    private TextView welcome;
    private TextView chinaRainbow;
    private TextView measureSystem;

    private final static String ATTACH_ACTION = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private final static String DETACH_ACTION = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_interface);
        healthApp = (HealthApp) getApplication();
        initView();
        initDev();
        initSerialPort();
        mReiceiver = new USBBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ATTACH_ACTION);
        filter.addAction(DETACH_ACTION);
        registerReceiver(mReiceiver, filter);
        Intent intent = new Intent(VisitorMeasureActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initDev() {
        file0 = new File("/dev/ttyUSB0");
        file1 = new File("/dev/ttyUSB1");
    }

    private void initSerialPort() {
        try {
            mSerialPort = getSerialPort();
            if (mSerialPort != null) {
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
                mReadThread = new ReadThread();
                mReadThread.start();
            }
        } catch (SecurityException e) {
            DisplayError(R.string.error_security);
        } catch (IOException e) {
            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            DisplayError(R.string.error_configuration);
        }
    }

    private void initView() {
        instance = this;
        welcome = (TextView) findViewById(R.id.welcome);
        chinaRainbow = (TextView) findViewById(R.id.chinarainbow);
        measureSystem = (TextView) findViewById(R.id.measuresystem);
        mHPresure = (TextView) findViewById(R.id.hPresure);
        mLPresure = (TextView) findViewById(R.id.lPresure);
        mHRate = (TextView) findViewById(R.id.hRate);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/huawenxingkai.ttf");
        welcome.setTypeface(typeface);
        chinaRainbow.setTypeface(typeface);
        measureSystem.setTypeface(typeface);

        mRemindDialog = new RemindDialog(this);
    }


    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (file0.exists()) {
            mSerialPort = new SerialPort(file0, 9600, 0);
        } else if (file1.exists()) {
            mSerialPort = new SerialPort(file1, 9600, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    public class USBBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ATTACH_ACTION)) {

                if (file0.exists() || file1.exists()) {
                    initSerialPort();
                }

            } else if (intent.getAction().equals(DETACH_ACTION)) {
//				Toast.makeText(context, "拔出", Toast.LENGTH_LONG).show();
                mReadThread.interrupt();

                closeSerialPort();
            }

        }
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);

                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VisitorMeasureActivity.this.finish();
            }
        });
        b.show();
    }

    private void onDataReceived(final byte[] buffer, final int size) {

        runOnUiThread(new Runnable() {
            public void run() {

                Log.i("TAG", "" + size);

                for (int i = 0; i < size; i++) {
                    int v = buffer[i] & 0xFF;
                    switch (v) {
                        case 254:
                            FECount++;
                            if (FECount == 1) {
                                mRemindDialog.showDialog();
                            }
                            break;
                        case 253:

                            FDCount++;
                            break;
                        case 90:

                            break;
                        case 13:
                            if (FDCount == 2 && results != null && results.size() == 8) {
                                DataCount++;
                                if (DataCount == 1) {
                                    transformResult(results);
                                    mRemindDialog.dismissDialog();
                                    mHPresure.setText(hightPressure + "");
                                    mLPresure.setText(lowPressure + "");
                                    mHRate.setText(heartRate + "");
                                    final MeasureData measureData = new MeasureData();
                                    measureData.setMeasuretime(StringUtil.getCurrentTime());
                                    measureData.setTerminalid(healthApp.getTerminalId());
                                    measureData.setDiastolicpressure(lowPressure);
                                    measureData.setSystolicpressure(hightPressure);
                                    measureData.setHeartrate(heartRate);
                                    if (heartState == 85) {
                                        measureData.setHeartstate(0);
                                    } else {
                                        measureData.setHeartstate(1);
                                    }

                                    HealthNetAPI.addVisitData(measureData, new Request.RequestListener<JSONObject>() {
                                        @Override
                                        public void onComplete(int stateCode, JSONObject result, String msg) {
                                            if (JsonToBean.isOK(result)) {
                                                Log.d(TAG, "游客数据上传成功");
                                            } else {
                                                healthApp.getDataBaseAPI().insertVisitordata(measureData);
                                            }
                                        }
                                    });
                                }

                            } else if (FDCount == 2 && results != null && results.size() == 5) {

                                if (results.get(0) != 0) {
                                    ErrCount++;
                                }
                                switch (results.get(0)) {
                                    case 0:
                                        FDCount = 0;
                                        DataCount = 0;
                                        FECount = 0;
                                        ErrCount = 0;
                                        mRemindDialog.dismissDialog();
                                        break;
                                    case 1:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "人体心跳信号太小或压力突降", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 2:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "有杂讯干扰", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 3:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "测量结果异常，需要重测", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 4:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "测得的结果异常", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 15:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "充气时间过长", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 11:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "电源低电压", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 14:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "EEPROM异常", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    default:
                                        if (ErrCount == 1) {
                                            mRemindDialog.dismissDialog();
                                            Toast.makeText(VisitorMeasureActivity.this, "未知错误", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                            results.clear();
                            FDCount = 0;
                            break;
                        case 10:
                            break;
                        case 165:

                            break;
                        default:
                            if (FDCount == 2) {
                                Log.e("TAG", "" + "ADD");
                                results.add(v);
                            }
                            break;

                    }
                }

            }


        });
    }

    private void transformResult(ArrayList<Integer> results) {
        // TODO Auto-generated method stub
        int hpHB, hpLB, lpHB, lpLB, hrHB, hrLB, hsLB, hsHB;
        hpLB = getRealV(results.get(0));
        hpHB = getRealV(results.get(1));
        lpLB = getRealV(results.get(2));
        lpHB = getRealV(results.get(3));
        hrLB = getRealV(results.get(4));
        hrHB = getRealV(results.get(5));
        hsLB = results.get(6);
        hightPressure = hpHB * 16 + hpLB;
        lowPressure = lpHB * 16 + lpLB;
        heartRate = hrHB * 16 + hrLB;
        heartState = hsLB;
        Log.d(TAG, "heartState =" + heartState);
    }

    protected int getRealV(Integer integer) {
        // TODO Auto-generated method stub
        switch (integer - 48) {
            case 0:
                return 0;

            case 1:
                return 1;

            case 2:
                return 2;

            case 3:
                return 3;

            case 4:
                return 4;

            case 5:
                return 5;

            case 6:
                return 6;

            case 7:
                return 7;

            case 8:
                return 8;

            case 9:
                return 9;

            case 17:
                return 10;

            case 18:
                return 11;

            case 19:
                return 12;

            case 20:
                return 13;

            case 21:
                return 14;

            case 22:
                return 15;
            default:
                return -1;

        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        closeSerialPort();
        unregisterReceiver(mReiceiver);
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
