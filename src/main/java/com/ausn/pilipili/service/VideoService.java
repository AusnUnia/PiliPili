package com.ausn.pilipili.service;

import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @Author: 付显贵
 * @DateTime: 2023/4/23 0:07
 * @Description:
 */

public interface VideoService extends IService<Video>
{
    public void handleChunk(MultipartFile chunkFile, Long seqNum, String videoId);
    public void mergeChunks(String bv, Long chunkSize, Long totalSize, String videoId) throws IOException, InterruptedException;
    public Result delete(Video video);
    public Video getByBv(String bv);
    public Result getByAuthorId(String authorId);
    public Result getRandomly();
    public Long upvote(String bv);
    public Result downvote(String bv);
    public Result coin(String bv, int num);
    public Result favorite(String bv);
    public Long getUpvoteNumByBv(String bv);
    public Result getCoinNumByBv(String bv);
    public String createNewVideoAndSave(VideoUploadRequest videoUploadRequest);
}
