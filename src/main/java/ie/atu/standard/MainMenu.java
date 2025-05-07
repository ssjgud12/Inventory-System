package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    login(scanner);
                    break;
                case "2":
                    register(scanner);
                    break;
                case "3":
                    running = false;
                    System.out.println("\n👋 Thank you for using the Inventory System. Goodbye!");
                    break;
                default:
                    System.out.println("\n⚠️ Invalid selection. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Welcome to the Inventory System ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void login(Scanner scanner) {
        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String query = "SELECT Username, Email, Role FROM user WHERE Username = ? AND Password = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String user = rs.getString("Username");
                String email = rs.getString("Email");
                String role = rs.getString("Role");

                Account account = new Account(user, email);
                System.out.println("\n✅ Login successful!");
                System.out.printf("Welcome, %s (%s)\n", account.getUsername(), role);

                Dashboard.show(scanner, role);
            } else {
                System.out.println("\n❌ Login failed: Incorrect username or password.");
            }

        } catch (SQLException e) {
            System.out.println("\n🚨 Database error during login:");
            e.printStackTrace();
        }
    }

    private static void register(Scanner scanner) {
        System.out.println("\n=== Register ===");
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter your email address: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Create a password: ");
        String password = scanner.nextLine().trim();

        final String role = "Customer"; // Default role for all new registrations

        String query = "INSERT INTO user (Username, Email, Phone_Number, Role, Password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, role);
            stmt.setString(5, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("\n✅ Registration successful! You can now login.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("\n❌ Registration failed: Email or username already exists.");
        } catch (SQLException e) {
            System.out.println("\n🚨 Database error during registration:");
            e.printStackTrace();
        }
    }
}

// Updated by Daniel
