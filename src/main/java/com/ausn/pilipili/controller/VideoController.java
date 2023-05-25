package com.ausn.pilipili.controller;

import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
public class VideoController
{
    @Autowired
    private VideoService videoService;
    @GetMapping
    public String save()
    {
        Video video=new Video();
        video.setBv("auidsas");
        System.out.println(videoService.save(video));
        return "save()";
    }

    @GetMapping("/BV{bv}")
    public String getByBv(String bv)
    {
        videoService.getByBv(bv);
        return "getByBv";
    }
}
