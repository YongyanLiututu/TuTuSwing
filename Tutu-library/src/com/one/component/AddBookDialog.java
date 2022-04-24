package com.one.component;

import com.one.domain.ResultInfo;
import com.one.listener.ActionDoneListener;
import com.one.net.FailListener;
import com.one.net.PostUtils;
import com.one.net.SuccessListener;
import com.one.util.JsonUtils;
import com.one.util.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AddBookDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    public AddBookDialog(JFrame jf, String title, boolean isModel, ActionDoneListener listener){
        super(jf,title,isModel);
        this.listener  = listener;
        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);

        Box vBox = Box.createVerticalBox();
        Box nBox = Box.createHorizontalBox();
        JLabel label3 = new JLabel("name：");
        JTextField nameField = new JTextField(25);
        nBox.add(label3);
        nBox.add(Box.createHorizontalStrut(20));
        nBox.add(nameField);

  
        Box stockBox = Box.createHorizontalBox();
        JLabel stockLable = new JLabel("store：");
        JTextField stockField = new JTextField(25);
        stockBox.add(stockLable);
        stockBox.add(Box.createHorizontalStrut(20));
        stockBox.add(stockField);

      
        Box aBox = Box.createHorizontalBox();
        JLabel label1 = new JLabel("aditor:");
        JTextField authorField = new JTextField(25);
        aBox.add(label1);
        aBox.add(Box.createHorizontalStrut(20));
        aBox.add(authorField);
        
        Box priceBox = Box.createHorizontalBox();
        JLabel label2 = new JLabel("Price：");
        JTextField priceField = new JTextField(25);
        priceBox.add(label2);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);
        
        Box dBox = Box.createHorizontalBox();
        JLabel label4 = new JLabel("story：");
        JTextArea descArea = new JTextArea(3,25);
        dBox.add(label4);
        dBox.add(Box.createHorizontalStrut(20));
        dBox.add(descArea);

        //按钮
        Box btnBox = Box.createHorizontalBox();
        JButton addBtn = new JButton("add:");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String stock = stockField.getText().trim();
                String author = authorField.getText().trim();
                String price = priceField.getText().trim();
                String story = descArea.getText().trim();

                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("description",story);
                params.put("author",author);
                params.put("price",price);
                params.put("stock",stock);

                PostUtils.postWithParams("http://localhost:8080/book/addBook", params, new SuccessListener() {
                    @Override
                    public void success(String result) {
                        ResultInfo info = JsonUtils.parseResult(result);
                        if (info.isFlag()){
                            JOptionPane.showMessageDialog(jf,"Success!");
                            dispose();
                            listener.done(null);
                        }else{
                            JOptionPane.showMessageDialog(jf,info.getMessage());
                        }
                    }
                }, new FailListener() {
                    @Override
                    public void fail() {
                        JOptionPane.showMessageDialog(jf,"Fail!");
                    }
                });
            }
        });


        btnBox.add(addBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(nBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(stockBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(aBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(priceBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(dBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(btnBox);
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);

    }

}
