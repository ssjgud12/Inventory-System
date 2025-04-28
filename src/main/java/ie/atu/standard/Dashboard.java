package ie.atu.standard;

import java.util.Scanner;

public class Dashboard {

    public static void show(Scanner scanner, String role) {
        boolean loggedIn = true;
        role = role.trim().toLowerCase();

        while (loggedIn) {
            printMenu(role);

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleFirstOption(role);
                    break;
                case "2":
                    handleSecondOption(role);
                    break;
                case "3":
                    if (role.equals("admin")) {
                        viewOrders();
                    } else {
                        System.out.println("🔓 Logging out...");
                        loggedIn = false;
                    }
                    break;
                case "4":
                    if (role.equals("admin")) {
                        System.out.println("🔓 Logging out...");
                        loggedIn = false;
                    } else {
                        System.out.println("⚠️ Invalid option. Please try again.");
                    }
                    break;
                default:
                    System.out.println("⚠️ Invalid option. Please try again.");
            }
        }
    }

    private static void printMenu(String role) {
        System.out.println("\n=== " + capitalize(role) + " Dashboard ===");

        switch (role) {
            case "admin":
                System.out.println("1. View Users");
                System.out.println("2. Manage Products");
                System.out.println("3. View Orders");
                System.out.println("4. Logout");
                break;
            case "manager":
                System.out.println("1. View Stock Reports");
                System.out.println("2. View Orders");
                System.out.println("3. Logout");
                break;
            default: // Customer, Staff, etc.
                System.out.println("1. View Products");
                System.out.println("2. View My Orders");
                System.out.println("3. Logout");
        }
    }

    private static void handleFirstOption(String role) {
        switch (role) {
            case "admin":
                viewUsers();
                break;
            case "manager":
                viewStockReports();
                break;
            default:
                viewProducts();
        }
    }

    private static void handleSecondOption(String role) {
        switch (role) {
            case "admin":
                manageProducts();
                break;
            case "manager":
                viewOrders();
                break;
            default:
                viewMyOrders();
        }
    }

    // user actions
    private static void viewUsers() {
        System.out.println("👤 Viewing all users (Admin only).");
    }

    private static void manageProducts() {
        System.out.println("🛠 Managing products (Admin only).");
    }

    private static void viewOrders() {
        System.out.println("📦 Viewing all orders.");
    }

    private static void viewStockReports() {
        System.out.println("📈 Viewing stock reports (Manager only).");
    }

    private static void viewProducts() {
        System.out.println("🛒 Viewing available products.");
    }

    private static void viewMyOrders() {
        System.out.println("📃 Viewing your orders.");
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
