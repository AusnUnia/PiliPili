package com.ausn.pilipili.entity.converter;

import com.ausn.pilipili.entity.PUser;
import com.ausn.pilipili.entity.dto.PUserDTO;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/29 0:19
 * @Description:
 */
public class PUserConverter
{
    public static PUser toPUser(PUserDTO pUserDTO) {
        if (pUserDTO == null) {
            return null;
        }
        PUser pUser = new PUser();
        pUser.setUid(pUserDTO.getUid());
        pUser.setNickName(pUserDTO.getNickName());
        pUser.setGender(pUserDTO.getGender());
        pUser.setBirthday(pUserDTO.getBirthday());
        pUser.setAvatarPath(pUserDTO.getAvatarPath());

        // Not mapped PUser fields:
        // phoneNumber
        return pUser;
    }
}
