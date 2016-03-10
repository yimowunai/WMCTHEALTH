package com.wmct.net;

import java.io.IOException;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/1 10:50
 * <p/>
 * --------------------------------------
 */
public interface HttpStack {
    public Response performRequest(Request<?> request) throws IOException;
}
