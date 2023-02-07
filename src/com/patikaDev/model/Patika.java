package com.patikaDev.model;

import com.patikaDev.helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Patika {

    private int id;
    private String name;

    public Patika(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Patika() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<Patika> getAll(){
        ArrayList<Patika> patikas = new ArrayList<>();
        Patika obj;
        try {
            Statement st = DbConnector.getInstance().createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * FROM patikas");
            while (resultSet.next()){
                obj = new Patika();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                patikas.add(obj);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patikas;

    }

    public static boolean add(String name){
        String query = "INSERT INTO patikas (name) VALUES (?)";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            return pr.executeUpdate() != -1;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public static boolean update(int id, String name){
        String query = "UPDATE patikas SET name=? WHERE id = ?";
        PreparedStatement pr = null;
        try {
            pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(2, id);
            pr.setString(1, name);
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public static Patika getFetch(int id){
        Patika obj = null;
        String query = "SELECT * From patikas WHERE id = ?";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                obj = new Patika();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM patikas WHERE id = ?";
        ArrayList<Course> courses = Course.getAll();
        for (Course course : courses){
            if (course.getPatika().getId() == id){
                Course.delete(course.getId());
            }
        }


        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
