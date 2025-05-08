package ie.atu.standard;

import java.sql.*;
import java.util.Scanner;
import ie.atu.pool.DatabaseUtils;

public class EditProductService {

    public static void editProduct(Scanner scanner) {
        System.out.print("\nEnter the Product ID to edit: ");
        int productId = Integer.parseInt(scanner.nextLine().trim());

        String selectQuery = "SELECT * FROM product WHERE Product_ID = ?";
        String updateQuery = "UPDATE product SET Product_Name = ?, Description = ?, Price = ?, Category_ID = ?, Supplier_ID = ?, Stock_Quantity = ? WHERE Product_ID = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            selectStmt.setInt(1, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nCurrent Product Details:");
                System.out.println("Name: " + rs.getString("Product_Name"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("Price: " + rs.getDouble("Price"));
                System.out.println("Category ID: " + rs.getInt("Category_ID"));
                System.out.println("Supplier ID: " + rs.getInt("Supplier_ID"));
                System.out.println("Stock Quantity: " + rs.getInt("Stock_Quantity"));

                System.out.println("\nEnter new values (leave blank to keep current):");
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = rs.getString("Product_Name");

                System.out.print("Description: ");
                String desc = scanner.nextLine().trim();
                if (desc.isEmpty()) desc = rs.getString("Description");

                System.out.print("Price: ");
                String priceInput = scanner.nextLine().trim();
                double price = priceInput.isEmpty() ? rs.getDouble("Price") : Double.parseDouble(priceInput);

                System.out.print("Category ID: ");
                String catInput = scanner.nextLine().trim();
                int catId = catInput.isEmpty() ? rs.getInt("Category_ID") : Integer.parseInt(catInput);

                System.out.print("Supplier ID: ");
                String suppInput = scanner.nextLine().trim();
                int suppId = suppInput.isEmpty() ? rs.getInt("Supplier_ID") : Integer.parseInt(suppInput);

                System.out.print("Stock Quantity: ");
                String stockInput = scanner.nextLine().trim();
                int stock = stockInput.isEmpty() ? rs.getInt("Stock_Quantity") : Integer.parseInt(stockInput);

                updateStmt.setString(1, name);
                updateStmt.setString(2, desc);
                updateStmt.setDouble(3, price);
                updateStmt.setInt(4, catId);
                updateStmt.setInt(5, suppId);
                updateStmt.setInt(6, stock);
                updateStmt.setInt(7, productId);

                int updated = updateStmt.executeUpdate();
                if (updated > 0) {
                    System.out.println("Product updated successfully.");
                }
            } else {
                System.out.println("Product not found.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to update product.");
            e.printStackTrace();
        }
    }
}
