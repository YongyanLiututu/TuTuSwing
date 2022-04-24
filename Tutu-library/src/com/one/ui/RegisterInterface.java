package com.one.ui;

import com.one.component.BackGroundPanel;
import com.one.domain.ResultInfo;
import com.one.net.FailListener;
import com.one.net.ImageRequestUtils;
import com.one.net.PostUtils;
import com.one.net.SuccessListener;
import com.one.util.JsonUtils;
import com.one.util.PathUtils;
import com.one.util.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterInterface {
    JFrame frame = new JFrame("Register tutu");

    final int WIDTH = 600;
    final int HEIGHT = 400;


  
    public void init() throws Exception {
        frame.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        frame.setResizable(false);
        frame.setIconImage(ImageIO.read(new File(PathUtils.getRealPath("loge.png"))));

        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File(PathUtils.getRealPath("regist.jpg"))));
        bgPanel.setBounds(0,0,WIDTH,HEIGHT);


        Box vBox = Box.createVerticalBox();


        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("Username：");
        JTextField uField = new JTextField(20);

        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);


        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("Password：");
        JTextField pField = new JTextField(20);
        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pField);
        Box tBox = Box.createHorizontalBox();
        JLabel tLabel = new JLabel("Phone-number：");
        JTextField tField = new JTextField(20);
        tBox.add(tLabel);
        tBox.add(Box.createHorizontalStrut(20));
        tBox.add(tField);
        Box gBox = Box.createHorizontalBox();
        JLabel label1 = new JLabel("sex：");
        JRadioButton maleBtn = new JRadioButton("boy",true);
        JRadioButton femaleBtn = new JRadioButton("girl",false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(maleBtn);
        bg.add(femaleBtn);

        gBox.add(label1);
        gBox.add(Box.createHorizontalStrut(20));
        gBox.add(maleBtn);
        gBox.add(femaleBtn);
        gBox.add(Box.createHorizontalStrut(120));

        //组装验证码
        Box cBox = Box.createHorizontalBox();
        JLabel cLabel = new JLabel("验证码：");
        JTextField cField = new JTextField(4);
        JLabel c = new JLabel(new ImageIcon(ImageRequestUtils.getImage("http://localhost:8080/code/getCheckCode")));
        c.setToolTipText("Refresher");
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                c.setIcon(new ImageIcon(ImageRequestUtils.getImage("http://localhost:8080/code/getCheckCode")));
                c.updateUI();
            }
        });

        cBox.add(cLabel);
        cBox.add(Box.createHorizontalStrut(20));
        cBox.add(cField);
        cBox.add(c);

        Box btnBox = Box.createHorizontalBox();
        JButton registBtn = new JButton("login");
        JButton backBtn = new JButton("return");

        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = uField.getText().trim();
                String password = pField.getText().trim();
                String phone = tField.getText().trim();
                String gender = bg.isSelected(maleBtn.getModel())? maleBtn.getText():femaleBtn.getText();
                String checkCode = cField.getText().trim();

                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                params.put("phone",phone);
                params.put("gender",gender);
                params.put("checkCode",checkCode);
                PostUtils.postWithParams("http://localhost:8080/user/register", params, new SuccessListener() {
                    @Override
                    public void success(String result) {
                        ResultInfo info = JsonUtils.parseResult(result);
                        if (info.isFlag()){
                            JOptionPane.showMessageDialog(frame,"Success!");
                            try {
                                new MainInterface().init();
                                frame.dispose();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }else{
                            JOptionPane.showMessageDialog(frame,info.getMessage());
                        }
                    }
                }, new FailListener() {
                    @Override
                    public void fail() {
                        JOptionPane.showMessageDialog(frame,"不成功！！");
                    }
                });
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    new MainInterface().init();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        btnBox.add(registBtn);
        btnBox.add(Box.createHorizontalStrut(80));
        btnBox.add(backBtn);

        vBox.add(Box.createVerticalStrut(50));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(tBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(gBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(cBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        bgPanel.add(vBox);

        frame.add(bgPanel);

        frame.setVisible(true);
    }


   /* public static void main(String[] args) {
        try {
            new RegisterInterface().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

}
