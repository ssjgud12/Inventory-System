package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;

public class UserService {

    public static void listUsers() {
        String query = "SELECT Username, Email, Role FROM user";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nRegistered Users\n");
            System.out.printf("%-20s %-25s %-15s%n", "Username", "Email", "Role");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-20s %-25s %-15s%n",
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("Role"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Could not load user list.");
            e.printStackTrace();
        }
    }
}

