package com.patikaDev.model;

import com.patikaDev.helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {

    private int id;
    private int user_id;
    private int patika_id;
    private String name;
    private String language;


    private Patika patika;
    private User educator;

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Course(int id, int user_id, int patika_id, String name, String language) {
        this.id = id;
        this.user_id = user_id;
        this.patika_id = patika_id;
        this.name = name;
        this.language = language;
        this.patika = Patika.getFetch(patika_id);
        this.educator = User.getFetch(user_id);
    }

    public static ArrayList<Course> getAll(){
        ArrayList<Course> courses = new ArrayList<>();
        Course obj;
        try {
            Statement st = DbConnector.getInstance().createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * FROM courses");
            int i = 0;
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int user_id = resultSet.getInt("user_id");
                int patika_id = resultSet.getInt("patika_id");
                String name = resultSet.getString("name");
                String language = resultSet.getString("language");
                obj = new Course(id, user_id, patika_id, name, language);
                courses.add(obj);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  courses;

    }

    public static boolean add(int user_id, int patika_id, String name, String language){
        String query = "INSERT INTO courses (user_id, patika_id, name, language) VALUES (?,?,?,?)";
        PreparedStatement pr = null;
        try {
            pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2,patika_id);
            pr.setString(3,name);
            pr.setString(4,language);
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public static ArrayList<Course> getAllByUser(int user_id){
        ArrayList<Course> courses = new ArrayList<>();
        Course obj;
        try {
            Statement st = DbConnector.getInstance().createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * FROM courses WHERE user_id="+user_id);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                int patika_id = resultSet.getInt("patika_id");
                String name = resultSet.getString("name");
                String language = resultSet.getString("language");
                obj = new Course(id, userId, patika_id, name, language);
                courses.add(obj);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  courses;

    }

    public static boolean delete(int id){
        String query = "DELETE FROM courses WHERE ID = ?";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int result = pr.executeUpdate();
            return result != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
