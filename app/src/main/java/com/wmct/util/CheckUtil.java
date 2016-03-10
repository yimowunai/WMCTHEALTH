package com.wmct.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yimowunai on 2016/1/4.
 */
public class CheckUtil {
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        System.out.println(m.matches() + "---");

        return m.matches();

    }

    public static String checkPwd(String pwd) {

        Pattern p = Pattern.compile("^\\d+$");
        Matcher m = p.matcher(pwd);
        if (pwd.length() < 8) {
            return "密码长度小于8位";
        } else if (pwd.length() > 16) {
            return "密码长度大于16位";
        } else if (m.matches()) {
            return "密码不能全为数字";
        } else if (pwd.contains(" ")) {
            return "密码不能含有空格";
        } else {
            return "Correct";
        }
    }

    public static boolean checkMemberAge(String age) {
        Pattern p = Pattern.compile("^\\d{1,3}$");
        Matcher m = p.matcher(age);
        if (m.matches()) {
            return true;
        }
        return false;


    }
}


