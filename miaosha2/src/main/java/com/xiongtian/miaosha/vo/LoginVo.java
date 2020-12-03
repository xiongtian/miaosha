package com.xiongtian.miaosha.vo;

/**
 * @Author xiongtian
 * @Date 2020/12/2 21:07
 * @Version 1.0
 */

public class LoginVo {
    private String mobile;
    private String password;

    public LoginVo() {
    }

    public LoginVo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
