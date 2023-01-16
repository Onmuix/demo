package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {

        HashMap<Integer,String> map = new HashMap<>();
        map.put(1,"111");
        map.put(2,"222");
        map.put(3,"333");
        String s = map.get(3);
        String s1 = map.get(1);

        System.out.println(0x0a);
        System.out.println(s+s1);
        System.out.println(0xffff);

    }



}
