package com.hs.chat.global.util;

import com.hs.chat.global.enums.UserType;

public class AuthorityUtils {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";

    public static String getAdminRoleName() {
        return ADMIN;
    }

    public static String getUserRoleName() {
        return USER;
    }

    public static String getRoleName(UserType userType) {
        if (userType == UserType.ADMIN) {
            return getAdminRoleName();
        } else {
            return getUserRoleName();
        }
    }

}

