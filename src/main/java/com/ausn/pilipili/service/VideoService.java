package com.ausn.pilipili.service;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.entity.Video;

import java.util.List;

public interface VideoService
{
    public Result save(Video video);
    public Result delete(Video video);
    public Result getByBv(String bv);
    public Result getByAuthorId(String authorId);
    public Result update(Video video);
    public Result getRandomly();
    public Result upvote(String bv);
    public Result downvote(String bv);
}
