package com.wmct.net;

import android.os.Build;

import java.net.HttpURLConnection;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/1 10:53
 * <p/>
 * --------------------------------------
 */
public class HttpStackFactory {
    private static final int ANDROID_NET_DEVIDE = 9;

    public static HttpStack createHttpStack(){
        int sdk_api = Build.VERSION.SDK_INT;
        if(sdk_api>ANDROID_NET_DEVIDE){
            return new HttpURLConnectionStack();
        }
        return new HttpClientStack();

    }
}
