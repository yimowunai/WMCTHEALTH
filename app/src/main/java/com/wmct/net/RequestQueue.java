package com.wmct.net;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/1 10:33
 * <p>
 * --------------------------------------
 */
public final class RequestQueue {
    private static final String TAG = "RequsetQueue";
    /**
     * 线程安全的请求队列
     */
    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<Request<?>>();
    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static int DEFAULT_CORE_POOL_SIZE = CPU_COUNT + 1;
    private int mDispatcherNums = DEFAULT_CORE_POOL_SIZE;
    private NetworkDispatcher[] mNetworkDispatcher;
    private HttpStack mHttpStack;

    RequestQueue() {
        this(DEFAULT_CORE_POOL_SIZE, null);
    }

    RequestQueue(int coreNums, HttpStack httpStack) {
        mDispatcherNums = coreNums;
        mHttpStack = httpStack != null ? httpStack : HttpStackFactory.createHttpStack();
    }

    public void start() {

        mNetworkDispatcher = new NetworkDispatcher[mDispatcherNums];
        Log.d(TAG, "DispatcherNums =" + mDispatcherNums);
        for (int i = 0; i < mDispatcherNums; i++) {
            mNetworkDispatcher[i] = new NetworkDispatcher(mRequestQueue, mHttpStack);
            mNetworkDispatcher[i].start();
            
        }
    }

    public void stop() {
        if (mNetworkDispatcher != null && mNetworkDispatcher.length > 0) {
            for (int i = 0; i < mNetworkDispatcher.length; i++) {
                mNetworkDispatcher[i].quit();
            }
        }
    }

    public void addRequest(Request<?> request) {
        if (!mRequestQueue.contains(request)) {
            request.setSerialNum(this.generateSequenceNum());
            mRequestQueue.add(request);
        } else {
            Log.d(TAG, request.toString() + "请求队列中已经存在此请求");
        }
    }

    public void clear() {
        mRequestQueue.clear();
    }

    public int generateSequenceNum() {
        return mSequenceGenerator.incrementAndGet();
    }
}
