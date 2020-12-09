package com.xiongtian.miaosha.util;

import java.util.Random;
import java.util.UUID;

/**
 * @Author xiongtian
 * @Date 2020/12/3 20:46
 * @Version 1.0
 */

public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
