package com.ausn.pilipili.controller;

import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class VideoController
{
    @Autowired
    private VideoService videoService;

    //upload a video
    @PostMapping
    public Result save(@RequestBody Video video)
    {
        return videoService.save(video);
    }

    //request for watching a video
    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
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
}
