package com.ausn.pilipili.service;

import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface VideoService
{
    public Result upload(MultipartHttpServletRequest request);
    public Result delete(Video video);
    public Result getByBv(String bv);
    public Result getByAuthorId(String authorId);
    public Result update(Video video);
    public Result getRandomly();
    public Result upvote(String bv);
    public Result downvote(String bv);
    public Result coin(String bv, int num);
    public Result favorite(String bv);
}
