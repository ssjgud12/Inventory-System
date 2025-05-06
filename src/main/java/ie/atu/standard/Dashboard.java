package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;
import java.util.Scanner;

public class Dashboard {

    public static void show(Scanner scanner, String role) {
        boolean loggedIn = true;
        role = role.trim().toLowerCase();

        while (loggedIn) {
            displayMenu(role);
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleFirstOption(scanner, role);
                    break;
                case "2":
                    handleSecondOption(scanner, role);
                    break;
                case "3":
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("That wasn't a valid choice. Please try again.");
            }
        }
    }

    private static void displayMenu(String role) {
        System.out.println("\n=== " + capitalize(role) + " Dashboard ===");
        switch (role) {
            case "admin":
                System.out.println("1. View Users\n2. Manage Products\n3. Logout");
                break;
            case "manager":
                System.out.println("1. View Stock Reports\n2. View All Orders\n3. Logout");
                break;
            default:
                System.out.println("1. View & Buy Products\n2. View Basket\n3. Logout");
        }
    }

    private static void handleFirstOption(Scanner scanner, String role) {
        switch (role) {
            case "admin":
                viewUsers();
                break;
            case "manager":
                viewStockReports();
                break;
            default:
                viewProducts(scanner);
        }
    }

    private static void handleSecondOption(Scanner scanner, String role) {
        switch (role) {
            case "admin":
                manageProducts();
                break;
            case "manager":
                viewCustomerOrders();
                break;
            default:
                System.out.print("Please enter your username to view your basket: ");
                String username = scanner.nextLine().trim();
                viewBasket(username);
        }
    }

    private static void viewUsers() {
        System.out.println("Viewing all registered users.");
    }

    private static void manageProducts() {
        System.out.println("Product management area.");
    }

    private static void viewStockReports() {
        String query = "SELECT Product_Name, Stock_Quantity FROM product";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== Current Stock Levels ===");
            System.out.printf("%-25s %-10s%n", "Product", "Stock");
            System.out.println("-------------------------------");

            while (rs.next()) {
                String name = rs.getString("Product_Name");
                int stock = rs.getInt("Stock_Quantity");
                System.out.printf("%-25s %-10d%n", name, stock);
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve stock report.");
            e.printStackTrace();
        }
    }

    private static void viewCustomerOrders() {
        String query = "SELECT o.Order_ID, u.Username, o.Total_Amount, o.Order_Date " +
                "FROM orders o JOIN user u ON o.User_ID = u.User_ID " +
                "ORDER BY o.Order_Date DESC";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== Order History ===");
            System.out.printf("%-10s %-15s %-10s %-20s%n", "Order ID", "Username", "Total (€)", "Date");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-15s %-10.2f %-20s%n",
                        rs.getInt("Order_ID"),
                        rs.getString("Username"),
                        rs.getDouble("Total_Amount"),
                        rs.getTimestamp("Order_Date").toString()
                );
            }

        } catch (SQLException e) {
            System.out.println("Could not load customer orders.");
            e.printStackTrace();
        }
    }

    private static void viewProducts(Scanner scanner) {
        String query = "SELECT Product_ID, Product_Name, Price, Stock_Quantity FROM product";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== Product Catalog ===");
            System.out.printf("%-5s %-25s %-10s %-12s%n", "ID", "Product", "Price (€)", "Stock");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("Product_ID");
                String name = rs.getString("Product_Name");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock_Quantity");
                System.out.printf("%-5d %-25s %-10.2f %-12d%n", id, name, price, stock);
            }

            System.out.print("Enter Product ID to add to basket or 0 to cancel: ");
            int productId = Integer.parseInt(scanner.nextLine().trim());
            if (productId > 0) {
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter your username: ");
                String username = scanner.nextLine().trim();
                addToBasket(username, productId, qty);
            }

        } catch (SQLException e) {
            System.out.println("Failed to load products.");
            e.printStackTrace();
        }
    }

    private static void addToBasket(String username, int productId, int qty) {
        String getUserId = "SELECT User_ID FROM user WHERE Username = ?";
        String insertBasket = "INSERT INTO basket (User_ID, Product_ID, Quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(getUserId);
             PreparedStatement basketStmt = conn.prepareStatement(insertBasket)) {

            userStmt.setString(1, username);
            ResultSet rs = userStmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("User_ID");
                basketStmt.setInt(1, userId);
                basketStmt.setInt(2, productId);
                basketStmt.setInt(3, qty);
                basketStmt.executeUpdate();
                System.out.println("Item added to basket.");
            } else {
                System.out.println("Username not found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to add to basket.");
            e.printStackTrace();
        }
    }

    private static void viewBasket(String username) {
        String query = "SELECT p.Product_Name, b.Quantity, (p.Price * b.Quantity) AS Total " +
                "FROM basket b JOIN product p ON b.Product_ID = p.Product_ID " +
                "JOIN user u ON b.User_ID = u.User_ID WHERE u.Username = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=== Basket Contents ===");
            System.out.printf("%-25s %-10s %-10s%n", "Product", "Qty", "Total (€)");
            System.out.println("------------------------------------------------");

            double totalAmount = 0;
            while (rs.next()) {
                String name = rs.getString("Product_Name");
                int qty = rs.getInt("Quantity");
                double total = rs.getDouble("Total");
                totalAmount += total;
                System.out.printf("%-25s %-10d %-10.2f%n", name, qty, total);
            }

            if (totalAmount > 0) {
                System.out.printf("\nTotal Basket Amount: €%.2f\n", totalAmount);
            } else {
                System.out.println("Your basket is empty.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve basket.");
            e.printStackTrace();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}


