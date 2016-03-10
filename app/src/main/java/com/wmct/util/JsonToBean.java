package com.wmct.util;

import android.util.Log;

import com.wmct.bean.Family;
import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/7 16:41
 * <p/>
 * --------------------------------------
 */
public class JsonToBean {
    public static final String Host = "http://58.194.170.16/body";
    private static final String TAG = "JsonToBean";

    public static boolean isOK(JSONObject jsonObject) {
        int status = 0;
        try {
            status = jsonObject.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status == 1;
    }

    public static String getUrl(JSONObject jsonObject) {
        String url = "";
        try {
            url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getMsg(JSONObject jsonObject) {
        String msg = "";
        try {
            msg = jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static List<Member> toMemberList(JSONObject jsonObject) {
        List<Member> list = new ArrayList<Member>();
        try {
            JSONArray array = jsonObject.getJSONArray("members");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Member member = new Member();
                long id = object.getLong("id");
                String familyPhone = object.getString("familyphone");
                String memberName = object.getString("membername");
                int age = object.getInt("age");
                int gender = object.getInt("gender");
                String image = object.getString("image");
                if (!image.equals("") && image != null) {
                    String[] sourcePath = image.split("\\/");
                    image = Host;
                    for (int j = 1; j < sourcePath.length; j++) {
                        Log.d(TAG, sourcePath[j]);
                        image += "/" + sourcePath[j];
                    }
                    Log.d(TAG, "image=" + image);

                }
                member.setMemberid(id);
                if (!"".equals(familyPhone) && familyPhone != null) {
                    member.setFamilyPhone(familyPhone);
                } else {
                    member.setFamilyPhone("");
                }
                if (!"".equals(image) && image != null) {
                    member.setImage(image);
                    Log.d(TAG, " member.setImage(image)=" + member.getImage());
                } else {
                    member.setImage("");
                }
                if (!"".equals(memberName) && memberName != null) {
                    member.setMembername(memberName);
                } else {
                    member.setMembername("");
                }
                member.setGender(gender);
                member.setAge(age);
                list.add(member);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Member toMember(JSONObject jsonObject) {
        Member member = new Member();
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            long memberid = data.getInt("id");
            String familyPhone = data.getString("familyphone");
            String membername = data.getString("membername");
            int age = data.getInt("age");
            int gender = data.getInt("gender");
            String image = data.getString("image");
            member.setMemberid(memberid);
            member.setAge(age);
            member.setFamilyPhone(familyPhone);
            member.setGender(gender);
            member.setImage(image);
            member.setMembername(membername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return member;
    }

    public static JSONArray toMeasureDataJsonarray(List<MeasureData> list) {
        JSONArray jsonArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonMeasureData = new JSONObject();
                MeasureData measureData = list.get(i);
                try {
                    jsonMeasureData.put("memberid", measureData.getMemberId() + "");
                    jsonMeasureData.put("diastolicpressure", measureData.getDiastolicpressure() + "");
                    jsonMeasureData.put("systolicpressure", measureData.getSystolicpressure() + "");
                    jsonMeasureData.put("heartrate", measureData.getHeartrate() + "");
                    jsonMeasureData.put("heartstatus", measureData.getHeartstate() + "");
                    jsonMeasureData.put("measuretime", measureData.getMeasuretime() + "");
                    jsonArray.put(jsonMeasureData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }

    public static JSONArray toVisitorMeasureDataJsonarray(List<MeasureData> list) {
        JSONArray jsonArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonMeasureData = new JSONObject();
                MeasureData measureData = list.get(i);
                try {
                    jsonMeasureData.put("terminalid", measureData.getTerminalid() + "");
                    jsonMeasureData.put("diastolicpressure", measureData.getDiastolicpressure() + "");
                    jsonMeasureData.put("systolicpressure", measureData.getSystolicpressure() + "");
                    jsonMeasureData.put("heartrate", measureData.getHeartrate() + "");
                    jsonMeasureData.put("heartstatus", measureData.getHeartstate() + "");
                    jsonMeasureData.put("measuretime", measureData.getMeasuretime() + "");
                    jsonArray.put(jsonMeasureData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }

    public static List<MeasureData> toMeasureDataList(JSONObject jsonObject) {
        List<MeasureData> list = new ArrayList<MeasureData>();
        try {
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                MeasureData data = new MeasureData();
                long memberId = object.getLong("memberid");
                int diastolicpressure = object.getInt("diastolicpressure");
                int systolicpressure = object.getInt("systolicpressure");
                int heartrate = object.getInt("heartrate");
                int heartstate = object.getInt("heartstate");
                int measuretime = object.getInt("measuretime");
                data.setMemberId(memberId);
                data.setDiastolicpressure(diastolicpressure);
                data.setSystolicpressure(systolicpressure);
                data.setHeartstate(heartstate);
                data.setHeartrate(heartrate);
                data.setMeasuretime(measuretime);
                list.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static MeasureData toMeasureData(JSONObject jsonObject) {
        MeasureData data = new MeasureData();
        int diastolicpressure = 0;
        int systolicpressure = 0;
        int heartrate = 0;
        int heartstate = 0;
        int currenttime = 0;
        int measuretime = 0;
        try {
            JSONObject object = jsonObject.getJSONObject("data");
            diastolicpressure = object.getInt("diastolicpressure");
            systolicpressure = object.getInt("systolicpressure");
            heartrate = object.getInt("heartrate");
            heartstate = object.getInt("heartstate");
            currenttime = object.getInt("currenttime");
            measuretime = object.getInt("measuretime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data.setDiastolicpressure(diastolicpressure);
        data.setSystolicpressure(systolicpressure);
        data.setHeartstate(heartstate);
        data.setHeartrate(heartrate);
        data.setMeasuretime(measuretime);
        data.setUploadtime(currenttime);
        return data;
    }

}
