package com.ausn.pilipili.dao;

import com.ausn.pilipili.entity.PUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PUserDao extends BaseMapper<PUser>
{
    public int save(PUser pUser);
    public int delete(PUser pUser);
    public PUser getByUid(@Param("uid") Long uid);
    public PUser getByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    public int update(PUser pUser);
    public Long getLastInsertedId();
}
