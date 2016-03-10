package com.wmct.health;

import com.wmct.net.Request;
import com.wmct.net.Response;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/2 15:28
 * <p/>
 * --------------------------------------
 */
public class StringRequest extends Request<String> {
    public StringRequest(HttpMethod httpMethod, String url, RequestListener<String> listener) {
        super(httpMethod, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
        return new String(response.getRawData());
    }
}
