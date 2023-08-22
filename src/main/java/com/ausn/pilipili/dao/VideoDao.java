package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface VideoDao extends BaseMapper<Video>
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
    public int setUpvoteNumByBv(@Param("bv") String bv,@Param("upvoteNum") int upvoteNum);
    public int updateDownvoteNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int setDownvoteNumByBv(@Param("bv") String bv,@Param("downvoteNum") int downvoteNum);
    public int updateCoinNumByBv(@Param("bv") String bv,@Param("num") int num);
    public Long getCoinNumByBv(@Param("bv") String bv);
    public int updateSaveNumByBv(@Param("bv") String bv,@Param("num") int num);
    public int updateShareNumByBv(@Param("bv") String bv,@Param("num") int num);
    public List<String> getBvByPage(@Param("offset") int offset,@Param("pageSize") int pageSize);
}
