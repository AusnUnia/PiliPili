package com.ausn.pilipili.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.ausn.pilipili.common.Result;
import com.ausn.pilipili.common.ResultCode;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@RestController
@RequestMapping("/videos")
public class VideoController
{
    @Autowired
    private VideoService videoService;

    /*
    upload a video
     */
    @PostMapping("/upload")
    @Transactional
    public Result upload(MultipartHttpServletRequest request)
    {
        //parse the information about the chunk file
        MultipartFile chunkFile = request.getFile("chunk");
        String videoId=request.getParameter("videoId");
        Long chunkSize=Long.valueOf(request.getParameter("chunkSize"));
        Long totalSize=Long.valueOf(request.getParameter("totalSize"));

        //save the chunk file to temporary folder
        if(chunkFile!=null&&!chunkFile.isEmpty())
        {
            Long seqNum=Long.valueOf(request.getParameter("seqNum"));
            videoService.handleChunk(chunkFile,seqNum,videoId);
            return Result.ok(ResultCode.SAVE_OK,"succeeded to save chunk");
        }

        //the chunk file is null, this means this is the last request for uploading the video.
        //parse the VideoUploadRequest which contains the title, description and tags of the uploading video
        String jsonStr = request.getParameter("videoUploadRequest");
        if(jsonStr==null||jsonStr.isEmpty())
        {
            return Result.fail("no video information! you should upload video information!");
        }
        VideoUploadRequest videoUploadRequest= JSON.parseObject(jsonStr,VideoUploadRequest.class);
        if(StrUtil.isBlank(videoUploadRequest.getTitle()))
        {
            return Result.fail("the title can't be blank!");
        }

        //create a new video and save into mysql, meanwhile add into the bloom filter
        String bv = videoService.createNewVideoAndSave(videoUploadRequest);
        if(bv==null)
        {
            return Result.fail(ResultCode.SAVE_ERR,"fail to save the video information to mysql!");
        }

        //merge all chunks into a video file
        try
        {
            videoService.mergeChunks(bv,chunkSize,totalSize,videoId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return Result.ok(ResultCode.SAVE_OK);
    }


    /*
    request for the information of a video
     */
    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
        Video video= videoService.getByBv(bv);
        if(video==null)
        {
            return Result.fail(ResultCode.GET_ERR,"no such video!");
        }
        return Result.ok(ResultCode.GET_OK,video);
    }

    /*
    handle upvote, if the current user has upvoted, cancel it; if not, upvote.
     */
    @PutMapping("/upvote/BV{bv}")
    public Result upvote(@PathVariable String bv)
    {
        Long upvoteNum=videoService.upvote(bv);
        if(upvoteNum==null)
        {
            return Result.fail(ResultCode.GET_ERR,"no such video!");
        }
        return Result.ok(ResultCode.GET_OK,upvoteNum);
    }

    @GetMapping("/upvoteNum/BV{bv}")
    public Result getUpvoteNumByBv(@PathVariable String bv)
    {
        Long upvoteNum=videoService.getUpvoteNumByBv(bv);
        if(upvoteNum==null)
        {
            return Result.fail(ResultCode.GET_ERR,"no such video!");
        }
        return Result.ok(ResultCode.GET_OK,upvoteNum);
    }

    /*
    handle downvote, if the current user has downvoted, cancel it; if not, downvote.
     */
    @PutMapping("/downvote/BV{bv}")
    public Result downvote(@PathVariable String bv)
    {
        return videoService.downvote(bv);
    }

    @PutMapping("/coin/BV{bv}/{num}")
    public Result coin(@PathVariable String bv,@PathVariable int num)
    {
        //the coin number should not be lager than 2
        if(num>2||num<1)
        {
            return Result.fail("can only put 1 or 2 coins every time!");
        }

        return videoService.coin(bv,num);
    }

    @GetMapping("/coinNum/BV{bv}")
    public Result getCoinNumByBv(@PathVariable String bv)
    {
        return videoService.getCoinNumByBv(bv);
    }

    @PostMapping("/favorite/BV{bv}")
    public Result save(@PathVariable String bv)
    {
        return videoService.favorite(bv);
    }
}
