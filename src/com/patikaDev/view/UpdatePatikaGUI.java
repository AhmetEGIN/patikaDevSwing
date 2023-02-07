package com.patikaDev.view;

import com.patikaDev.helper.Config;
import com.patikaDev.helper.Helper;
import com.patikaDev.model.Patika;

import javax.swing.*;

public class UpdatePatikaGUI extends  JFrame {
    private JPanel wrapper;
    private JTextField fld_patika_name;
    private JButton btn_update;
    private Patika patika;

    public UpdatePatikaGUI(Patika patika){
        this.patika = patika;
        add(wrapper);
        setSize(300,200);
        setLocation(Helper.screenCenterLocation("x", getSize()), Helper.screenCenterLocation("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TİTLE);
        setVisible(true);


        btn_update.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");
            }else{
                if (Patika.update(patika.getId(), fld_patika_name.getText())){
                    Helper.showMessage("done");

                }
                dispose();
            }
        });
    }



}
