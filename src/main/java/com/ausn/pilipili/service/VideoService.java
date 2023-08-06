package com.ausn.pilipili.service;

import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.entity.Video;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * @Author: 付显贵
 * @DateTime: 2023/4/23 0:07
 * @Description:
 */

public interface VideoService
{
    public Result uploadWhole(MultipartHttpServletRequest request);
    public Result upload(MultipartHttpServletRequest request);
    public Result delete(Video video);
    public Result getByBv(String bv);
    public Result getByAuthorId(String authorId);
    public Result getRandomly();
    public Result upvote(String bv);
    public Result downvote(String bv);
    public Result coin(String bv, int num);
    public Result favorite(String bv);
    public Result getUpvoteNumByBv(String bv);
    public Result getCoinNumByBv(String bv);
}
