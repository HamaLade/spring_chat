package com.hs.presentation;

/**
 * API 경로 정의
 */
public class ApiPaths {

    // index
    public static final String HOME = "/";

    // member
    public static final String MEMBER_ROOT_PATH = "/members";
    public static final String MEMBER_LOGIN = MEMBER_ROOT_PATH + "/login";
    public static final String MEMBER_SIGNUP = MEMBER_ROOT_PATH + "/signup";
    public static final String MEMBER_LOGOUT = MEMBER_ROOT_PATH + "/logout";
    public static final String MEMBER_WITHDRAW = MEMBER_ROOT_PATH + "/withdraw";

    // board
    public static final String GET_NOTICE_BOARD = "/boards/notice";
    public static final String GET_NOTICE_BOARD_PAGE = "/boards/notice/page";
    public static final String GET_BOARD_POST_DETAIL = "/boards/post/{postId}";

}
