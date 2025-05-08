package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;
import java.util.*;

public class BasketService {

    public static void viewBasket(String username) {
        String query = "SELECT p.Product_Name, b.Quantity, (p.Price * b.Quantity) AS Total " +
                "FROM basket b JOIN product p ON b.Product_ID = p.Product_ID " +
                "JOIN user u ON b.User_ID = u.User_ID WHERE u.Username = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nBasket Contents\n");
            System.out.printf("%-25s %-10s %-10s%n", "Product", "Qty", "Total (€)");
            System.out.println("---------------------------------------------");

            double totalAmount = 0;
            while (rs.next()) {
                System.out.printf("%-25s %-10d %-10.2f%n",
                        rs.getString("Product_Name"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Total"));
                totalAmount += rs.getDouble("Total");
            }

            if (totalAmount > 0) {
                System.out.printf("\nTotal Basket Amount: €%.2f\n", totalAmount);
                System.out.print("Would you like to checkout? (yes/no): ");
                Scanner input = new Scanner(System.in);
                if (input.nextLine().trim().equalsIgnoreCase("yes")) {
                    checkoutBasket(username);
                }
            } else {
                System.out.println("Your basket is empty.\n");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve basket.");
            e.printStackTrace();
        }
    }

    public static void checkoutBasket(String username) {
        String getBasketQuery = "SELECT b.Product_ID, b.Quantity, p.Price FROM basket b " +
                "JOIN user u ON b.User_ID = u.User_ID JOIN product p ON b.Product_ID = p.Product_ID WHERE u.Username = ?";
        String getUserIdQuery = "SELECT User_ID FROM user WHERE Username = ?";
        String insertOrder = "INSERT INTO orders (User_ID, Total_Amount) VALUES (?, ?)";
        String insertOrderItem = "INSERT INTO order_items (Order_ID, Product_ID, Quantity, Subtotal) VALUES (?, ?, ?, ?)";
        String deleteBasket = "DELETE FROM basket WHERE User_ID = ?";
        String updateStock = "UPDATE product SET Stock_Quantity = Stock_Quantity - ? WHERE Product_ID = ? AND Stock_Quantity >= ?";
        String insertTransaction = "INSERT INTO inventory_transaction (Product_ID, User_ID, Transaction_Type, Quantity) VALUES (?, ?, 'Stock Out', ?)";

        try (Connection conn = DatabaseUtils.getConnection()) {
            conn.setAutoCommit(false);
            int userId = -1;
            try (PreparedStatement userStmt = conn.prepareStatement(getUserIdQuery)) {
                userStmt.setString(1, username);
                ResultSet rs = userStmt.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("User_ID");
                } else {
                    System.out.println("User not found.");
                    return;
                }
            }

            try (PreparedStatement basketStmt = conn.prepareStatement(getBasketQuery)) {
                basketStmt.setString(1, username);
                ResultSet rs = basketStmt.executeQuery();

                double total = 0;
                class Item {
                    int productId, quantity;
                    double subtotal;
                    Item(int productId, int quantity, double subtotal) {
                        this.productId = productId;
                        this.quantity = quantity;
                        this.subtotal = subtotal;
                    }
                }
                List<Item> items = new ArrayList<>();

                while (rs.next()) {
                    int pid = rs.getInt("Product_ID");
                    int qty = rs.getInt("Quantity");
                    double price = rs.getDouble("Price");
                    double sub = price * qty;
                    total += sub;
                    items.add(new Item(pid, qty, sub));
                }

                if (items.isEmpty()) {
                    System.out.println("Basket is empty. Nothing to checkout.");
                    return;
                }

                try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    orderStmt.setInt(1, userId);
                    orderStmt.setDouble(2, total);
                    orderStmt.executeUpdate();
                    ResultSet keys = orderStmt.getGeneratedKeys();
                    if (keys.next()) {
                        int orderId = keys.getInt(1);
                        try (PreparedStatement itemStmt = conn.prepareStatement(insertOrderItem);
                             PreparedStatement stockStmt = conn.prepareStatement(updateStock);
                             PreparedStatement txStmt = conn.prepareStatement(insertTransaction)) {
                            for (Item item : items) {
                                itemStmt.setInt(1, orderId);
                                itemStmt.setInt(2, item.productId);
                                itemStmt.setInt(3, item.quantity);
                                itemStmt.setDouble(4, item.subtotal);
                                itemStmt.addBatch();

                                stockStmt.setInt(1, item.quantity);
                                stockStmt.setInt(2, item.productId);
                                stockStmt.setInt(3, item.quantity);
                                stockStmt.addBatch();

                                txStmt.setInt(1, item.productId);
                                txStmt.setInt(2, userId);
                                txStmt.setInt(3, item.quantity);
                                txStmt.addBatch();
                            }
                            itemStmt.executeBatch();
                            stockStmt.executeBatch();
                            txStmt.executeBatch();
                        }
                        try (PreparedStatement del = conn.prepareStatement(deleteBasket)) {
                            del.setInt(1, userId);
                            del.executeUpdate();
                        }
                        conn.commit();
                        System.out.println("Checkout complete. Order placed and stock updated.\n");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Checkout failed.");
            e.printStackTrace();
        }
    }
}
