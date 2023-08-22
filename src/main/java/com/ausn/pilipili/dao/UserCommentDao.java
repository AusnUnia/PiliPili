package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.UserComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCommentDao extends BaseMapper<UserComment>
{
    public int save(UserComment userComment);
    public int delete(UserComment userComment);
    public List<UserComment> getByBv(@Param("bv") String bv);
    public int update(UserComment userComment);
}
