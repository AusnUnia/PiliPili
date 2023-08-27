package com.ausn.pilipili.common.constants;

public class RedisConstants
{
    static public final String LOGIN_CODE_KEY_PREFIX="user:login:verification:"; //usage:  key is login:verification:<phone number>, value is verification code like 223344
    static public final Long LOGIN_CODE_TTL=3L; //the time to live of verification code (minutes)
    static public final String LOGIN_PUSER_KEY_PREFIX="user:login:token:"; //usage:  key is login:token:<token>, value is a PUser object
    static public final Long LOGIN_PUSER_TTL=10080L; //the time to live of user login information (minutes)
    static public final String VIDEO_CACHE_KEY_PREFIX="video:info:"; //usage:  key is video:<bv>, value is a Video object
    static public final Long VIDEO_CACHE_TTL=10L; //
    static public final String VIDEO_LOCK_KEY_PREFIX="lock:video:info";//usage:  key is lock:video:<bv>, value is determined by Redisson.

    static public final String VIDEO_UPVOTE_CACHE_KEY_PREFIX="video:upvote:"; //usage: key is video:upvote:<bv>, value is a set which contains users who upvoted this video.
    static public final String VIDEO_NOVOTE_CACHE_KEY_PREFIX="video:novote:"; //usage: key is video:novote:<bv>, value is a set which contains users who have upvoted or downvoted but then canceled.
    static public final String VIDEO_DOWNVOTE_CACHE_KEY_PREFIX="video:downvote:"; //usage: key is video:downvote:<bv>, value is a set which contains users who downvoted this video.
    static public final String VIDEO_VOTE_LOCK_KEY_PREFIX="lock:video:vote:"; //usage: key is lock:video:vote:<bv>, value is determined by Redisson.
                                                                         // Used to reconstruct the cache of "video_upvote:","video_novote:" and "video_downvote:"


    static public final String VIDEO_COIN_TODAY_CACHE_KEY_PREFIX="video:coin:"; // usage: key is video:coin:<bv>, it corresponds to a hash, and fields
                                                                                // are user's ids, the value of a field is the coin number. This hash contains users who put coins today
    static public final String VIDEO_COIN_NUM_CACHE_KEY_PREFIX="video:coin_num:"; // usage: key is video:coin_num:<bv> , value is the cached coins number of the video
    static public final Long VIDEO_COIN_NUM_CACHE_TTL=3L;
    static public final String VIDEO_COIN_NUM_LOCK_KEY_PREFIX="lock:video:coin_num:";//usage: key is lock:video:coin_num:<bv>, value is determined by Redisson.
                                                                                    // Used to reconstruct the cache of "video:coin_num:"
    static public final String VIDEO_FAVORITE_CACHE_KEY_PREFIX="video:favorite:";//usage: key is video_upvote:<bv>, value is a set which contains users who upvoted this video.

    static public final String EXPIRE_KEY_AT_MIDNIGHT_LOCK="lock:scheduled:expire_key_at_midnight";
    static public final String PERSIST_DATA_LOCK="lock:scheduled:persist_data";

    static public final String REQUEST_LIMIT_KEY_PREFIX="request_limit:";
}
