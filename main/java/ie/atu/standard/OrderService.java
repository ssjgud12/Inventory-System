package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;

public class OrderService {

    public static void viewOrders() {
        String query = "SELECT o.Order_ID, u.Username, o.Total_Amount, o.Order_Date " +
                "FROM orders o JOIN user u ON o.User_ID = u.User_ID ORDER BY o.Order_Date DESC";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Orders\n");
            System.out.printf("%-10s %-15s %-10s %-20s%n", "Order ID", "Username", "Total (€)", "Date");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-15s %-10.2f %-20s%n",
                        rs.getInt("Order_ID"),
                        rs.getString("Username"),
                        rs.getDouble("Total_Amount"),
                        rs.getTimestamp("Order_Date"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Could not load orders.");
            e.printStackTrace();
        }
    }
}
