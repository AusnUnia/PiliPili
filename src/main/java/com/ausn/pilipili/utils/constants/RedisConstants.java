package com.ausn.pilipili.utils.constants;

public class RedisConstants
{
    static public final String LOGIN_CODE_KEY_PREFIX="login:verification:";
    static public final Long LOGIN_CODE_TTL=3L; //the time to live of verification code (minutes)
    static public final String LOGIN_PUSER_KEY_PREFIX="login:token:";
    static public final Long LOGIN_PUSER_TTL=20L; //the time to live of user login information (minutes)
}
