package com.one.basic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    JFrame YeMian=new JFrame("made by TUTU");
    JMenuBar menuBar=new JMenuBar();
    JMenu jMenu=new JMenu("背景选择");
    JTextArea area = new JTextArea("欢迎来到兔兔的页面！！",6,30);
//    JMenu fMenue = new JMenu("文件");



    JMenuItem open = new JMenuItem(new AbstractAction("打开") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showOpenDialog(YeMian);

            //获取用户选择的文件
            File file = fileChooser.getSelectedFile();

            //进行显示
            try {
                image = ImageIO.read(file);
                drawArea.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    });

    JMenuItem save = new JMenuItem(new AbstractAction("另存为") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showSaveDialog(YeMian);

            //获取用户选择的保存文件路径
            File file = fileChooser.getSelectedFile();

            try {
                ImageIO.write(image,"jpeg",file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    });



    BufferedImage image;

    //swing提供的组件，都使用了图像缓冲区技术
    private class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            //把图片绘制到组件上即可
            g.drawImage(image,0,0,null);
        }
    }

    MyCanvas drawArea = new MyCanvas();

    JButton btn = new JButton(new AbstractAction("改变文本框背景颜色") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //弹出一个颜色选择器，

            Color result = JColorChooser.showDialog(YeMian, "颜色选择器", Color.white);

            //修改文本框背景
            YeMian.setBackground(result);
        }
    });

    public void init(){
//        Box topLeft = Box.createVerticalBox();
//        topLeft.add(area);
        YeMian.add(area,BorderLayout.SOUTH);

//        JPanel selectPanel = new JPanel();
        jMenu.add(btn);
        jMenu.addSeparator();
//        f.add(top);

        jMenu.add(open);
        jMenu.add(save);
        menuBar.add(menuBar);
        YeMian.setJMenuBar(menuBar);

        YeMian.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        YeMian.pack();
        YeMian.setVisible(true);
    }


    public static void main(String[] args) {
        new Background().init();

    }

}

