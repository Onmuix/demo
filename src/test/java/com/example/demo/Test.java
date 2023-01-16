package com.example.demo;

import java.util.*;

public class Test {
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("hehe");
        list.add("haha");
        list.add("qq");
        Iterator<String> iterator = list.iterator();
        int a = 0;
        while (iterator.hasNext()){
            System.out.println(iterator.next());
            System.out.println(a++);
        }


        HashMap<String, String> map = new HashMap<>();
        map.put("1","a");
        map.put("2","b");
        map.put("3","c");
        map.put("4","d");

        Set<Map.Entry<String, String>> entries = map.entrySet();
        System.out.println(entries);

        boolean one = true;
        if(!one){
            System.out.println(1);
        }else{
            System.out.println(2);
        }


        int A = 123;
        float B = A / 10.0F;
        System.out.println(B);

        System.out.println(52564/26668251);


        List<Integer> id = new ArrayList<>();
        id.add(1);
        int id1 = 1;
        boolean contains = id.contains(id1);
        System.out.println(contains);
//        if(contains){
//
//        }

        String str1 = "257";
        int i = Integer.parseInt(str1);
        System.out.println(i);

        int  aa1 = -2;

        int  a1 = 104^(aa1);

        float a2 = 265;
        System.out.println(a1);

        double pow = Math.pow(1.04, -2);
        double pow2 = pow * 265;
        double pow3 = Math.pow(1.075, -2);
        double pow4 = pow3 * 265;
        System.out.println(pow2);
        System.out.println(pow4);
        System.out.println(pow);
        pow = Math.pow(1.04, -2);
        System.out.println(pow);

    }
}
