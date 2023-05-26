package com.ausn.pilipili.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomePageController
{
    @GetMapping("/video/BV{bv}")
    public String getVideo(@PathVariable String bv)
    {
        return "pages/video-page";
    }
}
