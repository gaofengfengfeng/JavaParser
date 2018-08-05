package com.java.parse.beans.umlclass;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description:
 */
public class ParsePackage {

    private String name;
    private List<ParseClass> parseClassList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParseClass> getParseClassList() {
        return parseClassList;
    }

    public void setParseClassList(List<ParseClass> parseClassList) {
        this.parseClassList = parseClassList;
    }
}
