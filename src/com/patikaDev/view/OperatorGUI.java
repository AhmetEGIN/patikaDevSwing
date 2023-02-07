package com.patikaDev.view;

import com.patikaDev.helper.Config;
import com.patikaDev.helper.Helper;
import com.patikaDev.helper.Item;
import com.patikaDev.model.Course;
import com.patikaDev.model.Operator;
import com.patikaDev.model.Patika;
import com.patikaDev.model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JPanel pnl_userList;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JScrollPane scrl_userList;
    private JTable tbl_userList;
    private JPanel pnl_userForm;
    private JTextField fld_username;
    private JTextField fld_uname;
    private JPasswordField fld_password;
    private JComboBox cmb_userType;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_search_user_name;
    private JTextField fld_search_user_username;
    private JComboBox cmb_search_userType;
    private JButton btn_search_user;
    private JPanel pnl_patikaList;
    private JScrollPane scrll_patikaList;
    private JTable tbl_patikaList;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_course;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_educator;
    private JButton btn_course_add;
    private DefaultTableModel mdl_userList;
    private Object[] row_userList;
    private DefaultTableModel mdk_patikaList;
    private Object[] row_patikaList;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_List;
    private Object[] row_course_list;


    private final Operator operator;


    public OperatorGUI(Operator operator){
        this.operator = operator;
        add(wrapper);
        setSize(800,500);

        int x = Helper.screenCenterLocation("x",getSize());
        int y = Helper.screenCenterLocation("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TİTLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin: " + operator.getName());

        //ModelUserList
        mdl_userList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_userList = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_userList.setColumnIdentifiers(col_userList);
        row_userList = new Object[col_userList.length];
        loadUserTable();

        tbl_userList.setModel(mdl_userList);
        tbl_userList.getTableHeader().setReorderingAllowed(false);
        tbl_userList.getSelectionModel().addListSelectionListener(e ->{
            try{
                String selectedUserId = tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 0).toString();
                fld_user_id.setText(selectedUserId);
            }catch (Exception exception){
                System.out.println("");
            }

        });

        tbl_userList.getModel().addTableModelListener(e->{
            if (e.getType() == TableModelEvent.UPDATE){
                int userId = Integer.parseInt(tbl_userList.getValueAt(tbl_userList.getSelectedRow(),0).toString());
                String username = tbl_userList.getValueAt(tbl_userList.getSelectedRow(),1).toString();
                String user_uname = tbl_userList.getValueAt(tbl_userList.getSelectedRow(),2).toString();
                String password = tbl_userList.getValueAt(tbl_userList.getSelectedRow(),3).toString();
                String userType = tbl_userList.getValueAt(tbl_userList.getSelectedRow(),4).toString();
                if (User.update(userId, username, user_uname, password, userType)){
                    Helper.showMessage("done");

                }
                loadUserTable();
                loadEducatorCombo();
                loadCourseModel();
            }
        });
        // ## UserList  ##

        // Patika List

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_patikaList.getValueAt(tbl_patikaList.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updatePatikaGUI = new UpdatePatikaGUI(Patika.getFetch(selectedId));
            updatePatikaGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                }
            });

        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")){
                int selectedId = Integer.parseInt(tbl_patikaList.getValueAt(tbl_patikaList.getSelectedRow(), 0).toString());
                if (Patika.delete(selectedId)){
                    Helper.showMessage("done");
                    loadPatikaModel();
                }else{
                    Helper.showMessage("error");
                }
            }

        });




        mdk_patikaList = new DefaultTableModel();
        Object[] col_patikaList = {"ID", "Patika Adı"};
        mdk_patikaList.setColumnIdentifiers(col_patikaList);
        row_patikaList = new Object[col_patikaList.length];
        loadPatikaModel();
        tbl_patikaList.setModel(mdk_patikaList);
        tbl_patikaList.setComponentPopupMenu(patikaMenu);
        tbl_patikaList.getTableHeader().setReorderingAllowed(false);
        tbl_patikaList.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_patikaList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_patikaList.rowAtPoint(point);
                tbl_patikaList.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });
        // ## PatikaList

        // Course List
        mdl_course_List = new DefaultTableModel();
        Object[] col_course_list = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_List.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadCourseModel();
        tbl_course_list.setModel(mdl_course_List);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        loadPatikaCombo();
        loadEducatorCombo();


        // ##Course List


        btn_user_add.addActionListener(e -> {
                if (Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_uname) || Helper.isFieldEmpty(fld_password)){
                    Helper.showMessage("fill");
                }else {
                    String name = fld_username.getText();
                    String username = fld_uname.getText();
                    String password = fld_password.getText();
                    String userType = cmb_userType.getSelectedItem().toString();
                    if (User.add(name, username, password, userType)){
                        Helper.showMessage("done");
                        loadUserTable();
                        loadEducatorCombo();
                        fld_uname.setText(null);
                        fld_password.setText(null);
                        fld_username.setText(null);
                    }
                }
        });
        btn_user_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_id)){
                Helper.showMessage("fill");
            }else{
                Integer id = Integer.parseInt(fld_user_id.getText());

                if (Helper.confirm("sure")){
                    if (User.delete(id)){
                        Helper.showMessage("done");
                        loadUserTable();
                        loadEducatorCombo();
                        loadCourseModel();
                        fld_user_id.setText(null);
                    }else{
                        Helper.showMessage("error");
                    }

                }


            }
        });
        btn_search_user.addActionListener(e -> {
            String name = fld_search_user_name.getText();
            String username = fld_search_user_username.getText();
            String type = cmb_search_userType.getSelectedItem().toString();
            String query = User.searchQuery(name, username, type);
            ArrayList<User> searchingUsers = User.searchUserList(query);
            loadUserTable(searchingUsers);

        });
        btn_logout.addActionListener(e -> {
            LoginGUI loginGUI = new LoginGUI();
        });
        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");
            }else{
                if (Patika.add(fld_patika_name.getText())){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();

                    fld_patika_name.setText(null);
                }else{
                    Helper.showMessage("error");
                }

            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_educator.getSelectedItem();
            if (Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)){
                Helper.showMessage("fill");
            }else{
                if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())){
                    Helper.showMessage("done");
                    loadCourseModel();
                    fld_course_name.setText(null);
                    fld_course_lang.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }

        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Course obj : Course.getAll()){
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLanguage();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            mdl_course_List.addRow(row_course_list);

        }

    }

    public void loadUserTable(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_userList.getModel();
        clearModel.setRowCount(0);

        for (User obj : User.getAll()){
            int i = 0;
            row_userList[i++] = obj.getId();
            row_userList[i++] = obj.getName();
            row_userList[i++] = obj.getUsername();
            row_userList[i++] = obj.getPassword();
            row_userList[i++] = obj.getUser_type();
            mdl_userList.addRow(row_userList);
        }
    }
    public void loadUserTable(ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_userList.getModel();
        clearModel.setRowCount(0);

        for (User obj : list){
            int i = 0;
            row_userList[i++] = obj.getId();
            row_userList[i++] = obj.getName();
            row_userList[i++] = obj.getUsername();
            row_userList[i++] = obj.getPassword();
            row_userList[i++] = obj.getUser_type();
            mdl_userList.addRow(row_userList);
        }
    }
    public void loadPatikaModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patikaList.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Patika obj : Patika.getAll()){
            i = 0;
            row_patikaList[i++] = obj.getId();
            row_patikaList[i++] = obj.getName();
            mdk_patikaList.addRow(row_patikaList);
        }



    }

    public void loadEducatorCombo(){
        cmb_course_educator.removeAllItems();
        for(User obj : User.getAll()){
            if(obj.getUser_type().equals("educator")){
                cmb_course_educator.addItem(new Item(obj.getId(), obj.getName()));
            }
        }
    }
    public void loadPatikaCombo(){
        cmb_course_patika.removeAllItems();
        for(Patika obj : Patika.getAll()){
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }



    public static void main(String[] args) {
        Helper.setLayout();
        Operator op = new Operator();
        op.setId(1);
        op.setName("Mustafa");
        op.setUsername("mustafac");
        op.setPassword("123");
        op.setUser_type("operator");
        OperatorGUI operatorGUI = new OperatorGUI(op);
    }

}
