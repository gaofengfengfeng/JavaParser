package com.java.parse;

import com.java.parse.utils.StmtUtils;
import com.java.parse.utils.StringUtils;
import org.eclipse.jdt.core.dom.*;

import javax.swing.*;
import java.io.*;
import java.util.List;

/**
 * @Author:
 * @Date: 2018-07-23
 * @Description: 输出activity的入口类
 */
public class ActivityParser {
    /**
     * 转换为activity方法
     *
     * @param filePathList
     * @param fileName
     *
     * @throws InterruptedException
     */
    public void Convert2Activity(List<String> filePathList, String fileName) throws InterruptedException {
        FileInputStream fis = null;
        File file;
        PrintWriter printWriter;
        FileWriter fileWriter = null;
        //String fileName = pathName + "/" + String.valueOf(System.currentTimeMillis()) + ".txt";
        file = new File(fileName);
        try {
            fileWriter = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "转换成功！", "成功", JOptionPane.OK_OPTION);
            return;
        }
        printWriter = new PrintWriter(fileWriter);
        for (String filePath : filePathList) {
            try {
                // 判断是否是java文件
                if (!filePath.endsWith(".java")) {
                    System.out.println("The file is not java file filePath=" + filePath);
                    JOptionPane.showMessageDialog(null, "转换失败！", "失败", JOptionPane.OK_OPTION);
                    return;
                }
                // 将java文件以流的形式读入
                fis = new FileInputStream(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "转换失败！", "失败", JOptionPane.OK_OPTION);
                return;
            }

            //构建astparser解析器
            ASTParser astParser = ASTParser.newParser(AST.JLS3);
            astParser.setKind(ASTParser.K_COMPILATION_UNIT);
            astParser.setSource(getFileContent(fis, "utf8").toCharArray());
            astParser.setResolveBindings(true);
            CompilationUnit result = (CompilationUnit) astParser.createAST(null);
            String space = "";
            if (result.types().size() != 0) {
                TypeDeclaration typeDec = (TypeDeclaration) result.types().get(0);
                String className = typeDec.getName() + " :: ";
                MethodDeclaration[] methodDeclarations = typeDec.getMethods();
                //对java文件中的方法进行遍历
                for (MethodDeclaration methodDeclaration : methodDeclarations) {
                    SimpleName simpleName = methodDeclaration.getName();
                    List param = methodDeclaration.parameters();
                    printWriter.println(space + className + simpleName + "(" + StringUtils.paramToString(param) + ") {");
                    if (methodDeclaration.getBody() != null) {
                        Block block = methodDeclaration.getBody();
                        space+="  ";
                        for (int i = 0; i < block.statements().size(); i++) {
                            Statement statement = (Statement) block.statements().get(i);
                            int type = StmtUtils.getStmtType(statement);
                            StmtUtils.switchStmt(type, statement, space, true, printWriter);
                        }
                    }
                    space = space.substring(0, space.length()-2);
                    printWriter.println(space + "}");
                }
            }
            printWriter.flush();
        }
        printWriter.close();
        JOptionPane.showMessageDialog(null, "转换成功！", "成功", JOptionPane.OK_OPTION);
    }

    /**
     * 将文件流转化成String格式返回
     *
     * @param fis
     * @param encoding
     *
     * @return
     */

    public static String getFileContent(FileInputStream fis, String encoding) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
