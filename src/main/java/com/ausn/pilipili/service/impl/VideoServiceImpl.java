package com.ausn.pilipili.service.impl;

import com.ausn.pilipili.dao.VideoDao;
import com.ausn.pilipili.entity.Video;
import com.ausn.pilipili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService
{
    @Autowired
    VideoDao videoDao;
    @Override
    public boolean save(Video video)
    {
        video.setUploadDate(Timestamp.valueOf(LocalDateTime.now()));
        video.setSaveNum(BigInteger.ZERO);
        video.setShareNum(BigInteger.ZERO);
        video.setUpvoteNum(BigInteger.ZERO);
        video.setDownvoteNum(BigInteger.ZERO);
        video.setCoinNum(BigInteger.ZERO);

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

    public boolean updateSingleAttribution(String bv,String operation,int opFlag)
    {
        int res=0;
        if(operation.equals("upvote"))
        {
            if(opFlag==1)
            {
                res=videoDao.updateUpvoteNumByBv(bv,1);
            }
            else if(opFlag==-1)
            {
                res=videoDao.updateUpvoteNumByBv(bv,-1);
            }
        }
        else if(operation.equals("downvote"))
        {
            if(opFlag==1)
            {
                res=videoDao.updateDownvoteNumByBv(bv,1);
            }
            else if(opFlag==-1)
            {
                res=videoDao.updateDownvoteNumByBv(bv,-1);
            }
        }
        else if(operation.equals("coin"))
        {
            if(opFlag==1)
            {
                res=videoDao.updateCoinNumByBv(bv,1);
            }
            else if(opFlag==-1)
            {
                res=videoDao.updateCoinNumByBv(bv,-1);
            }
        }
        else if(operation.equals("save"))
        {
            if(opFlag==1)
            {
                res=videoDao.updateSaveNumByBv(bv,1);
            }
            else if(opFlag==-1)
            {
                res=videoDao.updateSaveNumByBv(bv,-1);
            }
        }
        else if(operation.equals("share"))
        {
            if(opFlag==1)
            {
                res=videoDao.updateShareNumByBv(bv,1);
            }
            else if(opFlag==-1)
            {
                res=videoDao.updateShareNumByBv(bv,-1);
            }
        }
        return res>0;
    }
}
