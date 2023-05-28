package com.ausn.pilipili.service.impl;

import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService
{
    @Autowired
    VideoDao videoDao;
    @Override
    public boolean save(Video video)
    {

        return videoDao.save(video)>0;
    }

    @Override
    public boolean delete(Video video)
    {
        return videoDao.delete(video)>0;
    }

    @Override
    public Video getByBv(String bv)
    {
        return videoDao.getByBv(bv);
    }

    @Override
    public Video getByAuthorId(String authorId)
    {
        return videoDao.getByAuthorId(authorId);
    }

    @Override
    public boolean update(Video video)
    {
        return videoDao.update(video)>0;
    }

    @Override
    public List<Video> getRandomly()
    {
        return videoDao.getRandomly();
    }
}
