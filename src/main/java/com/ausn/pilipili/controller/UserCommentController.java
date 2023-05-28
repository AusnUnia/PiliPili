package com.ausn.pilipili.controller;

import com.ausn.pilipili.entity.UserComment;
import com.ausn.pilipili.service.UserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class UserCommentController
{
    @Autowired
    private UserCommentService userCommentService;

    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
        List<UserComment> userComments=userCommentService.getByBv(bv);
        System.out.println(userComments);
        return new Result(ResultCode.GET_OK,"",userComments);
    }

    @PostMapping("/BV{bv}")
    public Result save(@RequestBody UserComment userComment,@PathVariable String bv)
    {
        if(!bv.equals(userComment.getBv()))
        {
            return new Result(ResultCode.SAVE_ERR,"评论发布失败,视频和评论bv号不对应",false);
        }
        boolean flag=userCommentService.save(userComment);
        int code=flag?ResultCode.SAVE_OK:ResultCode.SAVE_ERR;
        String msg=flag?"":"评论发布失败";
        return new Result(code,msg,flag);
    }
}
