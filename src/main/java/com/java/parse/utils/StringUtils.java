package com.java.parse.utils;

import com.java.parse.beans.umlactivity.ParseFor;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Date: 2018-07-26
 * @Description: 对String做相关处理的类
 */
public class StringUtils {

    /**
     * 删除字符串两端[]
     *
     * @param source
     *
     * @return
     */
    public static String trim(String source) {
        String result = "";
        result = source.substring(1, source.length() - 1);
        return result;
    }

    /**
     * 判断string是否能转化为int
     *
     * @param string
     *
     * @return
     */
    public static boolean isStr2Num(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 获取for expression的值（类似i<10）
     *
     * @param expression
     * @param variables
     */
    public static void getBound(Expression expression, List<ParseFor> variables) {
        if (expression instanceof InfixExpression) {
            InfixExpression infixExpression = (InfixExpression) expression;
            String var_name = infixExpression.getLeftOperand().toString();
            String var_bound = infixExpression.getRightOperand().toString();
            String var_op = infixExpression.getOperator().toString();
            for (int j = 0; j < variables.size(); j++) {
                if (var_name.equals(variables.get(j).getVarName())) {
                    if (var_op.contains("=")) {
                        variables.get(j).setVarBound(var_bound);
                    } else if (var_op.equals(">")) {
                        if (isStr2Num(var_bound)) {
                            variables.get(j).setVarBound(String.valueOf(Integer.parseInt(var_bound) + 1));
                        } else {
                            variables.get(j).setVarBound(var_bound + "+1");
                        }
                    } else if (var_op.equals("<")) {
                        if (isStr2Num(var_bound)) {
                            variables.get(j).setVarBound(String.valueOf(Integer.parseInt(var_bound) - 1));
                        } else {
                            variables.get(j).setVarBound(var_bound + "-1");
                        }
                    }
                }
            }
            getBound(infixExpression.getLeftOperand(), variables);
            getBound(infixExpression.getRightOperand(), variables);
        }
    }

    /**
     * 获取for循环条件，用于生成for i : Integer.subrange(0,10)
     *
     * @param initializer
     * @param forExpression
     *
     * @return
     */
    public static List<ParseFor> getVariables(List initializer, Expression forExpression) {
        List<ParseFor> variables = new ArrayList<>();

        VariableDeclarationExpression var = (VariableDeclarationExpression) initializer.get(0);
        String var_type = var.getType().toString();

        String split_Str = StringUtils.trim(var.fragments().toString().replace(" ", ""));
        String[] strings = split_Str.split(",");
        for (String str : strings) {
            int split_pos = str.indexOf('=');
            String var_name = str.substring(0, split_pos);
            String var_init = str.substring(split_pos + 1, str.length());
            ParseFor parseFor = new ParseFor();
            parseFor.setVarName(var_name);
            parseFor.setVarType(var_type);
            parseFor.setVarInit(var_init);
            variables.add(parseFor);
        }

        getBound(forExpression, variables);

        return variables;
    }


    /**
     * 生成for i : Integer.subrange(0,10)
     *
     * @param initializer
     * @param expression
     *
     * @return
     */
    public static String forExpression(List initializer, Expression expression) {
        String result = "";
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        List<ParseFor> variables = getVariables(initializer, expression);
        for (int i = 0; i < variables.size(); i++) {
            ParseFor parseFor = variables.get(i);
            line = parseFor.getVarName() + " : Integer.subrange(" + parseFor.getVarInit() + "," + parseFor.getVarBound() + ")";
            stringBuilder.append(line);
            if (i < variables.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        result = stringBuilder.toString();
        return result;
    }

    /**
     * 用于处理catch语句
     *
     * @param catchClauses
     * @param space
     *
     * @return
     */
    public static String catchClause(List catchClauses, String space) {
        String result = "";
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        String line1, line2;
        for (int i = 0; i < catchClauses.size(); i++) {
            String[] re = catchClauses.get(i).toString().split("\n");
            for (int j = 0; j < re.length; j++) {
                line1 = space + re[j].replace(";", "");
                stringBuilder1.append(line1);
                if (j < re.length - 1) {
                    stringBuilder1.append("\n");
                }
            }
            line2 = stringBuilder1.toString();
            stringBuilder2.append(line2);
            if (i < catchClauses.size() - 1) {
                stringBuilder2.append("\n");
            }
        }
        result = stringBuilder2.toString();
        return result;
    }

    /**
     * 判断是否为有效文件名
     *
     * @param fileName
     * @return
     */
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255)
            return false;
        else
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

    public static String paramToString(List param) {
        String result = "";
        if (param.size() == 0) {
            return result;
        } else {
            for (int i = 0; i < param.size(); i++) {
                String tempStr = param.get(i).toString();
                int spaceIndex = tempStr.indexOf(' ');
                int endIndex = tempStr.length();
                result += tempStr.substring(spaceIndex+1, endIndex) + " : " + tempStr.substring(0, spaceIndex);
                if (i < param.size()-1) {
                    result += ", ";
                }
            }
            return result;
        }
    }
}
