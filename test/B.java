package com.java.parse.test;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2018-07-22
 * @Description:
 */
public class B extends A implements Serializable, Appendable {

    private Integer[][] x = new Integer[5][];
    private String[] y;
    double z;
    private A[] a = new A[5];
    private List<String[]> b;

    public B() {
    }

    public B(Integer[][] x) {
        this.x = x;
    }

    public String c() {
        int a = 0;
        int b = 0;
        for (int i = 0; i < 10; i++) {
            a++;
        }
        while (b < 10) {
            b++;
        }

        if (a < 20) {
            if (a < 10) {
                a += 10;
            }
        } else if (a < 30) {
            a += 20;
        } else {
            a += 30;
        }

        return String.valueOf(a + b);
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        return null;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return null;
    }

    @Override
    public Appendable append(char c) throws IOException {
        return null;
    }
}
