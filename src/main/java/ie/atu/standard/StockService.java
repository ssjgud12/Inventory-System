package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;

public class StockService {

    public static void showStockReport() {
        String query = "SELECT Product_Name, Stock_Quantity FROM product";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nStock Report\n");
            System.out.printf("%-25s %-10s%n", "Product", "Stock");
            System.out.println("-------------------------------------");

            while (rs.next()) {
                System.out.printf("%-25s %-10d%n",
                        rs.getString("Product_Name"),
                        rs.getInt("Stock_Quantity"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Could not load stock report.");
            e.printStackTrace();
        }
    }
}

