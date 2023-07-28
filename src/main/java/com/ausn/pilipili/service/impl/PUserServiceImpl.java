package com.ausn.pilipili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.ausn.pilipili.controller.Result;
import com.ausn.pilipili.controller.ResultCode;
import com.ausn.pilipili.dao.PUserDao;
import com.ausn.pilipili.entity.dto.LoginFormDTO;
import com.ausn.pilipili.entity.dto.PUserDTO;
import com.ausn.pilipili.entity.PUser;
import com.ausn.pilipili.service.PUserService;
import com.ausn.pilipili.utils.constants.LocalConstants;
import com.ausn.pilipili.utils.PUserUtil;
import com.ausn.pilipili.utils.constants.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PUserServiceImpl implements PUserService
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PUserDao pUserDao;

    @Transactional
    public PUser createUserWithPhoneNumber(String phoneNumber)
    {
        PUser pUser=new PUser();
        pUser.setPhoneNumber(phoneNumber);
        pUser.setNickName(LocalConstants.PUSER_NICK_NAME_PREFIX+RandomUtil.randomNumbers(11));
        pUser.setAvatarPath("./images/avatars/default.jpg");
        pUser.setGender("unknown");
        pUser.setBirthday(Date.valueOf(LocalDate.now()));
        pUserDao.save(pUser);
        pUser.setUid(pUserDao.getLastInsertedId());

        return pUser;
    }

    @Override
    public Result login(LoginFormDTO loginFormDTO)
    {
        System.out.println(loginFormDTO);
        //check the phone number
        String phoneNumber= loginFormDTO.getPhoneNumber();
        if(!PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            return Result.fail(ResultCode.BUSINESS_ERR,"请输入正确的手机号");
        }

        //confirm the verification code
        String cachedCode=stringRedisTemplate.opsForValue()
                .get(RedisConstants.LOGIN_CODE_KEY_PREFIX+phoneNumber);
        String verificationCode= loginFormDTO.getVerificationCode();

        //if the cached verification code is not consistent with the verification code
        if(cachedCode==null||!cachedCode.equals(verificationCode))
        {
            System.out.println("cached:"+cachedCode);
            System.out.println("verificationCode:"+verificationCode);
            return Result.fail("验证码错误");
        }

        //query the user by the phone number
        PUser pUser= pUserDao.getByPhoneNumber(phoneNumber);
        if(pUser==null)
        {
            //if didn't find the user, it's new user, create it and save in database
            pUser=createUserWithPhoneNumber(phoneNumber);
        }

        //cache the information of the user in Redis

        //generate token, this is the key to identify whether a user has logged in.
        String token= UUID.randomUUID().toString(true);

        //cache user's information in Redis in the form of hash
        PUserDTO pUserDTO= BeanUtil.copyProperties(pUser, PUserDTO.class); //extract the insensitive information of the user
        Map<String,Object> pUserMap=BeanUtil.beanToMap(
                pUserDTO,new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((field,val)->val.toString())
        );                                                                 //convert the bean of user's insensitive information into map which can be accepted by the Redis
        //System.out.println("in PUSerServ: pUserMap:"+pUserMap);
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_PUSER_KEY_PREFIX+token,pUserMap);

        //set the login period of validity
        stringRedisTemplate.expire(RedisConstants.LOGIN_PUSER_KEY_PREFIX+token,RedisConstants.LOGIN_PUSER_TTL,TimeUnit.MINUTES);

        //return the token to the front end
        return Result.ok(ResultCode.DEFAULT_OK,token);
    }



    @Override
    public Result sendVerificationCode(String phoneNumber)
    {
        //check the phpne number
        if(!PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            return Result.fail(ResultCode.BUSINESS_ERR,"请输入正确的手机号");
        }

        //generate the verification code
        String verificationCode= RandomUtil.randomNumbers(6);

        //save the verification code in redis, later the login business will check it.
        //the verification code is accessible in 3 minutes.
        stringRedisTemplate.opsForValue()
                .set(RedisConstants.LOGIN_CODE_KEY_PREFIX+phoneNumber,verificationCode,
                        RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //send the verification code (it's just simulate the process)
        log.debug("发送短信验证码成功，验证码：{}",verificationCode);

        return Result.ok();
    }
}
