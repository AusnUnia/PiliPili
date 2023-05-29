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

    //加载对应视频下的评论
    @GetMapping("/BV{bv}")
    public Result getByBv(@PathVariable String bv)
    {
        List<UserComment> userComments=userCommentService.getByBv(bv);
        System.out.println(userComments);
        return new Result(ResultCode.GET_OK,"",userComments);
    }

    //发布评论
    @PostMapping("/BV{bv}")
    public Result publish(@RequestBody UserComment userComment,@PathVariable String bv)
    {
        if(!bv.equals(userComment.getBv()))
        {
            return new Result(ResultCode.SAVE_ERR,"评论发布失败,视频和评论bv号不对应",false);
        }
        boolean flag=userCommentService.publish(userComment);
        int code=flag?ResultCode.SAVE_OK:ResultCode.SAVE_ERR;
        String msg=flag?"":"评论发布失败";
        return new Result(code,msg,flag);
    }

    //删除评论
    @DeleteMapping("/BV{bv}")
    public Result delete(@PathVariable String bv,@RequestBody UserComment userComment)
    {
        boolean flag=userCommentService.delete(userComment);
        return new Result(123,"",flag);
    }
}
