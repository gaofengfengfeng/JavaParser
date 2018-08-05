package com.java.parse.utils;

import com.github.javaparser.ast.body.Parameter;
import com.java.parse.beans.umlclass.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.java.parse.JavaParse.umlClass;

/**
 * @Author: gaofeng
 * @Date: 2018-07-23
 * @Description: 将UML、ACTIVITY对象输出到txt文件中
 */
public class PrintToTxt {

    /**
     * 根据传入的UMLClass对象生成相关的txt文件
     *
     * @param umlClass
     *
     * @return
     */
    public static boolean printUMLToTxt(UMLClass umlClass, String filePath) {

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(filePath));
            Iterator iter = umlClass.getPackageConcurrentHashMap().entrySet().iterator();
            // 遍历包的hashMap
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                ParsePackage parsePackage = (ParsePackage) entry.getValue();
                // 在这里可能存在包为空的情况
                if (key.equals("")) {
                    // 打印类的信息
                    classPrint(pw, parsePackage.getParseClassList(), false);
                    if (parsePackage.getParseClassList().size() == 0) {
                        continue;
                    }
                } else {
                    pw.println("package " + key + " {");
                    classPrint(pw, parsePackage.getParseClassList(), true);
                    pw.println("}");
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        return true;
    }

    /**
     * 类相关信息的输出
     *
     * @param pw
     * @param parseClassList
     */
    public static void classPrint(PrintWriter pw, List<ParseClass> parseClassList, boolean hasPackage) {
        int flag = 0;
        for (ParseClass parseClass : parseClassList) {
            // 判断顶层是否有包名，如果有缩进两个字符，如果没有不做处理
            if (hasPackage) {
                pw.print("  ");
            }

            // 获取类的修饰符，如果不为空，则进行拼接
            if (!parseClass.getModifier().equals("")) {
                pw.print(parseClass.getModifier() + " ");
            }

            // 打印类名
            pw.print("class " + parseClass.getName());

            // 判断是否有继承类
            if (parseClass.getExtendClass() != null && !parseClass.getExtendClass().equals("")) {
                pw.print(" extends " + parseClass.getExtendClass());
            }

            // 判断是否有实现接口，接口可能实现多个，注意处理
            if (parseClass.getImplementInterfaceList() != null && parseClass.getImplementInterfaceList().size() != 0) {
                for (int i = 0; i < parseClass.getImplementInterfaceList().size(); i++) {
                    if (i == 0) {
                        pw.print(" implements " + parseClass.getImplementInterfaceList().get(i));
                    } else {
                        pw.print("," + parseClass.getImplementInterfaceList().get(i));
                    }
                }
            }
            pw.println(" {");
            // 打印字段信息
            fieldPrint(pw, parseClass.getParseFieldList(), parseClass.getName(), hasPackage);
            // 字段信息和方法信息之间换行(如果含有方法信息)
            if (parseClass.getParseMethodList().size() != 0) {
                pw.println();
            }
            // 打印方法信息
            methodPrint(pw, parseClass.getParseMethodList(), hasPackage);

            // 判断顶层是否有包名，如果有缩进两个字符，如果没有不做处理
            if (hasPackage) {
                pw.print("  }");
            } else {
                pw.print("}");
            }

            pw.println();
            flag++;
            if (flag < parseClassList.size()) {
                pw.println();
            }
        }
    }

    /**
     * 类内字段名的信息输出
     *
     * @param pw
     * @param parseFieldList
     */
    public static void fieldPrint(PrintWriter pw, List<ParseField> parseFieldList, String className, boolean
            hasPackage) {
        for (ParseField parseField : parseFieldList) {

            // 判断是否是集合类型，数组、list、set、map
            boolean isCollection = false;
            // 判断是否有序
            boolean isOrdered = false;

            // 字段类型，list中的泛型
            String fieldType = parseField.getType();

            if (CollectionType.isList.contains(CollectionType.getCollectionType(parseField.getType()))) {
                // 判断是否是list，有序
                isCollection = true;
                isOrdered = true;
                fieldType = CollectionType.getGenericity(parseField.getType());
            } else if (CollectionType.isSet.contains(CollectionType.getCollectionType(parseField.getType()))) {
                // 判断是否是set，无序
                isCollection = true;
                isOrdered = false;
                fieldType = CollectionType.getGenericity(parseField.getType());
            } else if (CollectionType.isMap.contains(CollectionType.getCollectionType(parseField.getType()))) {
                // 判断是否是map，无序
                isCollection = true;
                isOrdered = false;
                fieldType = CollectionType.getCommonTypeValue(CollectionType.getGenericity(parseField.getType()));
            } else if (CollectionType.isArray(parseField.getType(), parseField.getCommonType())) {
                // 判断是否是数组，有序
                isCollection = true;
                isOrdered = true;
                fieldType = CollectionType.getArrayType(parseField.getCommonType());
            }

            // 缩进判断
            if (hasPackage) {
                pw.print("    ");
            } else {
                pw.print("  ");
            }

            // 输出reference或者attribute
            pw.print(parseField.getClassification() + " " + parseField.getName());

            // 如果是reference才会有下述输出结果
            if (parseField.getClassification().equals("reference")) {
                if (isCollection) {
                    pw.print("[*]");
                } else if (parseField.getValue() != null && parseField.getValue().equals("null")) {
                    pw.print("[0-1]");
                }

                if (isOrdered) {
                    pw.print(" ordered");
                }
            }


            // 判断是否存在双向调用情况，如果存在输出oppositeOf
            if (isOppositeOf(className, fieldType) == null) {
                if (parseField.getClassification().equals("reference")) {
                    pw.println(" : " + fieldType + ";");
                } else {
                    pw.println(" : " + parseField.getCommonType() + ";");
                }

            } else {
                // 如果是reference才会有下述输出结果
                if (parseField.getClassification().equals("reference")) {
                    pw.println(" : " + fieldType + " oppositeOf " + isOppositeOf(className, fieldType) + ";");
                }
            }
        }
    }

    /**
     * 类内方法信息的输出
     *
     * @param pw
     * @param parseMethodList
     */
    public static void methodPrint(PrintWriter pw, List<ParseMethod> parseMethodList, boolean
            hasPackage) {
        for (ParseMethod parseMethod : parseMethodList) {
            // 缩进输出
            String space = "";
            if (hasPackage) {
                space = "    ";
            } else {
                space = "  ";
            }
            pw.print(space + "operation " + parseMethod.getMethodName() + "(");
            for (int i = 0; i < parseMethod.getParameters().size(); i++) {
                pw.print(parseMethod.getParameters().get(i).getName() + " : " + parseMethod.getParameters().
                        get(i).getType());
                if (i != parseMethod.getParameters().size() - 1) {
                    pw.print(", ");
                }
            }
            for (Parameter parameter : parseMethod.getParameters()) {

            }
            pw.println(") : " + parseMethod.getReturnType());
            pw.println(space + "pre: true");
            pw.println(space + "post: true");
        }
    }

    /**
     * 遍历所有的类，找到referenceClassName类，并看该类中的字段有没有sourceClassName类型的，如果有，返回true；没有，返回false
     *
     * @param sourceClassName 当前类的名称
     * @param referenceClassName 引用变量所在的类的名称
     *
     * @return
     */
    public static String isOppositeOf(String sourceClassName, String referenceClassName) {
        Iterator iter = umlClass.getPackageConcurrentHashMap().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            ParsePackage parsePackage = (ParsePackage) entry.getValue();
            for (ParseClass parseClass : parsePackage.getParseClassList()) {
                if (parseClass.getName().equals(referenceClassName)) {
                    for (ParseField parseField : parseClass.getParseFieldList()) {
                        if (parseField.getType().contains(sourceClassName)) {
                            return parseField.getName();
                        }
                    }
                }
            }
        }
        return null;
    }
}
