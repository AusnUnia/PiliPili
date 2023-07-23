package com.ausn.pilipili.utils.constants;

public class RedisConstants
{
    static public final String LOGIN_CODE_KEY_PREFIX="login:verification:";
    static public final Long LOGIN_CODE_TTL=3L; //the time to live of verification code (minutes)
    static public final String LOGIN_PUSER_KEY_PREFIX="login:token:";
    static public final Long LOGIN_PUSER_TTL=20L; //the time to live of user login information (minutes)
    static public final String VIDEO_CACHE_KEY_PREFIX="video:";
    static public final Long VIDEO_CACHE_TTL=10L; //
    static public final String VIDEO_LOCK_KEY_PREFIX="lock:video:";
}
