package com.patikaDev.view;

import com.patikaDev.helper.Config;
import com.patikaDev.helper.Helper;
import com.patikaDev.model.Operator;
import com.patikaDev.model.User;

import javax.swing.*;

public class LoginGUI extends  JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JLabel label;
    private JButton btn_login;

    public LoginGUI(){
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.screenCenterLocation("x",getSize()), Helper.screenCenterLocation("y", getSize()));
        setDefaultCloseOperation((JFrame.DISPOSE_ON_CLOSE));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)){
                Helper.showMessage("fill");
            }else{
                User u = User.getFetch(fld_user_uname.getText(),fld_user_pass.getText());
                if (u == null){
                    Helper.showMessage("Kullanıcı bulunamadı");
                }else{
                    switch (u.getUser_type()){
                        case "operator":

                            OperatorGUI operatorGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI educatorGUI = new EducatorGUI();
                            break;
                        case "student":
                            StudentGUI studentGUI = new StudentGUI();
                            break;
                    }
                    dispose();

                }

            }
        });
    }

    public static void main(String[] args) {
        LoginGUI loginGUI = new LoginGUI();
    }
}
