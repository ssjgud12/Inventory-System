package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;

public class LoginExample {
    public static void main(String[] args) {
        // Simulated login input — this must match the user in your database
        Login login = new Login("guest", "guest1");

        String query = "SELECT Username, Email FROM user WHERE Username = ? AND Password = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, login.getUsername());
            stmt.setString(2, login.getPassword());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("Username");
                String email = rs.getString("Email");

                Account account = new Account(username, email);
                System.out.println("✅ Login successful!");
                System.out.println("Welcome " + account.getUsername() + ", Email: " + account.getEmail());
            } else {
                System.out.println("❌ Login failed: Invalid username or password.");
            }
//changes
        } catch (SQLException e) {
            System.out.println("🚨 Database error:");
            e.printStackTrace();
        }
    }
}