package com.tzhen.commen.utils;

/**
 * Created by wuyong on 2016/12/15.
 */
public class StringUtils {
    public static final String USERNAME_REG = "[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    public static final String PASSWORD_REG = "[0-9a-zA-Z]{6,16}";

    public static boolean isValidUsername(String username) {
        return username.matches(USERNAME_REG);
    }

    public static boolean isValidPwd(String password) {
        return password.matches(PASSWORD_REG);
    }
}
