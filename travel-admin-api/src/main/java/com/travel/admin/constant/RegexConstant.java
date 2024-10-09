package com.travel.admin.constant;

/**
 * Regular constant expressions
 */
public interface RegexConstant {
    /**
     * Mobile phone number regular expression
     */
    String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * Username regular expression
     */
    String USERNAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,10}$";
    /**
     * Email regular expression
     */
    String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * QQ number regular expression
     */
    String QQ_REGEX = "^[1-9][0-9]{4,10}$";
    /**
     * Password regular expression. 6 to 20 characters of letters, numbers, underscores
     */
    String PASSWORD_REGEX = "^[a-zA-Z0-9_.-=*&^%$#@!+]{6,20}$";
    /**
     * Verification code regular expression, 6 digits
     */
    String VERIFY_CODE_REGEX = "^[\\d]{6}$";

}
