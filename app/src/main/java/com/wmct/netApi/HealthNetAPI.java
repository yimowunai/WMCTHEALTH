package com.wmct.netApi;

import android.util.Log;

import com.wmct.bean.Family;
import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;
import com.wmct.net.Request;
import com.wmct.net.RequestQueue;
import com.wmct.net.Requset.FileRequest;
import com.wmct.net.Requset.JsonRequest;
import com.wmct.net.Requset.MultipartRequest;
import com.wmct.net.WmctNet;
import com.wmct.util.JsonToBean;
import com.wmct.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/3 19:12
 * <p/>
 * --------------------------------------
 */
public class HealthNetAPI {
    private static final String TAG = "HealthNetAPI";
    private static final String adCachePath = "./data/data/com.wmct.health/cache/welcome/";
    private static RequestQueue mRequestQueue = WmctNet.newRequestQueue();

    private static final String WMCT_HEALTH_HOST_IP = "http://58.194.170.16";
    private static final String WMCT_HEALTH_HOST_PORT = ":80";
    private static final String WMCT_HEALTH_HOST = WMCT_HEALTH_HOST_IP + WMCT_HEALTH_HOST_PORT;
    private static final String WMCT_HEALTH_REGIST = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/registerfamily";
    private static final String WMCT_HEALTH_RESET_PASSWORD = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/sendresetpasswdtophone";
    private static final String WMCT_HEALTH_MODIFI_PASSWORD = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/changepasswd";
    private static final String WMCT_HEALTH_ADD_MEMBER = WMCT_HEALTH_HOST + "/body/index.php/Home/Member/addmembertofamily";
    private static final String WMCT_HEALTH_FIND_ALL_MEMBER = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/getAllmember";
    private static final String WMCT_HEALTH_MSG_RESET_VERIFACATION = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/sendresetverifycodetophone";
    private static final String WMCT_HEALTH_MSG_REGIST_VERIFACATION = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/sendverifycodetophone";
    private static final String WMCT_HEALTH_LOGIN = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/login";
    private static final String WMCT_HEALTH_LOGIN_NEW = WMCT_HEALTH_HOST + "/body/index.php/Home/Login/loginnew";
    private static final String WMCT_HEALTH_UPLOAD = WMCT_HEALTH_HOST + "/body/index.php/Home/Member/uploadimgtomember/memberid/";
    private static final String WMCT_HEALTH_DELETE_MEMBER = WMCT_HEALTH_HOST + "/body/index.php/Home/Member/deletemember";
    private static final String WMCT_HEALTH_ADD_MEASUREDATA = WMCT_HEALTH_HOST + "/body/index.php/Home/Measure/adddata";
    private static final String WMCT_HEALTH_BATCH_ADD_MEASUREDATA = WMCT_HEALTH_HOST + "/body/index.php/Home/Measure/addalldata";

    private static final String WMCT_HEALTH_ADD_VISITOR_MEASUREDATA = WMCT_HEALTH_HOST + "/body/index.php/Home/Measure/addvisitordata";
    private static final String WMCT_HEALTH_BATCH_ADD_VISITOR_MEASUREDATA = WMCT_HEALTH_HOST + "/body/index.php/Home/Measure/addallvisitordata";
    private static final String WMCT_HEALTH_QUERY_DATA = WMCT_HEALTH_HOST + "/body/index.php/Home/Measure/selectdata";
    private static final String WMCT_HEALTH_LASTE_DATA = WMCT_HEALTH_HOST + "/body/index.php/Home/Measure/getlastdata";
    private static final String WMCT_HEALTH_GET_MEMBER_INFO = WMCT_HEALTH_HOST + "/body/index.php/Home/Member/getmember";
    private static final String WMCT_HEALTH_MODIFY_MEMBER_INFO = WMCT_HEALTH_HOST + "/body/index.php/Home/Member/changememberinfo";
    private static final String WMCT_HEALTH_ADVERTISEMENT = WMCT_HEALTH_HOST + "/body/index.php/Home/Ads/index";

    /**
     * 给特定的手机号发送客户端生成的验证码(用于注册)
     *
     * @param phone    手机号
     * @param authcode 验证码
     * @param listener
     */
    public static void msgRegistVerification(String phone, String authcode, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_MSG_REGIST_VERIFACATION, listener);
        request.addParams("phone", phone);
        request.addParams("verificationcode", authcode);
        mRequestQueue.addRequest(request);
    }

    /**
     * 用户注册
     *
     * @param family
     * @param listener
     */
    public static void regist(Family family, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_REGIST, listener);

        request.addParams("name", family.getName());
        request.addParams("password", family.getPwd());
        request.addParams("phone", family.getPhone());
        request.addParams("address", family.getAddress());  //可为空
        request.addParams("terminalid", family.getTerminalid());  //可为空
        mRequestQueue.addRequest(request);
    }

    /**
     * 登陆
     *
     * @param family   用户
     * @param listener
     */
    public static void loginold(Family family, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_LOGIN, listener);
        request.addParams("phone", family.getPhone());
        request.addParams("password", family.getPwd());
        request.addParams("terminalid", family.getTerminalid());
        mRequestQueue.addRequest(request);
    }

    /**
     * 新的登陆接口
     *
     * @param family
     * @param listener
     */
    public static void login(Family family, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_LOGIN_NEW, listener);
        request.addParams("phone", family.getPhone());
        request.addParams("password", family.getPwd());
        request.addParams("terminalid", family.getTerminalid());
        mRequestQueue.addRequest(request);
    }


    public static void findAllMembers(Family family, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_FIND_ALL_MEMBER, listener);
        request.addParams("phone", family.getPhone());
        request.addParams("password", family.getPwd());
        request.addParams("terminalid", family.getTerminalid());
        mRequestQueue.addRequest(request);
    }

    /**
     * 用户增加成员功能
     *
     * @param phone
     * @param member
     * @param listener
     */
    public static void addMember(String phone, Member member, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_ADD_MEMBER, listener);
        request.addParams("familyphone", phone);
        request.addParams("membername", member.getMembername());
        request.addParams("age", member.getAge() + "");
        request.addParams("gender", member.getGender() + "");
        mRequestQueue.addRequest(request);

    }

    /**
     * 用户上传成员图像功能  (暂不支持)
     *
     * @param filePath
     * @param memberid
     * @param listener
     */
    public static void fileUpload(String filePath, long memberid, Request.RequestListener<JSONObject> listener) {
        String url = reWriteRUL(WMCT_HEALTH_UPLOAD, memberid);
        Log.d(TAG, "url=" + url);
        MultipartRequest request = new MultipartRequest(Request.HttpMethod.POST, url, listener);

        request.setFilePath(filePath);
        mRequestQueue.addRequest(request);
    }

    private static String reWriteRUL(String uploadUri, long memberid) {
        return uploadUri + memberid;
    }

    public static void fileUpload(String filePath, Request.RequestListener<JSONObject> listener) {
        MultipartRequest request = new MultipartRequest(Request.HttpMethod.POST, WMCT_HEALTH_UPLOAD, listener);
        request.setFilePath(filePath);
        mRequestQueue.addRequest(request);
    }

    /**
     * 用户删除成员功能
     *
     * @param memberid
     * @param familyPhone
     * @param listener
     */
    public static void deleteMember(long memberid, String familyPhone, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_DELETE_MEMBER, listener);
        request.addParams("memberid", memberid + "");
        request.addParams("familyphone", familyPhone);
        mRequestQueue.addRequest(request);
    }

    /**
     * 成员新增测量数据功能
     *
     * @param memberid
     * @param data
     * @param listener
     */
    public static void addData(long memberid, MeasureData data, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_ADD_MEASUREDATA, listener);
        request.addParams("memberid", memberid + "");
        request.addParams("diastolicpressure", data.getDiastolicpressure() + "");
        request.addParams("systolicpressure", data.getSystolicpressure() + "");
        request.addParams("heartrate", data.getHeartrate() + "");
        request.addParams("heartstate", data.getHeartstate() + "");
        request.addParams("measuretime", data.getMeasuretime() + "");
        mRequestQueue.addRequest(request);
    }

    /**
     * 批量上传登陆的未上传的测量数据。
     *
     * @param list
     * @param listener
     */
    public static void batchAddData(List<MeasureData> list, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_BATCH_ADD_MEASUREDATA, listener);
        JSONArray jsonArray = JsonToBean.toMeasureDataJsonarray(list);
        request.addParams("data", jsonArray.toString());
        mRequestQueue.addRequest(request);
    }

    public static void batchAddVisitorData(List<MeasureData> list, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_BATCH_ADD_VISITOR_MEASUREDATA, listener);
        JSONArray jsonArray = JsonToBean.toVisitorMeasureDataJsonarray(list);
        request.addParams("data", jsonArray.toString());
        mRequestQueue.addRequest(request);
    }

    /**
     * 游客添加测量数据。
     *
     * @param data
     * @param listener
     */
    public static void addVisitData(MeasureData data, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_ADD_VISITOR_MEASUREDATA, listener);
        request.addParams("terminalid", data.getTerminalid());
        request.addParams("diastolicpressure", data.getDiastolicpressure() + "");
        request.addParams("systolicpressure", data.getSystolicpressure() + "");
        request.addParams("heartrate", data.getHeartrate() + "");
        request.addParams("heartstate", data.getHeartstate() + "");
        request.addParams("measuretime", data.getMeasuretime() + "");
        mRequestQueue.addRequest(request);
    }


    /**
     * 查询某成员最近的测量数据（默认为10条）
     *
     * @param memberid
     * @param size
     * @param listener
     */
    public static void queryData(long memberid, int size, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_QUERY_DATA, listener);
        request.addParams("memberid", memberid + "");
        request.addParams("size", size + "");

        mRequestQueue.addRequest(request);
    }

    /**
     * 查询某成员最后一条测量数据
     *
     * @param memberid
     * @param listener
     */
    public static void lasteData(long memberid, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_LASTE_DATA, listener);
        request.addParams("memberid", memberid + "");

        mRequestQueue.addRequest(request);
    }

    /**
     * 用户获取member信息功能
     *
     * @param memberid
     * @param listener
     */
    public static void memberInfo(long memberid, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_GET_MEMBER_INFO, listener);
        request.addParams("memberid", memberid + "");

        mRequestQueue.addRequest(request);
    }

    /**
     * 用户修改member信息功能
     *
     * @param member
     * @param listener
     */
    public static void modifyMemberInfo(Member member, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_MODIFY_MEMBER_INFO, listener);
        request.addParams("memberid", member.getMemberid() + "");
        request.addParams("membername", member.getMembername());
        request.addParams("age", member.getAge() + "");
        request.addParams("gender", member.getGender() + "");

        mRequestQueue.addRequest(request);
    }

    /**
     * 给特定的手机号发送客户端生成的验证码(用于重置密码)
     *
     * @param phone    手机号
     * @param authcode 验证码
     * @param listener
     */
    public static void msgResetVerification(String phone, String authcode, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_MSG_RESET_VERIFACATION, listener);
        request.addParams("phone", phone);
        request.addParams("verificationcode", authcode);
        mRequestQueue.addRequest(request);
    }

    /**
     * 重置密码
     *
     * @param phone
     * @param password
     * @param listener
     */
    public static void resetPassword(String phone, String password, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST,
                WMCT_HEALTH_RESET_PASSWORD, listener);
        request.addParams("phone", phone);
        request.addParams("resetpasswd", password);
        mRequestQueue.addRequest(request);
    }

    /**
     * 用户修改密码功能
     *
     * @param phone
     * @param oldPassword
     * @param newPassword
     * @param listener
     */
    public static void modifiPassword(String phone, String oldPassword, String newPassword, Request.RequestListener<JSONObject> listener) {
        JsonRequest request = new JsonRequest(Request.HttpMethod.POST,
                WMCT_HEALTH_MODIFI_PASSWORD, listener);
        request.addParams("phone", phone);
        request.addParams("oldpasswd", oldPassword);
        request.addParams("newpasswd", newPassword);
        mRequestQueue.addRequest(request);
    }

    public static void downAdvertisement() {
        final JsonRequest request = new JsonRequest(Request.HttpMethod.POST, WMCT_HEALTH_ADVERTISEMENT, new Request.RequestListener<JSONObject>() {
            @Override
            public void onComplete(int stateCode, JSONObject result, String msg) {
                if (stateCode == 200) {
                    String fileName = "";
                    String filePath = "";
                    try {
                        fileName = result.getString("filename");
                        filePath = result.getString("filepath");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //String url = StringUtil.rewriteImageUrl(filePath);
                    Log.d(TAG, "filePath =" + filePath);
                    if (!StringUtil.isEmpty(fileName) && !StringUtil.isEmpty(filePath)) {
                        File file = new File(adCachePath + fileName);
                        if (!file.exists()) {
                            FileRequest fileRequest = new FileRequest(Request.HttpMethod.GET, fileName, filePath, new Request.RequestListener<File>() {
                                @Override
                                public void onComplete(int stateCode, File result, String msg) {
                                    Log.d(TAG, "file path =" + result.getAbsoluteFile());
                                }
                            });
                            mRequestQueue.addRequest(fileRequest);
                        }

                    }

                }
            }
        });
        mRequestQueue.addRequest(request);


    }

    public static void fileUpload(String filePath) {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/from-data";
        String CHARSET = "UTF-8";
        try {
            URL url = new URL(WMCT_HEALTH_UPLOAD);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());

            if (!"".equals(filePath) && filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    String fileName = "uninstall.png";
                    sb.append("Content-Disposition: form-data; name=\"member_image\";"
                            + " filename=\"" + fileName + "\"" + LINEND);
                    // sb.append("Content-Type: application/octet-stream; charset=" CHARSET + LINEND);
                    sb.append(LINEND);
                    outputStream.write(sb.toString().getBytes("UTF-8"));
                    //outputStream.writeBytes(sb.toString());
                    Log.d(TAG, sb.toString());
                    InputStream is = new FileInputStream(file);
                    int count = 0;
                    byte[] buff = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buff)) != -1) {
                        count += len;
                        Log.d(TAG, "count = " + count);
                        outputStream.write(buff, 0, len);
                    }
                    is.close();
                    outputStream.write(LINEND.getBytes("UTF-8"));
                    //outputStream.writeBytes(LINEND);
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes("UTF-8");
                    outputStream.write(end_data);
                    //outputStream.writeBytes(PREFIX + BOUNDARY + PREFIX + LINEND);
                    Log.d(TAG, "outputStream.size() = " + outputStream.size());
                    outputStream.flush();
                    // outputStream.close();
                    int responseCode = conn.getResponseCode();
                    InputStream inputStream = null;
                    if (responseCode == 200) {
                        inputStream = conn.getInputStream();
                        StringBuffer sb1 = new StringBuffer();
                        int ch;
                        while ((ch = inputStream.read()) != -1) {
                            sb1.append((char) ch);
                        }

                        Log.d(TAG, URLDecoder.decode(sb1.toString(), "UTF-8"));
                    }

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject imageUpload(InputStream inputStream, String filename, long memberid) {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINEND = "\r\n";
        JSONObject result = null;
        try {
            URL url = new URL(WMCT_HEALTH_UPLOAD + memberid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(PREFIX + BOUNDARY + LINEND);
            Log.d(TAG, PREFIX + BOUNDARY + LINEND + "Content-Disposition: form-data; name=\"member_image\";"
                    + " filename=\"" + filename + "\"" + LINEND + LINEND);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"member_image\";"
                    + " filename=\"" + filename + "\"" + LINEND);
            outputStream.writeBytes(LINEND);
            int count = 0;
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buff)) != -1) {
                count += len;
                Log.d(TAG, "count = " + count);
                outputStream.write(buff, 0, len);
            }
            inputStream.close();
            //outputStream.write(LINEND.getBytes());
            outputStream.writeBytes(LINEND);
            //byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            //outputStream.write(end_data);
            outputStream.writeBytes(PREFIX + BOUNDARY + PREFIX + LINEND);
            Log.d(TAG, "outputStream.size() = " + outputStream.size());
            outputStream.flush();
            // outputStream.close();
            int responseCode = conn.getResponseCode();
            InputStream is = null;
            if (responseCode == 200) {
                is = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ch;
                while ((ch = is.read()) != -1) {
                    sb1.append((char) ch);
                }
                Log.d(TAG, URLDecoder.decode(sb1.toString(), "UTF-8"));
                try {
                    result = new JSONObject(sb1.toString());
                    Log.d(TAG, result.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void fileUpload(File file) {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINEND = "\r\n";
        try {
            URL url = new URL(WMCT_HEALTH_UPLOAD + 8);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
           /* StringBuilder sb = new StringBuilder();
            sb.append(URLEncoder.encode("memberid", "UTF-8")).append('=').append(URLEncoder.encode("7", "UTF-8"));
            Log.d(TAG, "memberid=" + sb.toString());
            outputStream.write(sb.toString().getBytes("UTF-8"));*/

            if (file.exists()) {
                String fileName = "uninstall.png";
                outputStream.writeBytes(PREFIX + BOUNDARY + LINEND);
                Log.d(TAG, PREFIX + BOUNDARY + LINEND + "Content-Disposition: form-data; name=\"member_image\";"
                        + " filename=\"" + fileName + "\"" + LINEND + LINEND);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"member_image\";"
                        + " filename=\"" + fileName + "\"" + LINEND);
                outputStream.writeBytes(LINEND);

                InputStream is = new FileInputStream(file);
                int count = 0;
                byte[] buff = new byte[1024];
                int len = -1;
                while ((len = is.read(buff)) != -1) {
                    count += len;
                    Log.d(TAG, "count = " + count);
                    outputStream.write(buff, 0, len);
                }
                is.close();
                //outputStream.write(LINEND.getBytes());
                outputStream.writeBytes(LINEND);
                //byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                //outputStream.write(end_data);
                outputStream.writeBytes(PREFIX + BOUNDARY + PREFIX + LINEND);
                Log.d(TAG, "outputStream.size() = " + outputStream.size());
                outputStream.flush();
                // outputStream.close();
                int responseCode = conn.getResponseCode();
                InputStream inputStream = null;
                if (responseCode == 200) {
                    inputStream = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ch;
                    while ((ch = inputStream.read()) != -1) {
                        sb1.append((char) ch);
                    }

                    Log.d(TAG, URLDecoder.decode(sb1.toString(), "UTF-8"));
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
