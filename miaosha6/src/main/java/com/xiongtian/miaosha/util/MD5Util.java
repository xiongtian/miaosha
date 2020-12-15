package com.xiongtian.miaosha.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author xiongtian
 * @Date 2020/12/2 20:32
 * @Version 1.0
 */

public class MD5Util {

    /**
     * MD5加密
     * @param src
     * @return
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 传输的加密
     * @param inputPass
     * @return
     */
    public static String inputPassFormPass(String inputPass){

        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 入库的加密
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass,String salt){

        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 直接将输入的密码转换换为存入数据库的密码
     * @param inputPass
     * @param salt
     * @return
     */
    public static String inputPassToDBPass(String inputPass,String salt){
        return formPassToDBPass(inputPassFormPass(inputPass),salt);
    }
    public static void main(String[] args) {
        System.out.println(inputPassFormPass("123456")); // d3b1294a61a07da9b49b6e22b2cbd7f9 --> 12123456c3
        System.out.println(formPassToDBPass(inputPassFormPass("123456"),"1a2b3c4d")); // b7797cce01b4b131b433b6acf4add449 --> 12123456c3

        System.out.println(inputPassToDBPass("123456",salt));//b7797cce01b4b131b433b6acf4add449
    }

}
