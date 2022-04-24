package com.one.component;

import com.one.domain.ResultInfo;
import com.one.listener.ActionDoneListener;
import com.one.net.FailListener;
import com.one.net.GetUtils;
import com.one.net.PostUtils;
import com.one.net.SuccessListener;
import com.one.util.JsonUtils;
import com.one.util.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UpdateBookDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    private String id;
    private ActionDoneListener listener;
    private Map<String,Object> map;

    private JTextField nameField;
    private JTextField stockField;
    private JTextField authorField;
    private JTextField priceField;
    private JTextArea descArea;

    public UpdateBookDialog(JFrame jf, String title, boolean isModel, ActionDoneListener listener,String id){
        super(jf,title,isModel);
        this.listener  = listener;
        this.id=id;
        this.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);

        Box vBox = Box.createVerticalBox();
        
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLable = new JLabel("name：");
        nameField = new JTextField(20);
        nameBox.add(nameLable);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);


        Box stockBox = Box.createHorizontalBox();
        JLabel stockLable = new JLabel("store：");
        stockField = new JTextField(20);

        stockBox.add(stockLable);
        stockBox.add(Box.createHorizontalStrut(20));
        stockBox.add(stockField);
        
        Box authorBox = Box.createHorizontalBox();
        JLabel authorLable = new JLabel("edit:");
        authorField = new JTextField(20);

        authorBox.add(authorLable);
        authorBox.add(Box.createHorizontalStrut(20));
        authorBox.add(authorField);
        
        Box priceBox = Box.createHorizontalBox();
        JLabel priceLable = new JLabel("price：");
         priceField = new JTextField(20);
        priceBox.add(priceLable);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);


        //组装图书简介
        Box descBox = Box.createHorizontalBox();
        JLabel descLable = new JLabel("story:");
        descArea = new JTextArea(3,20);

        descBox.add(descLable);
        descBox.add(Box.createHorizontalStrut(20));
        descBox.add(new JScrollPane(descArea));

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton updateBtn = new JButton("change");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText().trim();
                String stock = stockField.getText().trim();
                String author = authorField.getText().trim();
                String price = priceField.getText().trim();
                String desc = descArea.getText().trim();

                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("description",desc);
                params.put("author",author);
                params.put("price",price);
                params.put("stock",stock);
                params.put("id",map.get("id").toString());

                PostUtils.postWithParams("http://localhost:8080/book/updateBook", params, new SuccessListener() {
                    @Override
                    public void success(String result) {
                        ResultInfo info = JsonUtils.parseResult(result);
                        if (info.isFlag()){
                            JOptionPane.showMessageDialog(jf,"success");
                            dispose();
                            listener.done(null);
                        }else{

                            JOptionPane.showMessageDialog(jf,info.getMessage());
                        }
                    }
                }, new FailListener() {
                    @Override
                    public void fail() {
                       JOptionPane.showMessageDialog(jf,"Fail");
                    }
                });
            }
        });
        //TODO 处理修改的行为
        btnBox.add(updateBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(stockBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(authorBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(priceBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(descBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
        requestData();
    }


    public void requestData(){
        GetUtils.get("http://localhost:8080/book/findBookById?id=" + id, new SuccessListener() {
            @Override
            public void success(String result) {
                ResultInfo info = JsonUtils.parseResult(result);
                map = (Map<String, Object>) info.getData();
                nameField.setText(map.get("name").toString());
                stockField.setText(map.get("stock").toString());
                authorField.setText(map.get("author").toString());
                priceField.setText(map.get("price").toString());
                descArea.setText(map.get("description").toString());
            }
        }, new FailListener() {
            @Override
            public void fail() {

            }
        });
    }

}
