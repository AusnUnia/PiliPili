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
    @PostMapping
    public Result save(@RequestBody Video video)
    {
        boolean flag=videoService.save(video);
        int code=flag?ResultCode.SAVE_OK:ResultCode.SAVE_ERR;
        return new Result(code,"",flag);
    }

    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
        Video video=videoService.getByBv(bv);
        System.out.println(video);
        if(video!=null)
        {
            return new Result(ResultCode.GET_OK,"",video);
        }
        return new Result(ResultCode.GET_ERR,"",null);
    }
}
