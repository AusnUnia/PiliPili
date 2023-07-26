package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface VideoDao
{
    public int save(Video video) throws SQLException;
    public int delete(Video video);
    public Video getByBv(@Param("bv") String bv);
    public List<Video> getByAuthorId(@Param("authorId")String authorId);
    public int update(Video video);
    public List<Video> getRandomly();
    public int updateViewNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateBulletScreenNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateCommentNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateUpvoteNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateDownvoteNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateCoinNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateSaveNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateShareNumByBv(@Param("bv") String bv,@Param("num") int num);
}
