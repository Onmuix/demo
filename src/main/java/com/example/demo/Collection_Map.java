package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Collection_Map {
    public static void main(String[] args) {

        //Map
        //HashMap ： Hash + map
        //数据存储无序

        //修改数据：put
        //修改数据，put方法也可以是修改数据，返回值就是被修改的值。
        HashMap <String,String> map = new HashMap<>();
        map.put("zs","1");
        map.put("zs","12");
        map.put("ls","2");
        map.put("ww","3");

        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry);
        }

        //查询数据
//        System.out.println(map.get("zs"));
//        map.remove("zs");
//        for (Object o : map.keySet()) {
//            System.out.println(map.get(o));
//        }
//        System.out.println(map.containsKey("zs"));



//        System.out.println(map);
    }
}
