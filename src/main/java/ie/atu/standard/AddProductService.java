package ie.atu.standard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddProductService {

    public static void addProduct(Scanner scanner) {
        System.out.println("\nEnter product details to add:");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Stock Quantity: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());

        String query = "INSERT INTO product (Product_Name, Description, Price, Category_ID, Supplier_ID, Stock_Quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, categoryId);
            stmt.setInt(5, supplierId);
            stmt.setInt(6, stock);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product added successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to add product.");
            e.printStackTrace();
        }
    }
}
