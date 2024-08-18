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
    public static final String MEMBER_PASSWORD_CHANGE = MEMBER_ROOT_PATH + "/password/change";
    public static final String MEMBER_WITHDRAW = MEMBER_ROOT_PATH + "/withdraw";
    public static final String MEMBER_TOKEN_AUTHORIZE = MEMBER_ROOT_PATH + "/token/authorize";
    public static final String MEMBER_SEARCH = MEMBER_ROOT_PATH + "/search";

    // board
    public static final String BOARD_ROOT_PATH = "/boards";
    public static final String GET_NOTICE_BOARD = BOARD_ROOT_PATH + "/notice";
    public static final String GET_NOTICE_BOARD_PAGE = BOARD_ROOT_PATH + "/notice/page";
    public static final String GET_BOARD_POST_DETAIL = BOARD_ROOT_PATH + "/post/{postId}";
    public static final String WRITE_NOTICE_POST = BOARD_ROOT_PATH + "/notice/write";
    public static final String EDIT_NOTICE_POST = BOARD_ROOT_PATH + "/notice/edit/{postId}";

    // chat
    public static final String CHAT_ROOT_PATH = "/chat";
    public static final String CHAT_ROOM_ROOT_PATH = CHAT_ROOT_PATH + "/rooms";
    public static final String CHAT_ROOM_PUBLIC = CHAT_ROOM_ROOT_PATH + "/public";
    public static final String CHAT_ROOM_SEARCH_PUBLIC = CHAT_ROOM_ROOT_PATH + "/search/public";
    public static final String CHAT_ROOM_CREATE = CHAT_ROOM_ROOT_PATH + "/create";
    public static final String CHAT_ROOM_JOIN = CHAT_ROOM_ROOT_PATH + "/join/{roomId}";
    public static final String CHAT_ROOM_PREVIOUS_MESSAGES = CHAT_ROOM_ROOT_PATH + "/previous-messages"; // 채팅방 현재 이전 메시지 조회 (previous message)
    public static final String CHAT_ROOM_EXIT = CHAT_ROOM_ROOT_PATH + "/exit/{roomId}";
    public static final String CHAT_ROOM_INVITE = CHAT_ROOM_ROOT_PATH + "/invite/{roomId}";

}
