package com.wmct.util;

import com.wmct.health.R;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p/>
 * 作者 ：三月半
 * <p/>
 * 时间 ：2016/1/18 14:23
 * <p/>
 * --------------------------------------
 */
public class GenderConversion {
    public final static int CHECK_MAN = 0;
    public final static int CHECK_WOMAN = 1;
    public final static int CHECK_SECRET = 2;

    public static String toString(int gender) {

        if (gender == CHECK_MAN) {
            return "男";
        } else if (gender == CHECK_WOMAN) {
            return "女";
        } else {
            return "保密";
        }
    }

}
