package com.wmct.net;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/3 10:41
 * <p>
 * --------------------------------------
 */
public final class WmctNet {
    public static RequestQueue newRequestQueue() {
        return newRequestQueue(RequestQueue.DEFAULT_CORE_POOL_SIZE);
    }

    public static RequestQueue newRequestQueue(int coreNums) {
        return newRequestQueue(coreNums, null);
    }

    public static RequestQueue newRequestQueue(int coreNums, HttpStack httpStack) {
        RequestQueue requestQueue = new RequestQueue(coreNums, httpStack);
        requestQueue.start();
        return requestQueue;
    }
}
