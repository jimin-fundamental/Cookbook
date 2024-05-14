package cookbook.repository;

import cookbook.DatabaseManager;
import cookbook.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements UserRepository{
    private DatabaseManager dbManager;
    private String url;

    public UserDao(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.url = "jdbc:mysql://srv1145.hstgr.io/u689018343_cookbook?&user=u689018343_nulla&password=TheWorldOfNull1&useSSL=false";
    }

    public boolean insertUser(String name, String username, String password){

        String sql = "INSERT INTO User (name, username, password) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // set values
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);

            int affectedRows = pstmt.executeUpdate();

            pstmt.close();
            connection.close();

            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean insertUser(String name, String username, String password, int isAdmin){

        String sql = "INSERT INTO User (name, username, password, isadmin) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // set values
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setInt(4, isAdmin);

            int affectedRows = pstmt.executeUpdate();

            pstmt.close();
            connection.close();

            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public User checkUser(String username, String password) {

        String sql = "SELECT COUNT(*), name, id, isadmin FROM User WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // set values
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            
            
            // check the results
            if (rs.next()) {
                int count = rs.getInt(1);
                String name = rs.getString("name");
                Long id = rs.getLong("id");
                int isAdmin = rs.getInt("isadmin");
                pstmt.close();
                connection.close();
                if (count > 0) {
                    User user = new User(id, name, username, password, isAdmin);
                    return user;
                }
            }
            pstmt.close();
            connection.close();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUser(){
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT id, name, username, password, isadmin FROM User";
        System.out.println("Attempting to fetch all users from the database.");
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int isAdmin = rs.getInt("isadmin");
    
                User user = new User(id, name, username, password, isAdmin);
                allUsers.add(user);
                System.out.println("User added: " + user.getId() + " - " + user.getName());
            }
            System.out.println("Total users fetched: " + allUsers.size());
    
        } catch (SQLException e) {
            System.out.println("Error fetching users from database.");
            e.printStackTrace();
            return null;
        }
    
        return allUsers;
    }

    public void editUser(User user){
        String sql = "UPDATE User SET name = ?, username = ?, password = ?, isAdmin = ? WHERE id = ?";
        System.out.println("Attempting to update user with ID: " + user.getId());
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
    
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getPassword());  // Note: Ensure that getPassword is spelled correctly in your User model.
            pstmt.setInt(4, user.getIsAdmin());
            pstmt.setLong(5, user.getId());
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                System.out.println("User updated successfully: " + user.getId());
            } else {
                System.out.println("No rows affected for user: " + user.getId());
            }
    
        } catch (SQLException e) {
            System.out.println("Error updating user: " + user.getId());
            e.printStackTrace();
        }
    }

    public void deleteUser(User user){
        String sql = "DELETE FROM User WHERE id = ?";
        System.out.println("Attempting to update user with ID: " + user.getId());
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
    
            pstmt.setLong(1, user.getId());
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                System.out.println("User updated successfully: " + user.getId());
            } else {
                System.out.println("No rows affected for user: " + user.getId());
            }
    
        } catch (SQLException e) {
            System.out.println("Error updating user: " + user.getId());
            e.printStackTrace();
        }
    }
    
    
}


