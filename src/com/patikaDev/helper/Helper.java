package com.patikaDev.helper;

import javax.swing.*;
import java.awt.*;

public class Helper {

    public static void setLayout(){
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if ("Nimbus".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public static  int screenCenterLocation(String axis, Dimension size){
        int point = 0;
        switch (axis){
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }
        return point;

    }


    public static boolean isFieldEmpty(JTextField field){
        return  field.getText().trim().isEmpty();
    }

    public static void showMessage(String str){
        String message;
        String title;
        switch (str){
            case "fill":
                message = "Tüm alanları doldurunuz";
                title = "Hata";
                break;
            case "done":
                message = "İşlem başarılı";
                title = "Başarılı";
                break;
            case "error":
                message = "Bir hata oluştu";
                title = "Hata";
            default:
                message = str;
                title = "Mesaj";


        }
        JOptionPane.showConfirmDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);

    }

    public static boolean confirm(String str){
        optionPageTr();
        String msg;

        switch (str){
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğinize emin misiniz?";
                break;
            default:
                msg = str;
                break;
        }
        return JOptionPane.showConfirmDialog(null, msg, "Son kararın mı?", JOptionPane.YES_NO_OPTION) == 0;

    }

    public static void optionPageTr(){
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");

    }

}
