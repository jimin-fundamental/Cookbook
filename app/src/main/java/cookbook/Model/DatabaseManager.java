package cookbook.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private String url;
    
    
    public DatabaseManager() {
        url = "jdbc:mysql://sql11.freemysqlhosting.net/sql11698285?&user=sql11698285&password=BlmMYE2vhj&useSSL=false ";
    }

    public Connection getConnection(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public boolean insert_user(String name, String username, String password){

        String sql = "INSERT INTO sql11698285.User (name, username, password) VALUES (?, ?, ?)";

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

    public boolean check_user(String username, String password){

        String sql = "SELECT COUNT(*) FROM cookbook.users WHERE username = ? AND password = ?";

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
