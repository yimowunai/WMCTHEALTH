package com.wmct.net;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * 网络访问类 ：抽象的同时具有泛型的类  用户可以自己定义不同的具体的访问类
 * 作者 ：三月半
 * <T> T为具体返回的数据类型
 * 时间 ：2015/12/31 15:21
 * <p/>
 * --------------------------------------
 */
public abstract class Request<T> implements Comparable<Request<T>> {

    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8"; //默认的编码格式
    public static final String HEADER_CONTENT_TYPE = "Content_Type";
    private static final String TAG = "Request的父类Request<T>";
    private HttpMethod mHttpMethod = HttpMethod.POST;   //请求的默认凡是方式是post
    private String mUrl;  //请求的网络地址
    private RequestListener mListener; //监听函数
    private int mSerialNum = 0;     //请求的序列号
    private Priority mPriority = Priority.NORMAL; //请求优先级别，默认为正常
    private boolean isCancel = false;       //是否取消此次请求，默认不开启
    private boolean mShouldCache = false;   //是否开启缓存功能，默认不开启
    private Map<String, String> mHeader = new HashMap<String, String>(); //请求头
    private Map<String, String> mParams = new HashMap<String, String>();   //请求体


    /**
     * 支持的请求方法（post和get）,这里用枚举。
     */
    public static enum HttpMethod {
        GET("GET"), POST("POST");
        private String mHttpMethod;

        private HttpMethod(String httpMethod) {
            mHttpMethod = httpMethod;
        }

        public String toString() {
            return mHttpMethod;
        }
    }

    /**
     * 网络访问的优先级别
     */
    public static enum Priority {
        LOW, NORMAL, HIGN, IMMEDIATE;
    }

    public static interface RequestListener<T> {
        public void onComplete(int stateCode, T result, String msg);
    }

    public Request(HttpMethod httpMethod, String url, RequestListener<T> listener) {
        this.mHttpMethod = httpMethod;
        this.mUrl = url;
        this.mListener = listener;
    }


    /**
     * 获取访问的地址
     *
     * @return 访问地址
     */
    public String getUrl() {
        return mUrl;
    }

    public boolean isShouldCache() {
        return mShouldCache;
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public RequestListener getListener() {
        return mListener;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public boolean isHttps() {
        return mUrl.startsWith("https");
    }

    /**
     * 设置网络访问地址
     *
     * @param url
     */
    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public void setHeader(Map<String, String> header) {
        this.mHeader = header;
    }

    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }

    public void setExtraHeader(Map<String, String> extraHeader) {
        Set<String> extraHeaderKey = extraHeader.keySet();
        for (String key : extraHeaderKey) {
            mHeader.put(key, extraHeader.get(key));
        }
        //mHeader.putAll(extraHeader);
    }


    public void setHttpMethod(HttpMethod httpMethod) {
        this.mHttpMethod = httpMethod;
    }

    public void setListener(RequestListener listener) {
        this.mListener = listener;
    }

    public void setParams(Map<String, String> params) {
        this.mParams = params;
    }

    public void addParams(String key, String value) {
        mParams.put(key, value);
    }

    public void setExtraParams(Map<String, String> extraParams) {
        Set<String> extraParamsKey = extraParams.keySet();
        for (String key : extraParamsKey) {
            mParams.put(key, extraParams.get(key));
        }
        //mParams.putAll(extraParams);
    }

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    public void setSerialNum(int serialNum) {
        this.mSerialNum = serialNum;
    }

    public void setShouldCache(boolean isCache) {
        this.mShouldCache = isCache;
    }

    public byte[] getBody() {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String encoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), encoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), encoding));

                encodedParams.append('&');
            }

            String paramsString = encodedParams.toString();
            // MessageDigest.getInstance("MD5").;
            //String paramStringMD5 = M
            return paramsString.substring(0, paramsString.length() - 1).getBytes(DEFAULT_PARAMS_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compareTo(Request<T> another) {
        Priority thisPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        return thisPriority.equals(anotherPriority) ? this.getSerialNum() - another.getSerialNum()
                : thisPriority.ordinal() - anotherPriority.ordinal();
    }

    public abstract T parseResponse(Response response);

    public final void deliveryResponse(Response response) {
        //Log.d(TAG, Thread.currentThread().getName());
        T result = parseResponse(response);
        if (mListener != null) {
            int stateCode = response != null ? response.getStatusCode() : -1;
            String msg = response != null ? response.getMessage() : "null";
            mListener.onComplete(stateCode, result, msg);
        }
    }

}
