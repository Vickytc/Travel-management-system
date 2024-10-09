package com.travel.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.travel.admin.constant.RegexConstant;

/**
 * Regular expression validation utility class
 */
public class RegexUtil {
    /**
     * Checks if the phone format is invalid
     * @param phone the phone number to validate
     * @return true: valid, false: invalid
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexConstant.PHONE_REGEX);
    }

    /**
     * Checks if the username format is invalid
     * @param userName the username to validate
     * @return true: valid, false: invalid
     */
    public static boolean isUserNameInvalid(String userName) {
        return mismatch(userName, RegexConstant.USERNAME_REGEX);
    }

    /**
     * Checks if the email format is invalid
     * @param email the email to validate
     * @return true: valid, false: invalid
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexConstant.EMAIL_REGEX);
    }

    /**
     * Checks if the QQ format is invalid
     * @param qq the QQ number to validate
     * @return true: valid, false: invalid
     */
    public static boolean isQQInvalid(String qq) {
        return mismatch(qq, RegexConstant.QQ_REGEX);
    }

    /**
     * Checks if the password format is invalid
     * @param password the password to validate
     * @return true: valid, false: invalid
     */
    public static boolean isPasswordInvalid(String password) {
        return mismatch(password, RegexConstant.PASSWORD_REGEX);
    }

    /**
     * Checks if the verification code format is invalid
     * @param code the verification code to validate
     * @return true: valid, false: invalid
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexConstant.VERIFY_CODE_REGEX);
    }

    // Checks if it does not match the regex format
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}

