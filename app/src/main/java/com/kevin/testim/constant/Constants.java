package com.kevin.testim.constant;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class Constants {
    public static final String ARGS = "fragment_args";

    public static final int STATE_LOGIN_SUCCESSFULLY = 1001;
    public static final int STATE_LOGIN_REQUESTING = 1002;
    public static final int STATE_LOGIN_FAILURE = 1003;
    public static final int STATE_NET_CONNECTED = 1004;
    public static final int STATE_NET_DISCONNECTED = 1005;

    public static final int CHAT_LIST_REFRESH = 1020;
    /**
     * 类型为：接收
     */
    public static final int CHAT_TYPE_RECEIVE=1021;
    /**
     * 类型为：发送
     */
    public static final int CHAT_TYPE_SEND=1022;

    public static final int MESSAGE_RECEIVED = 1023;
    public static final int MEESAGE_RECALLED = 1024;


    public static final String USERNAME = "current_username";

    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";

}
