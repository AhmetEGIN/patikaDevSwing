package com.patikaDev.view;

import com.patikaDev.helper.Config;
import com.patikaDev.helper.Helper;

import javax.swing.*;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_cikis_yap;
    private JTabbedPane tabbedPane1;
    private JLabel lbl_welcome;

    public EducatorGUI(){
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.screenCenterLocation("x",getSize()), Helper.screenCenterLocation("y", getSize()));
        setDefaultCloseOperation((JFrame.DISPOSE_ON_CLOSE));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);
    }

}
