package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.VideoVote;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoVoteDao
{
    public int save(VideoVote videoVote);
    public int delete(String bv,String userId);
    public VideoVote getByBvUserId(String bv,Long userId);
    public int update(VideoVote videoVote);
}
