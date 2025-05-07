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
                    if (role.equals("admin")) {
                        viewAllOrders();
                    } else {
                        System.out.println("\nLogging out...\n");
                        loggedIn = false;
                    }
                    break;
                case "4":
                    System.out.println("\nLogging out...\n");
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
                System.out.println("1. View Users\n2. Manage Products\n3. View Orders\n4. Logout");
                break;
            case "manager":
                System.out.println("1. View Stock Reports\n2. View All Orders\n3. Logout");
                break;
            default:
                System.out.println("1. View and Purchase Products\n2. View Basket\n3. Logout");
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
                manageProducts(scanner);
                break;
            case "manager":
                viewCustomerOrders();
                break;
            default:
                System.out.print("Enter your username to view your basket: ");
                String username = scanner.nextLine().trim();
                viewBasket(username);
        }
    }

    private static void viewUsers() {
        String query = "SELECT Username, Email, Role FROM user";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nRegistered Users\n");
            System.out.printf("%-20s %-25s %-15s%n", "Username", "Email", "Role");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-20s %-25s %-15s%n",
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("Role"));
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Could not load user list.");
            e.printStackTrace();
        }
    }

    private static void manageProducts(Scanner scanner) {
        boolean managing = true;
        while (managing) {
            System.out.println("\nProduct Management");
            listProducts();
            System.out.println(); // Gap for readability
            System.out.println("1. Add Product\n2. Edit Product\n3. Delete Product\n4. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
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
                    break;
                case "2":
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
                    break;
                case "3":
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
                    break;
                case "4":
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void listProducts() {
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

    private static void viewAllOrders() {
        String query = "SELECT o.Order_ID, u.Username, o.Total_Amount, o.Order_Date FROM orders o JOIN user u ON o.User_ID = u.User_ID ORDER BY o.Order_Date DESC";
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

    private static void viewStockReports() {
        String query = "SELECT Product_Name, Stock_Quantity FROM product";
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nCurrent Stock Levels\n");
            System.out.printf("%-25s %-10s%n", "Product", "Stock");
            System.out.println("-------------------------------");

            while (rs.next()) {
                String name = rs.getString("Product_Name");
                int stock = rs.getInt("Stock_Quantity");
                System.out.printf("%-25s %-10d%n", name, stock);
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Unable to retrieve stock report.");
            e.printStackTrace();
        }
    }

    private static void viewCustomerOrders() {
        viewAllOrders(); // Using same implementation
    }

    private static void viewProducts(Scanner scanner) {
        listProducts();
        System.out.println(); // Gap before basket input
        System.out.print("Enter Product ID to add to basket or 0 to cancel: ");
        int productId = Integer.parseInt(scanner.nextLine().trim());
        if (productId > 0) {
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter your username: ");
            String username = scanner.nextLine().trim();
            addToBasket(username, productId, qty);
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
                System.out.println("Item added to basket.\n");
            } else {
                System.out.println("Username not found.\n");
            }
        } catch (SQLException e) {
            System.out.println("Failed to add to basket.\n");
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

            System.out.println("\nBasket Contents\n");
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
                System.out.printf("\nTotal Basket Amount: €%.2f\n\n", totalAmount);
            } else {
                System.out.println("Your basket is empty.\n");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve basket.\n");
            e.printStackTrace();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
