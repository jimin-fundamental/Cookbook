package cookbook.repository;

import cookbook.DatabaseManager;

import java.sql.*;

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

    public boolean checkUser(String username, String password){

        String sql = "SELECT COUNT(*) FROM User WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // set values
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            connection.close();

            // check the results
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


