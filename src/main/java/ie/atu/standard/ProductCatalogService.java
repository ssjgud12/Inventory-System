package ie.atu.standard;

import java.sql.*;
import java.util.Scanner;

public class ProductCatalogService {

    public static void viewAndBuyProducts(Scanner scanner) {
        String query = "SELECT Product_ID, Product_Name, Price, Stock_Quantity FROM product";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== Product Catalog ===\n");
            System.out.printf("%-5s %-25s %-10s %-12s%n", "ID", "Product", "Price (€)", "Stock");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("Product_ID");
                String name = rs.getString("Product_Name");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock_Quantity");
                System.out.printf("%-5d %-25s %-10.2f %-12d%n", id, name, price, stock);
            }

            System.out.print("\nEnter Product ID to add to basket or 0 to cancel: ");
            int productId = Integer.parseInt(scanner.nextLine().trim());
            if (productId > 0) {
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter your username: ");
                String username = scanner.nextLine().trim();
                BasketService.addToBasket(username, productId, qty);
            }

        } catch (SQLException e) {
            System.out.println("Failed to load products.");
            e.printStackTrace();
        }
    }
}
