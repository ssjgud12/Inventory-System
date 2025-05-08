package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;

public class ViewUsers {

    public static void displayAllUsers() {
        String query = "SELECT User_ID, Username, Email, Role FROM user";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== User List ===\n");
            System.out.printf("%-8s %-15s %-25s %-10s%n", "User ID", "Username", "Email", "Role");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("User_ID");
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                String role = rs.getString("Role");

                System.out.printf("%-8d %-15s %-25s %-10s%n", id, username, email, role);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving users.");
            e.printStackTrace();
        }
    }
}
