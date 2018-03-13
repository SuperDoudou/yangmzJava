package com.yangmz.base.helper;

public class BaseValue {
    public static Long getLong(String date){
       try {
           return Long.valueOf(date);
       }catch (NumberFormatException e){
           return null;
       }
    }

    public static Long getLongId(String date){
        Long value = null;
        try {
            value = Long.valueOf(date);
        }catch (NumberFormatException e){
            return null;
        }
        if (value <= 0 ){
            return null;
        }
        return value;
    }
    public static Integer getInteger(String date){
        try {
            return Integer.valueOf(date);
        }catch (NumberFormatException e){
            return null;
        }
    }
    public static Integer getIntegerId(String date){
        Integer value = null;
        try {
            value = Integer.valueOf(date);
        }catch (NumberFormatException e){
            return null;
        }
        if (value <= 0 ){
            return null;
        }
        return value;
    }

}
