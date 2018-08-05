package com.java.parse.visitor;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.java.parse.JavaParse;
import com.java.parse.beans.umlclass.ParseClass;
import com.java.parse.beans.umlclass.ParseField;
import com.java.parse.beans.umlclass.ParseMethod;
import com.java.parse.beans.umlclass.ParsePackage;
import com.java.parse.utils.CollectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description: java解析器工具类
 */
public class MethodVisitor extends VoidVisitorAdapter<Void> {

    /**
     * 获取和类相关的信息
     *
     * @param classOrInterfaceDeclaration
     * @param aVoid
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Void aVoid) {

        // 构建类相关的信息，如类名、继承、实现、修饰符(这里只考虑了ABSTRACT,其他类似public的都忽略了，
        // 如果有需求可以加)、字段属性以及方法属性
        ParseClass parseClass = new ParseClass();
        parseClass.setName(classOrInterfaceDeclaration.getName().asString());
        parseClass.setExtendClass(classOrInterfaceDeclaration.getExtendedTypes().toString());
        parseClass.setExtendClass(classOrInterfaceDeclaration.getExtendedTypes().size() > 0 ?
                classOrInterfaceDeclaration.getExtendedTypes(0).toString() : "");
        List<String> implementInterfaceList = new ArrayList<>();
        for (ClassOrInterfaceType classOrInterfaceType : classOrInterfaceDeclaration.getImplementedTypes()) {
            implementInterfaceList.add(classOrInterfaceType.asString());
        }
        parseClass.setImplementInterfaceList(implementInterfaceList);
        parseClass.setModifier(classOrInterfaceDeclaration.getModifiers().contains(Modifier.ABSTRACT) ?
                "abstract" : "");

        // 构建字段属性
        List<ParseField> parseFieldList = new ArrayList<>();
        for (FieldDeclaration fieldDeclaration : classOrInterfaceDeclaration.getFields()) {
            ParseField parseField = new ParseField();
            parseField.setName(fieldDeclaration.getVariable(0).toString().split("=")[0].trim());
            // 判断字段声明语句是否赋值了，如果赋值了记录下"="右边的内容
            if (fieldDeclaration.getVariable(0).toString().contains("=")) {
                parseField.setValue(fieldDeclaration.getVariable(0).toString().split("=")[1].trim());
            }
            // 其中getElementType()得到的没有数组信息的类型，getCommonType()是完整的类型定义
            parseField.setCommonType(fieldDeclaration.getCommonType().asString());
            parseField.setType(fieldDeclaration.getElementType().asString());
            // 在这里String、Integer之类的封装对象，都是引用类型，给的示例里确是attribute，这里写了一个工具类，把java的封装对象列举
            // 出来，进行判断
            parseField.setClassification(fieldDeclaration.getElementType().isReferenceType() && !CollectionType
                    .javaWrappedClass.contains(fieldDeclaration.getElementType().asString()) ? "reference" :
                    "attribute");
            parseFieldList.add(parseField);
        }
        // 将字段属性加入到类属性中
        parseClass.setParseFieldList(parseFieldList);

        // 构建方法属性
        List<ParseMethod> parseMethodList = new ArrayList<>();
        for (MethodDeclaration methodDeclaration : classOrInterfaceDeclaration.getMethods()) {
            // 方法属性包括方法名和返回类型
            ParseMethod parseMethod = new ParseMethod();
            parseMethod.setMethodName(methodDeclaration.getName().asString());
            parseMethod.setReturnType(methodDeclaration.getType().asString());
            parseMethod.setParameters(methodDeclaration.getParameters());

            // 将方法属性添加到方法属性列表中
            parseMethodList.add(parseMethod);
        }
        // 将方法属性加入到类属性中
        parseClass.setParseMethodList(parseMethodList);

        // javaParser可以向上一级找到当前类所属的package
        Name packageName = null;
        Optional<PackageDeclaration> packageDeclaration = classOrInterfaceDeclaration.getParentNode().get()
                .findCompilationUnit().get().getPackageDeclaration();
        if (packageDeclaration.isPresent()) {
            packageName = packageDeclaration.get().getName();
        }
        ParsePackage parsePackage = JavaParse.umlClass.getPackageConcurrentHashMap().get
                (packageName == null ? "" : packageName.asString());

        // 如果包对象不为空，并且其类列表中不含有当前类解析对象，则把类解析对象添加单类列表中
        if (parsePackage != null) {
            List<ParseClass> parseClassList = parsePackage.getParseClassList();
            if (!parseClassList.contains(parseClass)) {
                parseClassList.add(parseClass);
            }
        }
        super.visit(classOrInterfaceDeclaration, aVoid);
    }

    /**
     * 获取和包相关的信息
     *
     * @param packageDeclaration
     * @param aVoid
     */
    @Override
    public void visit(PackageDeclaration packageDeclaration, Void aVoid) {
        // 在此只是将包名存储到map中，如果已经存在，则不做任何处理
        if (JavaParse.umlClass.getPackageConcurrentHashMap().get(packageDeclaration.getName().asString()) == null) {
            ParsePackage parsePackage = new ParsePackage();
            parsePackage.setName(packageDeclaration.getName().asString());
            parsePackage.setParseClassList(new ArrayList<ParseClass>());
            JavaParse.umlClass.getPackageConcurrentHashMap().put(packageDeclaration.getName().asString(), parsePackage);
        }

        super.visit(packageDeclaration, aVoid);
    }
}
