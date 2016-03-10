package com.wmct.net;

import android.util.Log;

import com.wmct.health.R;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/1 11:05
 * <p/>
 * --------------------------------------
 */
public class NetworkDispatcher extends Thread {
    private static final String TAG = "NetworkDispatcher";
    private BlockingQueue<Request<?>> mRequestQueue;
    private HttpStack mHttpStack;
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();
    private boolean isStop = false;

    public NetworkDispatcher(BlockingQueue<Request<?>> requestQueue, HttpStack httpStack) {
        mRequestQueue = requestQueue;
        mHttpStack = httpStack;
    }

    @Override
    public void run() {
        while (!isStop) {
            try {
                Log.d(TAG,"NetworkDispatcher running");
                Request<?> request = mRequestQueue.take();
                if (request.isCancel()) {
                    Log.i(TAG, request.toString() + "取消执行了");
                    continue;
                }
                Response response = null;
                response = mHttpStack.performRequest(request);
                mResponseDelivery.deliveryResponse(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void quit() {
        isStop = true;
        interrupt();
    }
}
