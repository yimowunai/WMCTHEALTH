package com.wmct.health;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/4 21:54
 * <p>
 * --------------------------------------
 */
public class MultipartEntity implements HttpEntity {

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public Header getContentType() {
        return null;
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return null;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {

    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void consumeContent() throws IOException {

    }
}
