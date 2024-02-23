package com.akp.examcoach.Basic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class BannerData implements Serializable {
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static List<BannerData> createJsonInList(String str){
        Gson gson=new Gson();
        Type type=new TypeToken<List<BannerData>>(){
        }.getType();
        return gson.fromJson(str,type);
    }
}
