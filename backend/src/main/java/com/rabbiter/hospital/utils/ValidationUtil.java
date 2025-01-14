package com.rabbiter.hospital.utils;

import java.util.regex.Pattern;

/**
 * 输入验证工具类
 */
public class ValidationUtil {
    
    /**
     * 验证手机号码
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        String regex = "^1[3-9]\\d{9}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证邮箱
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号
     */
    public static boolean isValidIdCard(String idCard) {
        if (idCard == null || (idCard.length() != 15 && idCard.length() != 18)) {
            return false;
        }
        String regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                      "(^[1-9]\\d{5}\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证密码强度
     * 至少包含一个大写字母、一个小写字母、一个数字，长度在6-20之间
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 20) {
            return false;
        }
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,20}$";
        return Pattern.matches(regex, password);
    }

    /**
     * 验证姓名
     * 2-20个字符，只能包含中文、英文字母
     */
    public static boolean isValidName(String name) {
        if (name == null || name.length() < 2 || name.length() > 20) {
            return false;
        }
        String regex = "^[\\u4e00-\\u9fa5a-zA-Z]{2,20}$";
        return Pattern.matches(regex, name);
    }
} 