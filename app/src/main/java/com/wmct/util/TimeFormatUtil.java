package com.wmct.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shan on 2016/1/14.
 */
public class TimeFormatUtil {


    public static String TimeTranstoString(String time){
        if(time!=""){
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
            String dateString = formatter.format(new Date(Long.parseLong(time)));
            return  dateString;}else {
            return "";
        }
    }
}
