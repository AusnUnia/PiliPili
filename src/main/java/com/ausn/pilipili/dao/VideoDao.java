package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoDao
{
    public int save(Video video);
    public int delete(Video video);
    public Video getByBv(@Param("bv") String bv);
    public Video getByAuthorId(@Param("authorId")String authorId);
    public int update(Video video);
    public List<Video> getRandomly();

}