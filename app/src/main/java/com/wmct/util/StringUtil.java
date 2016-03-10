package com.wmct.util;

import android.util.Log;

import com.wmct.health.R;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
    private static final String TAG = "StringUtil";
    public static final String Host = "http://58.194.170.16/body";

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }

    public static long getToday() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String curDate = format.format(time);
        Log.d(TAG, curDate);

        curDate.replace("-", "");
        Log.d(TAG, curDate);
        return Long.parseLong(curDate);
    }


    public static int getCurrentTime() {
        int time = (int) (System.currentTimeMillis() / 1000);
        return time;
    }

    public static String TimeTranstoString(String time) {
        if (time != "") {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
            String dateString = formatter.format(new Date(Long.parseLong(time)));
            return dateString;
        } else {
            return "";
        }
    }

    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                return false;
            }

        }
        return true;
    }

    public static String rewriteImageUrl(String image) {
        String url = "";
        String[] sourcePath = image.split("\\/");
        if (sourcePath != null && sourcePath.length > 0) {
            url = Host;
        }

        for (int j = 1; j < sourcePath.length; j++) {
            Log.d(TAG, sourcePath[j]);
            url += "/" + sourcePath[j];
        }
        Log.d(TAG, "url=" + url);
        return url;
    }

    public static int ENChangeToCH(String msg) {
        Log.i(TAG, "msg:------------------>" + msg);
        if (msg.equals("") || msg == null) {
            return R.string.serverbreak;
        }

        int resourceId = 0;
        switch (msg) {
            case "nullphone":
                resourceId = R.string.login_nullphone;
                break;
            case "nullpassword":
                resourceId = R.string.login_nullpassword;
                break;
            case "nouser":
                resourceId = R.string.login_nouser;
                break;
            case "queryerror":
                resourceId = R.string.login_queryerror;
                break;
            case "nullverificationcode":
                resourceId = R.string.register_check_nullverificationcode;
                break;
            case "nodata":
                resourceId = R.string.register_check_nodata;
                break;
            case "submitfailed":
                resourceId = R.string.register_check_submitfailed;
                break;
            case "illegalip":
                resourceId = R.string.register_check_illegalip;
                break;
            case "blackphone":
                resourceId = R.string.register_check_blackphone;
                break;
            case "illegalphone":
                resourceId = R.string.register_check_illegalphone;
                break;
            case "alreadyreg":
                resourceId = R.string.register_check_alreadyreg;
                break;
            case "sendcodeok":
                resourceId = R.string.register_check_sendcodeok;
                break;

            case "nullname":
                resourceId = R.string.register_nullname;
                break;
            case "phoneexist":
                resourceId = R.string.register_phoneexist;
                break;
            case "nameexist":
                resourceId = R.string.register_nameexis;
                break;
            case "havespace":
                resourceId = R.string.register_havespace;
                break;
            case "shortpassword":
                resourceId = R.string.register_shortpassword;
                break;
            case "registerok":
                resourceId = R.string.register_registerok;
                break;
            case "nullresetpasswd":
                resourceId = R.string.reset_nullresetpasswd;
                break;
            case "resetpasswdok":
                resourceId = R.string.reset_passwdok;
                break;
            case "resetpasswderror":
                resourceId = R.string.reset_passwderror;
                break;
            case "nothisuser":
                resourceId = R.string.reset_check_nothisuser;
                break;
            case "memberexist":
                resourceId = R.string.add_memberexist;
                break;
            case "nullfamilyphone":
                resourceId = R.string.add_nullfamilyphone;
                break;
            case "nullmembername":
                resourceId = R.string.add_nullmembername;
                break;
            case "nullage":
                resourceId = R.string.add_nullage;
                break;
            case "nullgender":
                resourceId = R.string.add_nullgender;
                break;
            case "addmemberok":
                resourceId = R.string.add_addmemberok;
                break;
            case "updateok":
                resourceId = R.string.update_member_success;
                break;
            case "uploadok":
                resourceId = R.string.upload_member_image_ok;
                break;


        }
        return resourceId;
    }
}
