package com.ausn.pilipili.service;

import com.ausn.pilipili.entity.Video;

import java.util.List;

public interface VideoService
{
    public int save(Video video);
    public Video getByBv(String bv);
    public Video getByAuthorId(String authorId);
    public int update(Video video);
    public List<Video> getRandomly();

}
