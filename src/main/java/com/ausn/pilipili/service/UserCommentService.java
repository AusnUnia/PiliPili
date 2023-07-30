package com.ausn.pilipili.service;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.entity.requestEntity.CommentPublishRequest;

import java.util.List;

public interface UserCommentService
{
    public Result publish(CommentPublishRequest commentPublishRequest);
    public boolean delete(UserComment userComment);
    public List<UserComment> getByBv(String bv);
    public boolean update(UserComment userComment);
}
