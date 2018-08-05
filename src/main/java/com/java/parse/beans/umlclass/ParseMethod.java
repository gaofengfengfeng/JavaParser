package com.java.parse.beans.umlclass;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description: 定义类中方法的属性
 */
public class ParseMethod {
    private String returnType;
    private String methodName;
    private NodeList<Parameter> parameters;

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public NodeList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(NodeList<Parameter> parameters) {
        this.parameters = parameters;
    }
}
