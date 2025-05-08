package ie.atu.service;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;
import java.util.Scanner;

public class ProductService {

    public static void listProducts() {
        String query = "SELECT Product_ID, Product_Name, Price, Stock_Quantity FROM product";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAvailable Products:");
            System.out.printf("%-5s %-25s %-10s %-12s%n", "ID", "Product", "Price (€)", "Stock");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("Product_ID");
                String name = rs.getString("Product_Name");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock_Quantity");
                System.out.printf("%-5d %-25s %-10.2f %-12d%n", id, name, price, stock);
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Could not load product list.");
            e.printStackTrace();
        }
    }

    public static void addProduct(Scanner scanner) {
        System.out.print("Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        String insert = "INSERT INTO product (Product_Name, Price, Stock_Quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, qty);
            stmt.executeUpdate();
            System.out.println("Product added.\n");
        } catch (SQLException e) {
            System.out.println("Error adding product.");
            e.printStackTrace();
        }
    }

    public static void editProduct(Scanner scanner) {
        System.out.print("Product ID to edit: ");
        int editId = Integer.parseInt(scanner.nextLine());
        System.out.print("New Name: ");
        String newName = scanner.nextLine();
        System.out.print("New Price: ");
        double newPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("New Stock: ");
        int newStock = Integer.parseInt(scanner.nextLine());

        String update = "UPDATE product SET Product_Name=?, Price=?, Stock_Quantity=? WHERE Product_ID=?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, newName);
            stmt.setDouble(2, newPrice);
            stmt.setInt(3, newStock);
            stmt.setInt(4, editId);
            stmt.executeUpdate();
            System.out.println("Product updated.\n");
        } catch (SQLException e) {
            System.out.println("Error updating product.");
            e.printStackTrace();
        }
    }

    public static void deleteProduct(Scanner scanner) {
        System.out.print("Product ID to delete: ");
        int delId = Integer.parseInt(scanner.nextLine());

        String delete = "DELETE FROM product WHERE Product_ID=?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(delete)) {
            stmt.setInt(1, delId);
            stmt.executeUpdate();
            System.out.println("Product deleted.\n");
        } catch (SQLException e) {
            System.out.println("Error deleting product.");
            e.printStackTrace();
        }
    }
}
