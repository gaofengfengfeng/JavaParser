package com.java.parse.beans.umlclass;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description:
 */
public class UMLClass {

    private ConcurrentHashMap<String, ParsePackage> packageConcurrentHashMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, ParsePackage> getPackageConcurrentHashMap() {
        return packageConcurrentHashMap;
    }

    public void setPackageConcurrentHashMap(ConcurrentHashMap<String, ParsePackage> packageConcurrentHashMap) {
        this.packageConcurrentHashMap = packageConcurrentHashMap;
    }
}
