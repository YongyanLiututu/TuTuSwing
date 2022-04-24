package com.one.ui;

import com.one.component.BookManageComponent;
import com.one.domain.ResultInfo;
import com.one.net.FailListener;
import com.one.net.GetUtils;
import com.one.net.SuccessListener;
import com.one.util.JsonUtils;
import com.one.util.PathUtils;
import com.one.util.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ManagerInterface {
    JFrame frame = new JFrame("TuTu图书馆，管理页面！");

    final int WIDTH = 1300;
    final int HEIGHT = 600;
    
    public void init() throws Exception {
        //给窗口设置属性
        frame.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setIconImage(ImageIO.read(new File(PathUtils.getRealPath("loge.png"))));

        //设置菜单栏
        JMenuBar menu = new JMenuBar();
        JMenu jmenu = new JMenu("setting");
        JMenuItem m1 = new JMenuItem("切换");
        JMenuItem m2 = new JMenuItem("退出");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//回到主页面
                    new MainInterface().init();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //右上角退出
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jmenu.add(m1);
        jmenu.add(m2);
        menu.add(jmenu);

        frame.setJMenuBar(menu);
        //分页操作
        JSplitPane sp = new JSplitPane();
        
        sp.setContinuousLayout(true);
        sp.setDividerLocation(178);
        sp.setDividerSize(6);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("main-manager");
        DefaultMutableTreeNode userManage = new DefaultMutableTreeNode("user-manager");
        DefaultMutableTreeNode b1 = new DefaultMutableTreeNode("book");
        DefaultMutableTreeNode b2 = new DefaultMutableTreeNode("borrow");
        DefaultMutableTreeNode s = new DefaultMutableTreeNode("count");
        root.add(userManage);
        root.add(b1);
        root.add(b2);
        root.add(s);
        Color color = new Color(203,220,217);
        JTree tree = new JTree(root);
        MyRenderer myRenderer = new MyRenderer();
        myRenderer.setBackgroundNonSelectionColor(color);
        myRenderer.setBackgroundSelectionColor(new Color(140,140,140));
        tree.setCellRenderer(myRenderer);

        tree.setBackground(color);
        tree.setSelectionRow(2);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();

                if (userManage.equals(lastPathComponent)){
                    sp.setRightComponent(new JLabel("User-manager"));
                    sp.setDividerLocation(200);
                }else if (b1.equals(lastPathComponent)){
                    sp.setRightComponent(new BookManageComponent(frame));
                    sp.setDividerLocation(200);
                } if (b2.equals(lastPathComponent)){
                    sp.setRightComponent(new JLabel("borrow"));
                    sp.setDividerLocation(200);
                } if (s.equals(lastPathComponent)){
                    sp.setRightComponent(new JLabel("count"));
                    sp.setDividerLocation(200);
                }

            }
        });


        sp.setRightComponent(new BookManageComponent(frame));
        sp.setLeftComponent(tree);
        frame.add(sp);
        frame.setVisible(true);

        GetUtils.get("http://localhost:8080/user/findUsername", new SuccessListener() {
            @Override
            public void success(String result) {
                ResultInfo info = JsonUtils.parseResult(result);
                if (info.isFlag()){
                    //获取成功
                    frame.setTitle(info.getData()+",欢迎您来到图书馆！");
                }
            }
        }, new FailListener() {
            @Override
            public void fail() {

            }
        });

    }

    public static void main(String[] args) {
        try {
            new ManagerInterface().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //图形化
    private class MyRenderer extends DefaultTreeCellRenderer {
        private ImageIcon rootIcon = null;
        private ImageIcon userManageIcon = null;
        private ImageIcon Icon1 = null;
        private ImageIcon Icon2 = null;
        private ImageIcon Icon3 = null;

        public MyRenderer() {
            rootIcon = new ImageIcon(PathUtils.getRealPath("systemManage.png"));
            userManageIcon = new ImageIcon(PathUtils.getRealPath("userManage.png"));
            Icon1 = new ImageIcon(PathUtils.getRealPath("b1.png"));
            Icon2 = new ImageIcon(PathUtils.getRealPath("b2.png"));
            Icon3 = new ImageIcon(PathUtils.getRealPath("s.png"));

        }

        //当绘制树的每个结点时，都会调用这个方法
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            //使用默认绘制
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ImageIcon image = null;
            switch (row) {
                case 0:
                    image = rootIcon;
                    break;
                case 1:
                    image = userManageIcon;
                    break;
                case 2:
                    image = Icon1;
                    break;
                case 3:
                    image = Icon2;
                    break;
                case 4:
                    image = Icon3;
                    break;
            }

            this.setIcon(image);
            return this;
        }
    }
}
