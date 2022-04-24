package Swing1;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Process {


        JFrame frame = new JFrame("练习测试 Exercise by tutu");

        JCheckBox notcertain = new JCheckBox("no_certain");
        JCheckBox noBorder = new JCheckBox("没有边框");
        JProgressBar b = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        BoundedRangeModel model = b.getModel();

        public void init(){
            notcertain.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean selected = notcertain.isSelected();
                    //设置当前进度条不确定进度
                    b.setIndeterminate(selected);
                    b.setStringPainted(!selected);

                }
            });

            noBorder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //监听器
                    boolean selected = noBorder.isSelected();
                    b.setBorderPainted(!selected);
                }
            });

            Box box = Box.createVerticalBox();
            box.add(notcertain);
            box.add(noBorder);

            b.setStringPainted(true);
            b.setBorderPainted(true);

            //FlowLayout
            frame.setLayout(new FlowLayout());
            frame.add(box);
            frame.add(b);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            //开启子线程，模拟耗时操作
            SimulaterActivity simulaterActivity = new SimulaterActivity(b.getMaximum());
            new Thread(simulaterActivity).start();

            //设置一个定时任务
            Timer timer = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //读取线程任务对象的当前完成量，设置给进度条
                    int current = simulaterActivity.getCurrent();
                    //b.setValue(current);
                    model.setValue(current);
                }
            });
            timer.start();

            //监听进度条的任务变化
            model.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int value = model.getValue();
                    if (value == simulaterActivity.getAmount()){
                        timer.stop();
                    }
                }
            });


        }

        private class SimulaterActivity implements  Runnable{

            private volatile int current;
            private int amount;
            public SimulaterActivity(int amount) {
                this.amount = amount;
            }
            public int getAmount() {
                return amount;
            }
            public void setAmount(int amount) {
                this.amount = amount;
            }
            public int getCurrent() {
                return current;
            }
            public void setCurrent(int current) {
                this.current = current;
            }

            @Override
            public void run() {
                //子线程的任务 -- 模拟耗时操作
                while(current<amount){
                    try {
                         Thread.currentThread().sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    current++;
                }
            }
        }

        public static void main(String[] args) {
            new Process().init();
        }
    }

