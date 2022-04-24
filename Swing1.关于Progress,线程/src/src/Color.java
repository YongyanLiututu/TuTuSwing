package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Color {

    JFrame jFrame = new JFrame("colo1练习，made by tutu");

    JTextArea jta = new JTextArea("欢迎来到兔兔的页面！！",6,30);

    //声明按钮
    JButton btn = new JButton(new AbstractAction("改变文本框背景颜色") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //弹出一个颜色选择器，
            java.awt.Color result = JColorChooser.showDialog(jFrame, "颜色选择器", java.awt.Color.BLACK);

            //修改文本框背景
            jta.setBackground(result);
        }
    });



    public void init(){

        //组装视图
        jFrame.add(jta);

        jFrame.add(btn,BorderLayout.SOUTH);


        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Color().init();
    }

}

