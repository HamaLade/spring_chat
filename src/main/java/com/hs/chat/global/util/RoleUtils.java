package com.hs.chat.global.util;

import com.hs.chat.global.enums.UserType;

public class RoleUtils {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static String getRoleName(String role) {
        return ROLE_PREFIX + role;
    }

    public static String getRoleName(UserType userType) {
        return ROLE_PREFIX + userType.name();
    }

    public static String getAdminRoleName() {
        return ROLE_ADMIN;
    }

    public static String getUserRoleName() {
        return ROLE_USER;
    }

}

