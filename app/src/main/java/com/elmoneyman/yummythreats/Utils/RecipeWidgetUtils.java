package com.elmoneyman.yummythreats.Utils;


import com.google.gson.Gson;

import java.lang.reflect.Type;

public class RecipeWidgetUtils {

    public static <T> T convertFromJsonString(String jsonString, Type type){
        if(jsonString==null) return null;
        Gson gson=new Gson();
        return gson.fromJson(jsonString,type);
    }

    public static String convertToJsonString(Object object, Type type){
        if(object==null) return null;
        Gson gson=new Gson();
        return gson.toJson(object,type);
    }

}
