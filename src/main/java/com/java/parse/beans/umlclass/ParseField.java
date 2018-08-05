package com.java.parse.beans.umlclass;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description: 定义类中字段的属性
 */
public class ParseField {

    private String classification; // atrribute or reference
    private String name;
    private String type;
    private String commonType;
    private String value;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommonType() {
        return commonType;
    }

    public void setCommonType(String commonType) {
        this.commonType = commonType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
