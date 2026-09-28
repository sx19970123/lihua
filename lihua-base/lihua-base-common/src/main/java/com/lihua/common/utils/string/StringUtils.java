package com.lihua.common.utils.string;

public class StringUtils extends org.springframework.util.StringUtils {

    /**
     * 去除字符串首尾空白，空白串统一转为 null。
     * <p>与 commons-lang3 StringUtils.trimToNull 同义，但该项目未引入该依赖
     * （父类 trimWhitespace 空串不转 null，语义不符），故本地实现
     */
    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 字符串转首字母大写
     */
    public static String initialUpperCase(String str) {
        if (!hasText(str)) {
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
        if (!hasText(str)) {
            return str;
        }

        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

}
