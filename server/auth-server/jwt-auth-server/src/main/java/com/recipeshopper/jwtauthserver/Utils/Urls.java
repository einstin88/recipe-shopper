package com.recipeshopper.jwtauthserver.Utils;

public class Urls {
    public static final String URL_PREFIX_AUTH = "/auth";
    public static final String URL_HEALTH = "/health";
    public static final String URL_SIGN_IN_BASIC = "/sign-in/basic";
    public static final String URL_SIGN_IN_DEFAULT = "/sign-in/default";
    public static final String URL_REGISTER = "/registration";
    public static final String URL_AUTHENTICATED = "/authenticated";
    public static final String URL_LOG_OUT = "/signout";
    
    public static final String EP_HEALTH = URL_PREFIX_AUTH + URL_HEALTH;
    public static final String EP_SIGN_IN_BASIC = URL_PREFIX_AUTH + URL_SIGN_IN_BASIC;
    public static final String EP_SIGN_IN_DEFAULT = URL_PREFIX_AUTH + URL_SIGN_IN_DEFAULT;
    public static final String EP_REGISTER = URL_PREFIX_AUTH + URL_REGISTER;
    public static final String EP_AUTHENTICATED = URL_PREFIX_AUTH + URL_AUTHENTICATED;
    public static final String EP_LOG_OUT = URL_PREFIX_AUTH + URL_LOG_OUT;

}
