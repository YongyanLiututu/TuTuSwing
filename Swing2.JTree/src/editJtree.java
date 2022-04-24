
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

    public class editJtree {

        JFrame frame ;
        JTree tree;
        TreePath path;
        DefaultTreeModel m1;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("实验班");
        DefaultMutableTreeNode zhangsan = new DefaultMutableTreeNode("张三");
        DefaultMutableTreeNode lisi = new DefaultMutableTreeNode("李四");
        DefaultMutableTreeNode wangwu = new DefaultMutableTreeNode("王五");
        DefaultMutableTreeNode yiban = new DefaultMutableTreeNode("一班");
        DefaultMutableTreeNode erban = new DefaultMutableTreeNode("二班");
        DefaultMutableTreeNode LiuTutu = new DefaultMutableTreeNode("刘兔头");
        DefaultMutableTreeNode Guoguigui = new DefaultMutableTreeNode("郭龟龟");
        JButton addSiblingBtn = new JButton("添加兄弟结点");
        JButton addChildBtn = new JButton("添加子结点");
        JButton deleteBtn = new JButton("删除结点");
        JButton editBtn = new JButton("编辑当前结点");

        public void init(){

            //通过add()方法建立父子层级关系
            erban.add(LiuTutu);
            erban.add(Guoguigui);
            yiban.add(wangwu);
             yiban.add(zhangsan);
            yiban.add(lisi);

            root.add(yiban);
            root.add(erban);

            frame = new JFrame("欢迎来到兔兔的页面!!");
            tree = new JTree(root);


            m1 = (DefaultTreeModel) tree.getModel();
            tree.setEditable(true);

            //监听器
            MouseListener ml = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
                    if (tp!=null){
                        path = tp;
                    }
                }

                //拖入到的父结点
                @Override
                public void mouseReleased(MouseEvent e) {

                    TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
                    if (tp!=null && path!=null){
                        //阻止向子结点拖动
                        if (path.isDescendant(tp) && path!=tp){
                            JOptionPane.showMessageDialog(frame,"无法移动，请再次确认","非法移动",JOptionPane.WARNING_MESSAGE);
                        }
                        //不是向子结点移动，并且鼠标按下和松开也不是同一个结点

                        if (path!=tp){
                            //add方法内部，先将该结点从原父结点删除，然后再把该结点添加到新结点中
                            DefaultMutableTreeNode tartParentNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
                            DefaultMutableTreeNode moveNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                            tartParentNode.add(moveNode);

                            path=null;
                            tree.updateUI();
                        }
                    }
                }
            };

            //为JTree添加鼠标监听器
            tree.addMouseListener(ml);

            JPanel panel = new JPanel();

            addSiblingBtn.addActionListener(e -> {
                //获取选中结点
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

               
                if (selectedNode==null){
                    return;
                }

                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();

            
                if (parent==null){
                    return;
                }

                //创建一个新结点
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("new");

               
                int selectedIndex = parent.getIndex(selectedNode);

       
                m1.insertNodeInto(newNode,parent,selectedIndex);
                //获取从根结点到新结点的所有结点
                TreeNode[] pathToRoot = m1.getPathToRoot(newNode);

                //结点数组创建TreePath
                TreePath path1 = new TreePath(pathToRoot);

                //显示指定的tree
                tree.scrollPathToVisible(path1);


            });

            panel.add(addSiblingBtn);

            addChildBtn.addActionListener(e -> {
                //获取选中结点
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                if (selectedNode==null){
                    return ;
                }

                //创建新结点
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("new");

                //m1.insertNodeInto(newNode,selectedNode,selectedNode.getChildCount());使用TreeModel的方法添加，不需要手动刷新UI
                selectedNode.add(newNode);//使用TreeNode的方法添加，需要手动刷新UI

                //显示新结点

                TreeNode[] pathToRoot = m1.getPathToRoot(newNode);
                TreePath path1 = new TreePath(pathToRoot);
                tree.scrollPathToVisible(path1);

                //手动刷新UI
                tree.updateUI();

            });

            panel.add(addChildBtn);

            deleteBtn.addActionListener(e -> {

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                if (selectedNode!=null && selectedNode.getParent()!=null){
                    m1.removeNodeFromParent(selectedNode);
                }

            });

            panel.add(deleteBtn);

            //实现编辑结点的监听器
            editBtn.addActionListener(e -> {

                TreePath selectionPath = tree.getSelectionPath();

                if (selectionPath!=null){
                    //编辑选中结点
                    tree.startEditingAtPath(selectionPath);
                }

            });

            panel.add(editBtn);

            frame.add(new JScrollPane(tree));
            frame.add(panel, BorderLayout.SOUTH);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

        }

        public static void main(String[] args) {
            new editJtree().init();
        }
    }

