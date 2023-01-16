package com.example.demo;

public class aa1 {
    private static aa1 aa1 = new aa1();
    private static aa2 aa2;

    private static aa3 aa3;

    public static aa1 getaa1(){
        if(aa1==null){
            aa1 = new aa1();
        }
        return aa1;
    };

    public aa2 getaa2(){
      if(aa2==null){
        aa2 = new aa2();
      }
      return aa2;
    };

    public aa3 getAa3(){
        if(aa3==null){
            aa3 = new aa3();
        }
        return aa3;
    }
}
