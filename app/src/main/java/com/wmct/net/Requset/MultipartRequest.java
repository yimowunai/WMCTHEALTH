package com.wmct.net.Requset;

import android.util.Log;

import com.wmct.net.MultipartEntity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/4 21:04
 * <p/>
 * --------------------------------------
 */
public class MultipartRequest extends JsonRequest {
    private String filePath;
    private long memberid;

    MultipartEntity mMultiPartEntity = new MultipartEntity();

    public MultipartRequest(HttpMethod httpMethod, String url, RequestListener<JSONObject> listener) {
        super(httpMethod, url, listener);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public long getMemberid() {
        return memberid;
    }

    /**
     * @return
     */
    public MultipartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 将MultipartEntity中的参数写入到bos中
            File file = null;
            if (!"".equals(filePath) && filePath != null) {
                file = new File(filePath);
            }
            if (!file.exists()) {
                return new byte[0];
            }
            mMultiPartEntity.addFilePart("member_image", file);
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

}
