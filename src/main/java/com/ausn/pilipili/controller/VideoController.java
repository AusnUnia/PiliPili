package com.ausn.pilipili.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.entity.requestEntity.VideoUploadRequest;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
    public Result upload(MultipartHttpServletRequest request)
    {
        System.out.println(request.getFile("video").getSize());
        System.out.println(request.getParameter("videoUploadRequest"));
        VideoUploadRequest videoUploadRequest = BeanUtil.toBean(request.getParameter("videoUploadRequest"), VideoUploadRequest.class);
        System.out.println(videoUploadRequest); //TODO 无法正确序列化
        return null;
        /*return videoService.upload(request);*/
    }

    /*
    request for the information of a video
     */
    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
        Video video= (Video) videoService.getByBv(bv).getData();
        System.out.println(video);
        return videoService.getByBv(bv);
    }

    /*
    handle upvote, if the current user has upvoted, cancel it; if not, upvote.
     */
    @PutMapping("/upvote/BV{bv}")
    public Result upvote(@PathVariable String bv)
    {
        return videoService.upvote(bv);
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
        return videoService.coin(bv,num);
    }

    @PostMapping("/favorite/BV{bv}")
    public Result save(@PathVariable String bv)
    {
        return videoService.favorite(bv);
    }
}
