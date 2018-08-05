package com.java.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.java.parse.beans.umlclass.ParseClass;
import com.java.parse.beans.umlclass.ParsePackage;
import com.java.parse.beans.umlclass.UMLClass;
import com.java.parse.utils.PrintToTxt;
import com.java.parse.visitor.MethodVisitor;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2018-07-22
 * @Description: UML CLASS入口类
 */
public class JavaParse {

    public static UMLClass umlClass;

    /**
     * 转换为UML方法
     *
     * @param filePathList
     * @param fileName
     *
     * @throws InterruptedException
     */
    public void Convert2UMLClass(List<String> filePathList, String fileName) throws InterruptedException {
        umlClass = new UMLClass();
        ParsePackage parsePackage = new ParsePackage();
        parsePackage.setName("");
        parsePackage.setParseClassList(new ArrayList<ParseClass>());
        umlClass.getPackageConcurrentHashMap().put("", parsePackage);

        FileInputStream fis = null;

        for (String filePath : filePathList) {
            try {
                // 判断是否是java文件
                if (!filePath.endsWith(".java")) {
                    System.out.println("the file is not java file filePath=" + filePath);
                    JOptionPane.showMessageDialog(null, "转换失败！", "失败", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 将java文件以流的形式读入
                fis = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "转换失败！", "失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 对文件进行解析
                CompilationUnit cu = JavaParser.parse(fis);
                // 进入自定义的java解析器工具类，查看java文件的包名、类名、属性名、方法名等信息
                cu.accept(new MethodVisitor(), null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "转换失败！", "失败", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        //String fileName = String.valueOf(System.currentTimeMillis()) + ".txt";
        if (PrintToTxt.printUMLToTxt(umlClass, fileName)) {
            JOptionPane.showMessageDialog(null, "转换成功！", "成功", JOptionPane.OK_OPTION);
        } else {
            JOptionPane.showMessageDialog(null, "转换失败！", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}
