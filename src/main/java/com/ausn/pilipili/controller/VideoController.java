package com.ausn.pilipili.controller;

import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        return videoService.upload(request);
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

    @GetMapping("/upvoteNum/BV{bv}")
    public Result getUpvoteNumByBv(@PathVariable String bv)
    {
        return videoService.getUpvoteNumByBv(bv);
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
