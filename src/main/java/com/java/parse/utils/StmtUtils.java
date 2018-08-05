package com.java.parse.utils;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Statement;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.swing.plaf.nimbus.State;
import java.io.PrintWriter;
import java.util.List;

/**
 * @Author:
 * @Date: 2018-07-26
 * @Description: 输出activity文件时，处理不同语句类型的类
 */
public class StmtUtils {
    //定义statement类型
    public final static int FOR_STMT = 0;
    public final static int WHILE_STMT = 1;
    public final static int IF_STMT = 2;
    public final static int EXPERSSION_STMT = 3;
    public final static int SWITCH_STMT = 4;
    public final static int BREAK_STMT = 5;
    public final static int DO_STMT = 6;
    public final static int EMPTY_STMT = 7;
    public final static int VARIABLEDEC_STMT = 8;
    public final static int RETURN_STMT = 9;
    public final static int ENHANCEDFOR_STMT = 10;
    public final static int TRY_STMT = 11;
    public final static int THROW_STMT = 12;
    public final static int SWITCHCASE_STMT = 13;
    public final static int CONTINUE_STMT = 14;

    /**
     * 获取Statement种类
     *
     * @param stmt
     *
     * @return
     */
    public static Integer getStmtType(Statement stmt) {
        Integer stmtType = -1;

        if (stmt instanceof ForStatement) {
            stmtType = FOR_STMT;
        } else if (stmt instanceof WhileStatement) {
            stmtType = WHILE_STMT;
        } else if (stmt instanceof IfStatement) {
            stmtType = IF_STMT;
        } else if (stmt instanceof ExpressionStatement) {
            stmtType = EXPERSSION_STMT;
        } else if (stmt instanceof SwitchStatement) {
            stmtType = SWITCH_STMT;
        } else if (stmt instanceof BreakStatement) {
            stmtType = BREAK_STMT;
        } else if (stmt instanceof DoStatement) {
            stmtType = DO_STMT;
        } else if (stmt instanceof EmptyStatement) {
            stmtType = EMPTY_STMT;
        } else if (stmt instanceof VariableDeclarationStatement) {
            stmtType = VARIABLEDEC_STMT;
        } else if (stmt instanceof ReturnStatement) {
            stmtType = RETURN_STMT;
        } else if (stmt instanceof EnhancedForStatement) {
            stmtType = ENHANCEDFOR_STMT;
        } else if (stmt instanceof TryStatement) {
            stmtType = TRY_STMT;
        } else if (stmt instanceof ThrowStatement) {
            stmtType = THROW_STMT;
        } else if (stmt instanceof SwitchCase) {
            stmtType = SWITCHCASE_STMT;
        } else if (stmt instanceof ContinueStatement) {
            stmtType = CONTINUE_STMT;
        }

        return stmtType;
    }

    /**
     * 根据statement种类，选择相应处理方法
     *
     * @param type
     * @param statement
     * @param space
     * @param printWriter
     */
    public static void switchStmt(Integer type, Statement statement, String space, Boolean flag, PrintWriter printWriter) {
        switch (type) {
            case FOR_STMT://For Loop
                ForStatement forStatement = (ForStatement) statement;
                List forInitList = forStatement.initializers();
                Expression forExpression = forStatement.getExpression();
                printWriter.println(space + "for " + StringUtils.forExpression(forInitList, forExpression));
                printWriter.println(space + "do");
                printWriter.println(space + "(");
                //for循环体可能包含多种语句，需要循环调用switchStmt
                Block forBlock = (Block) forStatement.getBody();
                if (forBlock != null) {
                    space += "  ";
                    for (int i = 0; i < forBlock.statements().size(); i++) {
                        Statement sub_statement = (Statement) forBlock.statements().get(i);
                        int sub_type = getStmtType(sub_statement);
                        switchStmt(sub_type, sub_statement, space, false,  printWriter);
                    }
                }
                printWriter.println(space.substring(0, space.length() - 2) + ");");
                break;
            case WHILE_STMT://While Loop
                WhileStatement whileStatement = (WhileStatement) statement;
                Expression whileExpression = whileStatement.getExpression();
                printWriter.println(space + "while " + whileExpression);
                printWriter.println(space + "do");
                printWriter.println(space + "(");
                Block whileBlock = (Block) whileStatement.getBody();
                if (whileBlock != null) {
                    space += "  ";
                    for (int i = 0; i < whileBlock.statements().size(); i++) {
                        Statement sub_statement = (Statement) whileBlock.statements().get(i);
                        int sub_type = getStmtType(sub_statement);
                        switchStmt(sub_type, sub_statement, space, false,  printWriter);
                    }
                }
                printWriter.println(space.substring(0, space.length() - 2) + ");");
                break;
            case IF_STMT://If Condition
                IfStatement ifStatement = (IfStatement) statement;
                Expression ifExpression = ifStatement.getExpression();
                printWriter.println(space + "if " + ifExpression);
                printWriter.println(space + "then");
                Block ifBlock = (Block) ifStatement.getThenStatement();
                if (ifBlock != null) {
                    space += "  ";
                    for (int i = 0; i < ifBlock.statements().size(); i++) {
                        Statement sub_statement = (Statement) ifBlock.statements().get(i);
                        int sub_type = getStmtType(sub_statement);
                        switchStmt(sub_type, sub_statement, space, false, printWriter);
                    }
                }

                if (ifStatement.getElseStatement() != null) {
                    printWriter.println(space.substring(0, space.length() - 2) + "else");
                    Statement elseIfStatement = ifStatement.getElseStatement();
                    //if-else这边 elseif语句可以为if statement 也可以是block所以分类处理
                    if (elseIfStatement instanceof IfStatement) {
                        int sub_type = getStmtType(elseIfStatement);
                        switchStmt(sub_type, elseIfStatement, space, false, printWriter);
                    } else if (elseIfStatement instanceof Block) {
                        Block elseifBlock = (Block) elseIfStatement;
                        //space += "  ";
                        for (int j = 0; j < elseifBlock.statements().size(); j++) {
                            Statement elifStatement = (Statement) elseifBlock.statements().get(j);
                            int elseifType = getStmtType(elifStatement);
                            switchStmt(elseifType, elifStatement, space, true, printWriter);
                        }
                    }

                } else {
                    printWriter.println(space.substring(0, space.length() - 2) + "else skip;");
                }

                break;
            case EXPERSSION_STMT://Expression(Assignment/Method Invocation)
                ExpressionStatement expressStmt = (ExpressionStatement) statement;
                Expression expression = expressStmt.getExpression();
                if (flag) {
                    //expression这边分为assignment和method invocation，分类处理
                    if (expression instanceof Assignment) {
                        Assignment assign = (Assignment) expression;
                        if (assign.toString().contains("+=") || assign.toString().contains("-=") ||
                                assign.toString().contains("*=") || assign.toString().contains("/=")) {
                            printWriter.println(space + assign.toString() + ";");
                        } else {
                            printWriter.println(space + assign.toString().replace("=", " := "));
                        }
                    } else if (expression instanceof MethodInvocation) {
                        MethodInvocation mi = (MethodInvocation) expression;
                        printWriter.println(space + "execute " + mi + ";");
                    } else {
                        printWriter.println(space + expression + ";");
                    }
                } else {
                    //expression这边分为assignment和method invocation，分类处理
                    if (expression instanceof Assignment) {
                        Assignment assign = (Assignment) expression;
                        if (assign.toString().contains("+=") || assign.toString().contains("-=") ||
                                assign.toString().contains("*=") || assign.toString().contains("/=")) {
                            printWriter.println(space + assign.toString());
                        } else {
                            printWriter.println(space + assign.toString().replace("=", " := "));
                        }
                    } else if (expression instanceof MethodInvocation) {
                        MethodInvocation mi = (MethodInvocation) expression;
                        printWriter.println(space + "execute " + mi);
                    } else {
                        printWriter.println(space + expression);
                    }
                }

                break;
            case SWITCH_STMT://Switch Statement
                SwitchStatement switchStatement = (SwitchStatement) statement;
                space += "  ";
                for (int i = 0; i < switchStatement.statements().size(); i++) {
                    Statement switch_statement = (Statement) switchStatement.statements().get(i);
                    int switch_type = getStmtType(switch_statement);
                    switchStmt(switch_type, switch_statement, space, false, printWriter);
                }
                break;
            case BREAK_STMT://Break Statement
                BreakStatement breakStatement = (BreakStatement) statement;
                printWriter.println(space + "break");
                break;
            case DO_STMT://Do Statement
                DoStatement doStatement = (DoStatement) statement;
                printWriter.println(space + doStatement.toString() + ";");
                break;
            case EMPTY_STMT://Empty Statement
                EmptyStatement emptyStatement = (EmptyStatement) statement;
                printWriter.println(space + emptyStatement.toString() + ";");
                break;
            case VARIABLEDEC_STMT://Variable Declaration Statement
                VariableDeclarationStatement var = (VariableDeclarationStatement) statement;
                int split_length = var.fragments().toString().indexOf('=');
                if (split_length > 1) {
                    String var_name = var.fragments().toString().substring(1, split_length);
                    printWriter.println(space + "var " + var_name + " : " + var.getType() + ";");
                    if (var.fragments().toString().contains("+=") || var.fragments().toString().contains("-=") ||
                            var.fragments().toString().contains("*=") || var.fragments().toString().contains("/=")) {
                        printWriter.println(space + var.fragments().toString() + ";");
                    } else {
                        printWriter.println(space + StringUtils.trim(var.fragments().toString().replace("=", " := ")) + ";");
                    }
                } else {
                    String var_name = StringUtils.trim(var.fragments().toString().replace(var.getType() + " ", ""));
                    printWriter.println(space + "attribute " + var_name + " : " + var.getType() + ";");
                }

                break;
            case RETURN_STMT://Return Statement
                ReturnStatement returnStatement = (ReturnStatement) statement;
                printWriter.println(space + "return " + returnStatement.getExpression());
                break;
            case ENHANCEDFOR_STMT://Enhanced For Statement
                EnhancedForStatement enhancedForStatement = (EnhancedForStatement) statement;
                printWriter.println(space + "for " + enhancedForStatement.getParameter() + " : " + enhancedForStatement.getExpression());
                printWriter.println(space + "do");
                printWriter.println(space + "(");
                Block edForBlock = (Block) enhancedForStatement.getBody();
                if (edForBlock != null) {
                    space += "  ";
                    for (int i = 0; i < edForBlock.statements().size(); i++) {
                        Statement sub_statement = (Statement) edForBlock.statements().get(i);
                        int sub_type = getStmtType(sub_statement);
                        switchStmt(sub_type, sub_statement, space, false, printWriter);
                    }
                }
                printWriter.println(space.substring(0, space.length() - 2) + ");");
                break;
            case TRY_STMT://Try Statement
                TryStatement tryStatement = (TryStatement) statement;
                printWriter.println(space + "try");
                printWriter.println(space + "{");
                Block tryBlock = tryStatement.getBody();
                if (tryBlock != null) {
                    space += "  ";
                    for (int i = 0; i < tryBlock.statements().size(); i++) {
                        Statement sub_statement = (Statement) tryBlock.statements().get(i);
                        int sub_type = getStmtType(sub_statement);
                        switchStmt(sub_type, sub_statement, space, false, printWriter);
                    }
                }
                printWriter.println(space.substring(0, space.length() - 2) + "}");
                List catchBlock = tryStatement.catchClauses();
                printWriter.println(StringUtils.catchClause(catchBlock, space.substring(0, space.length() - 2)));

                break;
            case THROW_STMT://Throw Statement
                ThrowStatement throwStatement = (ThrowStatement) statement;
                printWriter.println(space + throwStatement.toString());
                break;
            case SWITCHCASE_STMT://Switch Case Statement
                SwitchCase switchCase = (SwitchCase) statement;
                if (switchCase.isDefault()) {
                    printWriter.println(space.substring(0, space.length() - 2) + "case default : ");
                } else {
                    printWriter.println(space.substring(0, space.length() - 2) + "case " + switchCase.getExpression() + " : ");
                }
                break;
            case CONTINUE_STMT://Continue Statement
                ContinueStatement continueStatement = (ContinueStatement) statement;
                printWriter.println(space + "continue");
                break;
            default:
                printWriter.println(space + statement + ";");
                break;
        }
    }

    public static String getReturn(Block block) {
        String result = "";
        for (int i = 0; i < block.statements().size(); i++) {
            Statement statement = (Statement) block.statements().get(i);
            int type = StmtUtils.getStmtType(statement);
            if (type == RETURN_STMT) {
                ReturnStatement returnStatement = (ReturnStatement) statement;
                result += returnStatement.getExpression();
            }
        }
        return result;
    }
}