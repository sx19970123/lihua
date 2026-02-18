package com.lihua.utils.string;

public class StringUtils {

    /**
     * 字符串转首字母大写
     */
    public static String initialUpperCase(String str) {
        if (!org.springframework.util.StringUtils.hasText(str)) {
            return str;
        }

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 字符串首字母小写
     */
    public static String initialLowerCase(String str) {
        if (!org.springframework.util.StringUtils.hasText(str)) {
            return str;
        }

        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

}
