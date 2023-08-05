package com.ausn.pilipili.utils.constants;

public class RedisConstants
{
    static public final String LOGIN_CODE_KEY_PREFIX="login:verification:"; //usage:  key is login:verification:<phone number>, value is verification code like 223344
    static public final Long LOGIN_CODE_TTL=3L; //the time to live of verification code (minutes)
    static public final String LOGIN_PUSER_KEY_PREFIX="login:token:"; //usage:  key is login:token:<token>, value is a PUser object
    static public final Long LOGIN_PUSER_TTL=10080L; //the time to live of user login information (minutes)
    static public final String VIDEO_CACHE_KEY_PREFIX="video:"; //usage:  key is video:<bv>, value is a Video object
    static public final Long VIDEO_CACHE_TTL=10L; //
    static public final String VIDEO_LOCK_KEY_PREFIX="lock:video:";//usage:  key is lock:video:<bv>, value is determined by Redisson.

    static public final String VIDEO_UPVOTE_CACHE_KEY_PREFIX="video_upvote:"; //usage: key is video_upvote:<bv>, value is a set which contains users who upvoted this video.
    static public final String VIDEO_NOVOTE_CACHE_KEY_PREFIX="video_novote:"; //usage: key is video_novote:<bv>, value is a set which contains users who have upvoted or downvoted but then canceled.
    static public final String VIDEO_DOWNVOTE_CACHE_KEY_PREFIX="video_downvote:"; //usage: key is video_downvote:<bv>, value is a set which contains users who downvoted this video.
    static public final String VIDEO_VOTE_LOCK_KEY_PREFIX="lock:video_vote:"; //usage: key is lock:video_vote:<bv>, value is determined by Redisson.
                                                                         // Used to reconstruct the cache of "video_upvote:","video_novote:" and "video_downvote:"


    static public final String VIDEO_COIN_TODAY_CACHE_KEY_PREFIX="video_coin:"; // usage: key is video_coin:<bv>, it corresponds to a hash, and fields
                                                                                // are user's ids, the value of a field is the coin number. This hash contains users who put coins today
    static public final String VIDEO_COIN_NUM_CACHE_KEY_PREFIX="video_coin_num:"; // usage: key is video_coin_num:<bv> , value is the cached coins number of the video
    static public final Long VIDEO_COIN_NUM_CACHE_TTL=3L;
    static public final String VIDEO_COIN_NUM_LOCK_KEY_PREFIX="lock:video_coin_num:";//usage: key is lock:video_coin_num:<bv>, value is determined by Redisson.
                                                                                    // Used to reconstruct the cache of "video_coin_num:"
    static public final String VIDEO_FAVORITE_CACHE_KEY_PREFIX="video_favorite:";//usage: key is video_upvote:<bv>, value is a set which contains users who upvoted this video.
}
