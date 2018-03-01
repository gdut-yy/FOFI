package net.fofi.app.improve.utils.parser;

import java.util.regex.Pattern;

/**
 * 富文本解析工具
 * Created by ZYY on 2018/2/27.
 */

public abstract class RichTextParser {
    /**
     * 判断手机输入合法
     *
     * @param phoneNumber 手机号码
     * @return true|false
     */
    public static boolean machPhoneNum(CharSequence phoneNumber) {
        String regex = "^[1][34578][0-9]\\d{8}$";
        return Pattern.matches(regex, phoneNumber);
    }
}
