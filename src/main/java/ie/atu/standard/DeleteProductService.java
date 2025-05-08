package ie.atu.standard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import ie.atu.pool.DatabaseUtils;

public class DeleteProductService {

    public static void deleteProduct(Scanner scanner) {
        System.out.print("\nEnter the Product ID to delete: ");
        int productId = Integer.parseInt(scanner.nextLine().trim());

        String query = "DELETE FROM product WHERE Product_ID = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found or could not be deleted.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to delete product.");
            e.printStackTrace();
        }
    }
}
