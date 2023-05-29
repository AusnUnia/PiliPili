package com.ausn.pilipili.service;

import com.ausn.pilipili.entity.Video;

import java.util.List;

public interface VideoService
{
    public boolean save(Video video);
    public boolean delete(Video video);
    public Video getByBv(String bv);
    public Video getByAuthorId(String authorId);
    public boolean update(Video video);
    public List<Video> getRandomly();
    public boolean updateSingleAttribution(String bv,String operation,int opFlag);

}
