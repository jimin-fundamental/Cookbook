package cookbook.repository;

import cookbook.DatabaseManager;
import cookbook.model.Help;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpDao implements HelpRepository{
    private DatabaseManager dbManager;
    private String url;

    public HelpDao(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.url = "jdbc:mysql://srv1145.hstgr.io/u689018343_cookbook?&user=u689018343_nulla&password=TheWorldOfNull1&useSSL=false";
    }

    
    public List<Help> getAllHelp(){
        List<Help> allHelpEntries = new ArrayList<>();
        String sql = "SELECT id, title, text FROM HelpEntries";
        System.out.println("Attempting to fetch all help entries from the database.");
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("text");
    
                Help helpEntry = new Help(id, title, description);
                allHelpEntries.add(helpEntry);
                System.out.println("Help entry added: " + helpEntry.getId() + " - " + helpEntry.getTitle());
            }
            System.out.println("Total help entries fetched: " + allHelpEntries.size());
    
        } catch (SQLException e) {
            System.out.println("Error fetching help entries from database.");
            e.printStackTrace();
            return null;
        }
    
        return allHelpEntries;
    }
    
    
}


