package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.VideoVote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VideoVoteDao
{
    public int save(VideoVote videoVote);
    public int delete(@Param("bv") String bv, @Param("userId")Long userId);
    public VideoVote getByBvUserId(@Param("bv") String bv, @Param("userId")Long userId);
    public int update(VideoVote videoVote);
}
