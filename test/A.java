package com.java.parse.test;

import java.util.HashMap;

/**
 * @Author: gaofeng
 * @Date: 2018-07-22
 * @Description:
 */
public class A {
    private Integer a;

    private String b;

    public String getB() {
        return b;
    }


    private char[] c;
    private B d;
    private D e;
    private HashMap<String, B> f;

    public A() {

    }

    public A(Integer a, String b) {
        this.a = a;
        this.b = b;
    }

    public Integer returnInteger(Integer a) {
        return a;
    }

    public Integer returnTwoInteger(Integer a, List<C> c) {
        return a;
    }

    public String returnString(String b) {
        if (b == "") {
            return "Null";
        } else {
            return b;
        }
    }

    public void returnNothing() {
    }
}





