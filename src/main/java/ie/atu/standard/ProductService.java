package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;
import java.util.Scanner;

public class ProductService {

    public static void viewAndBuyProducts(Scanner scanner) {
        String query = "SELECT Product_ID, Product_Name, Price, Stock_Quantity FROM product";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== Product Catalog ===");
            System.out.printf("%-5s %-25s %-10s %-10s%n", "ID", "Product", "Price (€)", "Stock");
            System.out.println("--------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("Product_ID");
                String name = rs.getString("Product_Name");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock_Quantity");
                System.out.printf("%-5d %-25s %-10.2f %-10d%n", id, name, price, stock);
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
            System.out.println("Error loading products.");
            e.printStackTrace();
        }
    }

    public static void manageProducts(Scanner scanner) {
        String query = "SELECT Product_ID, Product_Name, Price, Stock_Quantity FROM product";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== Product List ===");
            System.out.printf("%-5s %-25s %-10s %-10s%n", "ID", "Product", "Price (€)", "Stock");
            System.out.println("--------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-25s %-10.2f %-10d%n",
                        rs.getInt("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock_Quantity"));
            }

            System.out.println("\n1. Add Product\n2. Delete Product\n3. Cancel");
            System.out.print("Choose an action: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addProduct(scanner);
                case "2" -> deleteProduct(scanner);
                default -> System.out.println("Returning to dashboard.");
            }

        } catch (SQLException e) {
            System.out.println("Error loading product list.");
            e.printStackTrace();
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter stock quantity: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());

        String insert = "INSERT INTO product (Product_Name, Price, Stock_Quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, stock);
            stmt.executeUpdate();

            System.out.println("Product added successfully.");

        } catch (SQLException e) {
            System.out.println("Failed to add product.");
            e.printStackTrace();
        }
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        String delete = "DELETE FROM product WHERE Product_ID = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(delete)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Product deleted.");
            } else {
                System.out.println("Product ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to delete product.");
            e.printStackTrace();
        }
    }
}
