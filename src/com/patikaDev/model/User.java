package com.patikaDev.model;

import com.patikaDev.helper.DbConnector;
import com.patikaDev.helper.Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String user_type;

    public User(int id, String name, String username, String password, String user_type) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.user_type = user_type;
    }

    public User() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public static ArrayList<User> getAll(){
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        User obj;
        Connection connection = DbConnector.getInstance();
        try {
            Statement st =connection.createStatement();
            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()){
                obj = new User();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUsername(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUser_type(resultSet.getString("user_type"));
                users.add(obj);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;

    }
    public static ArrayList<User> searchUserList(String query){
        ArrayList<User> users = new ArrayList<>();
        User obj;
        Connection connection = DbConnector.getInstance();
        try {
            Statement st =connection.createStatement();
            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()){
                obj = new User();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUsername(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUser_type(resultSet.getString("user_type"));
                users.add(obj);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public static String searchQuery(String name, String username, String user_type){
        String query = "SELECT * FROM users WHERE username LIKE '%{{username}}%' AND name LIKE '%{{name}}%'";
        query = query.replace("{{username}}", username);
        query = query.replace("{{name}}", name);
        if (!user_type.isEmpty()){
            query += " AND user_type = '{{type}}'";
            query = query.replace("{{type}}", user_type);
        }

        return query;
    }

    public static boolean add(String name, String username, String password, String user_type){
        String query = "INSERT INTO users (name, username, password, user_type) VALUES (?,?,?,?)";
        User findUser = User.getFetch(username);
        if (findUser != null){
            Helper.showMessage("Bu kullanıcı adı daha önce eklenmiş");
            return false;
        }

        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, username);
            pr.setString(3, password);
            pr.setString(4,user_type);
            int result = pr.executeUpdate();
            if (result == -1 ){
                Helper.showMessage("error");
            }
            return (result != -1);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean delete(int id){
        String query = "DELETE FROM users WHERE ID = ?";
        ArrayList<Course> courses = Course.getAllByUser(id);
        for (Course course : courses){
            Course.delete(course.getId());
        }

        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int result = pr.executeUpdate();



            return result != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean update(int id, String name, String username, String password, String userType){
        String query = "UPDATE users SET name = ?, username = ?, password = ?, user_type = ? WHERE id = ?";
        User findUser = User.getFetch(username);
        if (findUser != null && findUser.getId() != id){
            Helper.showMessage("Bu kullanıcı adı daha önce eklenmiş");
            return false;
        }
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2, username);
            pr.setString(3, password);
            pr.setString(4, userType);
            pr.setInt(5, id);
            int result = pr.executeUpdate();
            return result != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getFetch(String uname){
        User obj = null;
        String query = "SELECT * From users where username=?";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                obj = new User();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUsername(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUser_type(resultSet.getString("user_type"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
    public static User getFetch(String uname, String password){

        String query = "SELECT * From users where username=? AND password =?";
        User obj = null;
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            pr.setString(2, password);
            ResultSet resultSet = pr.executeQuery();

            while (resultSet.next()){
                switch (resultSet.getString("user_type")){
                    case "operator" :
                        obj = new Operator();
                        break;
                    default:
                        obj = new User();
                        break;
                }

                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUsername(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUser_type(resultSet.getString("user_type"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
    public static User getFetch(int id){
        User obj = null;
        String query = "SELECT * From users where id=?";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                obj = new User();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUsername(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUser_type(resultSet.getString("user_type"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}
