package com.one.component;

import com.one.domain.ResultInfo;
import com.one.listener.ActionDoneListener;
import com.one.net.FailListener;
import com.one.net.GetUtils;
import com.one.net.SuccessListener;
import com.one.util.JsonUtils;
import com.one.util.ResultInfoData2Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class BookManageComponent extends Box {
    final int WIDTH=850;
    final int HEIGHT=600;

    JFrame frame = null;
    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private DefaultTableModel tableModel;

    public BookManageComponent(JFrame frame){
        super(BoxLayout.Y_AXIS);
        this.frame = frame;
        JPanel panel1 = new JPanel();
        Color color = new Color(203,220,217);
        panel1.setBackground(color);
        panel1.setMaximumSize(new Dimension(WIDTH,80));
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addBtn = new JButton("add");
        JButton updateBtn = new JButton("edit");
        JButton deleteBtn = new JButton("delete");

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddBookDialog(frame, "add book", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        requestData();
                    }
                }).setVisible(true);
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow==-1){
                    JOptionPane.showMessageDialog(frame,"chose!");
                    return;
                }

                String id = tableModel.getValueAt(selectedRow, 0).toString();


                new UpdateBookDialog(frame, "edit book", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        requestData();
                    }
                },id).setVisible(true);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow==-1){
                    JOptionPane.showMessageDialog(frame,"chose!");
                    return;
                }

                int result = JOptionPane.showConfirmDialog(frame, "Are you sure", "yes", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION){
                    return;
                }

                String id = tableModel.getValueAt(selectedRow, 0).toString();

                GetUtils.get("http://localhost:8080/book/deleteBookById?id=" + id, new SuccessListener() {
                    @Override
                    public void success(String result) {
                        ResultInfo info = JsonUtils.parseResult(result);
                        if (info.isFlag()){
                            JOptionPane.showMessageDialog(frame,"删除成功");
                            requestData();
                        }else{
                            JOptionPane.showMessageDialog(frame,info.getMessage());
                        }

                    }
                }, new FailListener() {
                    @Override
                    public void fail() {
                        JOptionPane.showMessageDialog(frame,"fail!");
                    }
                });

            }
        });

        panel1.add(addBtn);
        panel1.add(updateBtn);
        panel1.add(deleteBtn);

        this.add(panel1);
        
        String[] ts = {"编号","书名","简介","作者","价格","库存"};
        titles = new Vector<>();
        for (String title : ts) {
            titles.add(title);
        }
        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData,titles);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
  
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);

        requestData();
    }

 
    public void requestData(){
        GetUtils.get("http://localhost:8080/book/findAllBook", new SuccessListener() {
            @Override
            public void success(String result) {
                ResultInfo info = JsonUtils.parseResult(result);
                Vector<Vector> vectors = ResultInfoData2Vector.convertResultInfoData2Vector(info);

                tableData.clear();
                for (Vector vector : vectors) {
                    tableData.add(vector);
                }

                tableModel.fireTableDataChanged();
            }
        }, new FailListener() {
            @Override
            public void fail() {

            }
        });
    }

}
