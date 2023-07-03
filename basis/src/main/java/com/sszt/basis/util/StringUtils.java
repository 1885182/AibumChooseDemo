package com.sszt.basis.util;

import java.lang.reflect.Field;

/**
 * String 工具类
 */
public class StringUtils {

    public static Object getBuildConfigValue( String fieldName) {
        try {
            Class<?> clazz = Class.forName("com.sszt.grassrootsgovernance.BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StringUtils INSTANCE;

    public static StringUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (StringUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StringUtils();
                }
            }
        }
        return INSTANCE;
    }

    public String replaceAll(String str, String regex, String replacement) {

        return str.replaceAll(regex, replacement);


    }


    //把一个字符串中的大写转为小写
    public String exChangeToSmall(String str) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    //小写转换为大写
    public String exChangeToBig(String str) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

}
