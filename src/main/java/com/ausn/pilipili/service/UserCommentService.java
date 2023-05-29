package com.ausn.pilipili.service;

import com.ausn.pilipili.entity.UserComment;

import java.util.List;

public interface UserCommentService
{
    public boolean publish(UserComment userComment);
    public boolean delete(UserComment userComment);
    public List<UserComment> getByBv(String bv);
    public boolean update(UserComment userComment);
}
