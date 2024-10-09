package com.travel.admin.constant;

/**
 * User-related constantsUser-related constants
 */
public interface UserConstant {
    /**
     * Administrator roles
     */
    int ADMIN_ROLE = 0;

    /**
     * Merchant Role
     */
    int BUSINESS_ROLE = 1;

    /**
     * Customer Role
     */
    int CUSTOM_ROLE = 2;

    /**
     * Default Avatar
     */
    String DEFAULT_IMAGE = "/api/image/a1.jpg";

    /**
     * User login key
     */
    String USER_LOGIN_KEY = "travel-admin-api:user:login:";

    /**
     * User login error count key
     */
    String USER_LOGIN_ERROR_COUNT_KEY = "travel-admin-api:user:login_error_count:";

    /**
     * User registration verification code key
     */
    String USER_REGISTER_CODE = "travel-admin-api:user:register_code:";
}
