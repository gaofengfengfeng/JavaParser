import com.java.parse.ActivityParser;
import com.java.parse.JavaParse;
import com.java.parse.utils.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * @Author:
 * @Date: 2018-07-27
 * @Description: 界面设计类
 */
public class MainForm {
    private JPanel MainPanel;
    private JPanel PanelMid;
    private JPanel PanelBtm;
    private JButton ButtonDownload;
    private JButton ButtonConvertUMLClass;
    private JList jList;
    private JTextField fileText;
    private JPanel PanelMid_Btm;
    private JPanel PanelMid_Top;
    private JPanel PanelMidTop_Btm;
    private JButton FileDel;
    private JButton FileAdd;
    private JPanel PanelMidTop_Top;
    private JScrollPane jScrollPane;
    private JButton ButtonConvertActivity;
    private DefaultListModel listModel;
    private String DirPathAbsolute = "";
    private String UMLFilePathName = "";
    private String ActivityFilePathName = "";

    /**
     * 初始化窗口，并添加按钮Listener
     */
    public MainForm() {

        //添加文件按钮
        FileAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String[] fileEName = {".java", "JAVA源程序 文件(*.java)"};
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileFilter(new FileFilter() {
                    public boolean accept(File file) {
                        if (file.getName().endsWith(fileEName[0]) || file.isDirectory()) {
                            return true;
                        }
                        return false;
                    }

                    public String getDescription() {
                        return fileEName[1];
                    }
                });
                fileChooser.showDialog(null, null);

                File[] files = fileChooser.getSelectedFiles();
                if (files != null) {
                    listModel = new DefaultListModel();
                    ListModel oldModel = jList.getModel();

                    if (oldModel != null) {
                        for (int i = 0; i < oldModel.getSize(); i++) {
                            listModel.addElement(oldModel.getElementAt(i));
                        }
                    }

                    for (File file : files) {
                        if (!listModel.contains(file.getPath())) {
                            listModel.addElement(file.getPath());
                        }
                    }
                    jList.setModel(listModel);
                }
            }
        });

        // 删除文件按钮
        FileDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = jList.getSelectedIndices();
                if (selectedIndices.length == 0) {
                    JOptionPane.showMessageDialog(null, "未选中文件！", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    listModel = new DefaultListModel();
                    ListModel oldModel = jList.getModel();
                    if (oldModel != null) {
                        for (int i = 0; i < oldModel.getSize(); i++) {
                            if (!isContain(selectedIndices, i)) {
                                listModel.addElement(oldModel.getElementAt(i));
                            }
                        }
                    }
                    jList.setModel(listModel);
                }
            }

            public boolean isContain(int[] selectedIndices, int index) {
                boolean contained = false;
                for (int i = 0; i < selectedIndices.length; i++) {
                    if (index == selectedIndices[i]) {
                        contained = true;
                    }
                }
                return contained;
            }
        });

        // 获取文件夹路径按钮
        ButtonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showDialog(null, null);

                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    DirPathAbsolute = file.getAbsolutePath();
                    fileText.setText(DirPathAbsolute);
                }
            }
        });

        // java文件转换UML
        ButtonConvertUMLClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListModel pathModel = jList.getModel();
                ArrayList<String> filePath = new ArrayList<>();
                if (pathModel.getSize() != 0) {
                    try {
                        for (int i = 0; i < pathModel.getSize(); i++) {
                            filePath.add(pathModel.getElementAt(i).toString());
                        }
                        JavaParse javaParse = new JavaParse();
                        String filePathText = fileText.getText();
                        if (filePathText.endsWith("Desktop")) {
                            //JOptionPane.showMessageDialog(null, "未选择输出路径！", "错误", JOptionPane.ERROR_MESSAGE);
                            FileSystemView fsv = FileSystemView.getFileSystemView();
                            File com = fsv.getHomeDirectory();
                            if (com.getPath().contains("Desktop")) {
                                //javaParse.Convert2UMLClass(filePath, com.getPath());
                                DirPathAbsolute = com.getPath();
                            } else {
                                //javaParse.Convert2UMLClass(filePath, com.getPath() + "/Desktop");
                                DirPathAbsolute = com.getPath() + "/Desktop";
                            }
                        }
                        String fileName = JOptionPane.showInputDialog(null, "请输入文件名");
                        if (fileName != null) {
                            if (fileName.equals("")) {
                                JOptionPane.showMessageDialog(null, "请输入正确文件名！", "错误", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (!fileName.contains(".txt"))
                                    fileName = fileName + ".txt";
                                if (StringUtils.isValidFileName(fileName)) {
                                    UMLFilePathName = DirPathAbsolute + "/" + fileName;
                                    javaParse.Convert2UMLClass(filePath, UMLFilePathName);
                                } else {
                                    JOptionPane.showMessageDialog(null, "请输入正确文件名！", "错误", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "未选择文件！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // java文件转换activity
        ButtonConvertActivity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListModel pathModel = jList.getModel();
                ArrayList<String> filePath = new ArrayList<>();
                if (pathModel.getSize() != 0) {
                    try {
                        for (int i = 0; i < pathModel.getSize(); i++) {
                            filePath.add(pathModel.getElementAt(i).toString());
                        }
                        ActivityParser activityParser = new ActivityParser();
                        String filePathText = fileText.getText();
                        if (filePathText.endsWith("Desktop")) {
                            //JOptionPane.showMessageDialog(null, "未选择输出路径！", "错误", JOptionPane.ERROR_MESSAGE);
                            FileSystemView fsv = FileSystemView.getFileSystemView();
                            File com = fsv.getHomeDirectory();
                            if (com.getPath().contains("Desktop")) {
                                //activityParser.Convert2Activity(filePath, com.getPath());
                                DirPathAbsolute = com.getPath();
                            } else {
                                //activityParser.Convert2Activity(filePath, com.getPath() + "/Desktop");
                                DirPathAbsolute = com.getPath() + "/Desktop";
                            }
                        }
                        String fileName = JOptionPane.showInputDialog(null, "请输入文件名");

                        if (fileName != null) {
                            if (fileName.equals("")) {
                                JOptionPane.showMessageDialog(null, "请输入正确文件名！", "错误", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (!fileName.contains(".txt"))
                                    fileName = fileName + ".txt";
                                if (StringUtils.isValidFileName(fileName)) {
                                    ActivityFilePathName = DirPathAbsolute + "/" + fileName;
                                    activityParser.Convert2Activity(filePath, ActivityFilePathName);
                                } else {
                                    JOptionPane.showMessageDialog(null, "请输入正确文件名！", "错误", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "未选择文件！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        String dir = "";
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        if (com.getPath().contains("Desktop")) {
            //activityParser.Convert2Activity(filePath, com.getPath());
            dir = com.getPath();
        } else {
            //activityParser.Convert2Activity(filePath, com.getPath() + "/Desktop");
            dir = com.getPath() + "/Desktop";
        }
        mainForm.fileText.setText(dir);
        JFrame jFrame = new JFrame("Java Converter");
        jFrame.setSize(600, 500);
        jFrame.setPreferredSize(new Dimension(600, 500));
        jFrame.setLocationRelativeTo(null);
        jFrame.setContentPane(mainForm.MainPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}
