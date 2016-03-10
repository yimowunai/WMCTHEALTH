package com.wmct.net.Requset;

import com.wmct.net.Request;
import com.wmct.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/4 9:44
 * <p>
 * --------------------------------------
 */
public class JsonRequest extends Request<JSONObject> {


    public JsonRequest(HttpMethod httpMethod, String url, RequestListener<JSONObject> listener) {
        super(httpMethod, url, listener);
    }

    @Override
    public JSONObject parseResponse(Response response) {
        if(response!=null&&response.getRawData()!=null){
            byte[] datas = response.getRawData();
            String string = null;
            JSONObject jsonObject = null;
            try {
                string = new String(datas, Request.DEFAULT_PARAMS_ENCODING);
                jsonObject = new JSONObject(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
       return new JSONObject();
    }
}
