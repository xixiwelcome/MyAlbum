package com.cc.milkalbum.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cc.milkalbum.model.Girl;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/9.
 */
public class ResInfoUtils {

    public static ArrayList<Girl> getGirlsInfo(int page, int limit) throws Exception{
        ArrayList<Girl> girls = new ArrayList<>();

        String strJsonGirlList = NetworkUtils.postRequest(ConstantUtils.GirlsListUrl, "page=" + page + "&limit=" + limit);

        JSONObject jsonObject = new JSONObject(strJsonGirlList);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
        if(jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject oneObj = jsonArray.getJSONObject(i);
            JSONObject authorObj = oneObj.getJSONObject("author");
            JSONObject sourceObj = oneObj.getJSONObject("source");
            Girl girl = new Girl(oneObj.getInt("id"), authorObj.getString("nickname"),
                    ConstantUtils.HeaderUrlHeader + authorObj.getString("headerUrl"),
                    oneObj.getString("title"), sourceObj.getString("catalog"),
                    oneObj.getString("issue"), oneObj.getInt("pictureCount"));
            girls.add(girl);
        }
        return girls;
    }


}
