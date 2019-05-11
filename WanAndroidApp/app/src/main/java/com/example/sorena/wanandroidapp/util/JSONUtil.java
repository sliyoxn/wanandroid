package com.example.sorena.wanandroidapp.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtil
{
    private static final String TAG = "日志:JSONUtil";


    /**
     *
     * @param key : 要找的目标
     * @param string : 一个符合JSON格式的字符串
     * @param paths : 寻找路径
     * @return : 返回找寻目标的toString方法的值
     */
    public static String getValue(String key , String string , String[] paths){

        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONTokener jsonTokener;
        String jsonString = string;
        boolean flag = false;
        if (paths.length == 0) flag = true;
        try {
            int i = 0;
            while (true){
                jsonTokener = new JSONTokener(jsonString);
                Object json = jsonTokener.nextValue();
                if (json instanceof JSONArray){
                    jsonArray = (JSONArray)json;
                    jsonString = jsonArray.get(0).toString();
                }
                else if (json instanceof  JSONObject){
                    jsonObject = (JSONObject)json;
                    if (flag){
                        return jsonObject.get(key).toString();
                    }
                    jsonString = jsonObject.get(paths[i++]).toString();
                    if ( paths.length  == i ){
                        flag = true;
                    }
                }
                else {
                    break;
                }

            }

        }
        catch (JSONException e) {
            Log.d(TAG,"警告,传入的JSON字符串有错误或者路径指定错误");
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     *
     * @param jsonString ： 一个可以按JSON解析的字符串
     * @param keys : 要存储为数组的值
     * @return 一个以以keys中对应值为key, 对应值的List为value的一个Map
     */
    public static Map<String,List<String>> getMapInArray(String jsonString, String[] keys){

        JSONTokener jsonTokener = new JSONTokener(jsonString);
        Object json = null;
        try {
            json = jsonTokener.nextValue();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (json instanceof JSONArray){
                JSONArray jsonArray = (JSONArray)json;
                if (new JSONTokener(jsonArray.get(0).toString()).nextValue() instanceof JSONArray) return null;
                List<List<String>> lists = new ArrayList<>();
                for (int i = 0 ; i < keys.length ; i++){
                    lists.add(new ArrayList<String>());
                }
                for (int i = 0; i < jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    for (int j = 0 ; j < keys.length ; j++){
                        lists.get(j).add(jsonObject.get(keys[j]).toString());
                    }
                }
                Map<String,List<String>> stringListMap = new HashMap<>();
                for (int i = 0 ; i < keys.length ; i++){
                    stringListMap.put(keys[i] , lists.get(i));
                }
                return stringListMap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}