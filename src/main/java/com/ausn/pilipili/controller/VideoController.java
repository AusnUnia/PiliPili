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

    //处理上传视频
    @PostMapping
    public Result save(@RequestBody Video video)
    {
        boolean flag=videoService.save(video);
        int code=flag?ResultCode.SAVE_OK:ResultCode.SAVE_ERR;
        return new Result(code,"",flag);
    }

    //处理请求观看视频
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

    //处理点赞，点踩，投币，收藏或分享（点踩和点赞同时处理有点复杂，还要记录用户的行为，先写不记名的（就是一个人就能点赞或点踩任意次数）好了）
    //flag=1表示进行操作，opFlag=-1表示取消操作，operation是具体操作名称
    @PutMapping("/BV{bv}/{operation}/{opFlag}")
    public Result updateSingleAttribution(@PathVariable String bv,@PathVariable String operation,@PathVariable int opFlag)
    {
        boolean flag=videoService.updateSingleAttribution(bv,operation,opFlag);
        int code=flag?ResultCode.UPDATE_OK:ResultCode.UPDATE_ERR;
        return new Result(code,"",flag);
    }
}
