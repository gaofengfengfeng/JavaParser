package com.java.parse.beans.umlclass;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description: 定义类的属性
 */
public class ParseClass {

    private String modifier; //类的修饰符
    private String name;
    private String extendClass;
    private List<String> implementInterfaceList;
    private List<ParseField> parseFieldList;
    private List<ParseMethod> parseMethodList;

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendClass() {
        return extendClass;
    }

    public void setExtendClass(String extendClass) {
        this.extendClass = extendClass;
    }

    public List<String> getImplementInterfaceList() {
        return implementInterfaceList;
    }

    public void setImplementInterfaceList(List<String> implementInterfaceList) {
        this.implementInterfaceList = implementInterfaceList;
    }

    public List<ParseField> getParseFieldList() {
        return parseFieldList;
    }

    public void setParseFieldList(List<ParseField> parseFieldList) {
        this.parseFieldList = parseFieldList;
    }

    public List<ParseMethod> getParseMethodList() {
        return parseMethodList;
    }

    public void setParseMethodList(List<ParseMethod> parseMethodList) {
        this.parseMethodList = parseMethodList;
    }
}
