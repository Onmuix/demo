package com.example.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

public class Test01 {
    public static void main(String[] args) {
//        String strId = "123,456,789";
//        for (String s : strId.split(",")) {
//            System.out.println(s);
//        }

//        double pow = Math.pow((0.000035 * 1000 + 1), -2);
        double pow = Math.pow(1.1, -2);
        double pow1 = Math.pow((0.0004 * 100+1), -2);
//        double pow2 = Math.pow((0.000075 * 5006 + 1), -2);
        double pow3 = Math.pow((0.0000381 * 900 + 1), -2);
        double pow4 = Math.pow((0.0000225 * 1000 + 1), -2);
        double pow5 = Math.pow((0.0000081 * 2000 + 1), -2);
        double pow6 = Math.pow((0.0000095 * 1000 + 1), -2);
        double pow7 = Math.pow((0.0000048 * 1000 + 1), -2);
        double pow8 = Math.pow((0.000001 * 1 + 1), -2);

//        System.out.println(pow1*265);
////        System.out.println(pow2*265);
//        System.out.println(pow3*245);
//        System.out.println(pow4*229);
//        System.out.println(pow5*219);
//        System.out.println(pow6*212);
//        System.out.println(pow7*208);
//        System.out.println(pow8*206);

        Calendar calendar = Calendar.getInstance();
        long longDateNum = calendar.getTimeInMillis();
        System.out.println(calendar);
        System.out.println(longDateNum);


//        int[] number = new int[]{-96 ,-96 ,12 ,2 ,1 ,4 ,0 ,0 ,0 ,13 ,14 ,8 ,10 ,0 ,1};
//
//        int a1 = 0;
//
//        for (int i = 0; i < number.length; i++) {
//            System.out.println("i: " + number[i]);
//            a1 = a1^number[i];
//            System.out.println("a1: " + a1);
//        }
//
//        System.out.println(a1);

        int a = 256;
        System.out.println(a & 0xFF);
        System.out.println( 38 | 2 << 8);
        System.out.println(550f / 60f);

        System.out.println(16%8);




//        System.out.println(pow*265);
//        pow = pow * 265 ;
//        System.out.println(0.8653*265);
//        pow = pow * 265;
//        System.out.println(pow);
//        System.out.println(229f/1000f);

        // 1000 = 0.8653
    }
}
