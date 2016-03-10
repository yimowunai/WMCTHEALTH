package com.wmct.net.Requset;

import com.wmct.net.Request;
import com.wmct.net.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/11 16:14
 * <p/>
 * --------------------------------------
 */
public class FileRequest extends Request<File> {
    private static final String adCachePath = "./data/data/com.wmct.health/cache/welcome/";
    private String fileName;

    public FileRequest(HttpMethod httpMethod, String fileName, String url, RequestListener<File> listener) {
        super(httpMethod, url, listener);
        this.fileName = fileName;
    }

    @Override
    public File parseResponse(Response response) {
        if (response != null && response.getRawData() != null) {
            byte[] datas = response.getRawData();
            File file = new File(adCachePath + fileName);
            FileOutputStream ous = null;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    ous = new FileOutputStream(file);
                    ous.write(datas);
                    ous.flush();
                    ous.close();
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ous != null) {
                        try {
                            ous.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
