package com.one.ui;

import com.one.component.BackGroundPanel;
import com.one.domain.ResultInfo;
import com.one.net.FailListener;
import com.one.net.PostUtils;
import com.one.net.SuccessListener;
import com.one.util.JsonUtils;
import com.one.util.PathUtils;
import com.one.util.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.Map;

import static javax.imageio.ImageIO.read;

public class MainInterface {

    JFrame frame = new JFrame("TuTu图书馆");

    final int WIDTH = 700;
    final int HEIGHT = 700;

    //组装视图
    public void init() throws Exception {
        frame.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        frame.setResizable(false);
//        //basepath=URLDecoder.decode(basepath,"utf-8");
//        frame.setIconImage(ImageIO.read(new File(PathUtils.getRealPath(".../images/loge.jpg"))));
        //登入图标
        //背景图
        BackGroundPanel bgPanel = new BackGroundPanel(read(new File(PathUtils.getRealPath("library.jpg"))));
        bgPanel.setBounds(0,0,WIDTH,HEIGHT);
        //组装
        Box UserBox= Box.createHorizontalBox();
        Box ViewBox = Box.createVerticalBox();
       //标签
        JLabel label1 = new JLabel("Username:");
        JTextField area = new JTextField(20);

       UserBox.add(label1);
       UserBox.add(Box.createHorizontalStrut(30));
       UserBox.add(area);
        //密码
        Box PasswordBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("Passwords：");
        JTextField pArea = new JTextField(15);

        PasswordBox.add(pLabel);
        PasswordBox.add(Box.createHorizontalStrut(20));
        PasswordBox.add(pArea);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton lBtn = new JButton("login");
        JButton rBtn = new JButton("register");

       lBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String username = area.getText().trim();
               String password = pArea.getText().trim();
               
               Map<String,String> params = new HashMap<>();
               params.put("username",username);
               params.put("password",password);

               //访问登录接口
               PostUtils.postWithParams("http://localhost:8080/user/login", params, new SuccessListener() {
                   @Override
                   public void success(String result) {
                       //result参数就是服务器响应回来的json字符串
                       ResultInfo in = JsonUtils.parseResult(result);
                       if (in.isFlag()){
                           try {
                               new ManagerInterface().init();
                               frame.dispose();
                           } catch (Exception ex) {
                               ex.printStackTrace();
                           }
                       }else{
                           JOptionPane.showMessageDialog(frame,in.getMessage());
                       }
                   }
               }, new FailListener() {
                  
                   @Override
                   public void fail() {
                        JOptionPane.showMessageDialog(frame,"不成功！");
                   }
               });

           }
       });

       rBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              
               try {
                   new RegisterInterface().init();
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
               frame.dispose();
           }
       });


        btnBox.add(lBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(rBtn);

        ViewBox.add(Box.createVerticalStrut(50));
        ViewBox.add(UserBox);
        ViewBox.add(Box.createVerticalStrut(20));
        ViewBox.add(PasswordBox);
        ViewBox.add(Box.createVerticalStrut(40));
        ViewBox.add(btnBox);

        bgPanel.add(ViewBox);
        frame.add(bgPanel);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        try {
            new MainInterface().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
