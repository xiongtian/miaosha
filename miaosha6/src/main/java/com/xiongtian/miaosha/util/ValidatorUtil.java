package com.xiongtian.miaosha.util;

/**
 * @Author xiongtian
 * @Date 2020/12/2 22:19
 * @Version 1.0
 */


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断手机格式
 */
public class ValidatorUtil {


    private static final Pattern mobile_pattern =Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m =mobile_pattern.matcher(src);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("12345678901"));
    }
}
