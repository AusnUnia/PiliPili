package com.ausn.pilipili.utils;

public class PUserUtil
{
    public static boolean isPhoneNumberValid(String phoneNumber)
    {
        //the phone number in China contains 11 digits
        if(phoneNumber==null||phoneNumber.length()!=11)
        {
            return false;
        }

        for(char ch:phoneNumber.toCharArray())
        {
            if(!Character.isDigit(ch))
            {
                return false;
            }
        }

        return true;
    }
}
