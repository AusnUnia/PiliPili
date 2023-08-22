package com.ausn.pilipili.service;

import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.entity.requestEntity.CommentPublishRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserCommentService extends IService<UserComment>
{
    public Result publish(CommentPublishRequest commentPublishRequest);
    public boolean delete(UserComment userComment);
    public List<UserComment> getByBv(String bv);
    public boolean update(UserComment userComment);
}
