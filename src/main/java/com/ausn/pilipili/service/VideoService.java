package com.ausn.pilipili.service;

import com.ausn.pilipili.domain.Video;

import java.util.List;

public interface VideoService
{
    public int save(Video video);
    public Video getByBv(String bv);
    public List<Video> getByPreference();
}
