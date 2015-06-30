package com.miaotu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author sunfuyong
 */
public class StringUtil {


    /**
     * 判断字符串是否为手机号码
     *
     * @param str
     * @return
     */
    public static boolean isPhoneNumber(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("^(13|15|18|17|14|00)[0-9]{9}$");
    }

    /**
     * 判断字符串去除空格,回车，制表符以及emoji表情后是否为空串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null || str.equals("") || str.trim().equals("")||trimAll(str).equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 替换字符串中的换行符
     *
     * @param src
     * @return
     */
    public static String insteadChangeLine(String src) {
        if (isEmpty(src)) {
            return null;
        } else {
            return src.replace("\r\n", "\n");
        }
    }

    /**
     * 待验证的字符串
     *
     * @return 如果是符合邮箱格式的字符串, 返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private static boolean isNotEmojiCharacter(char codePoint)
    {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source)
    {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++)
        {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint))
            {
                buf.append(codePoint);
            }
        }
        return buf.toString();
    }

    /**
     *  检测是否有emoji表情
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;

    }

    /**
     * 判断是否是Emoji
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤字符串中所有的空格，回车，制表符，emoji表情
     * @param str
     * @return
     */
    public static String trimAll(String str){
        return filterEmoji(str.replaceAll("\\s*", ""));
    }
}
