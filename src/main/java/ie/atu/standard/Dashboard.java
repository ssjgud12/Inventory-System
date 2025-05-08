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
                    handleFirstOption(scanner, role);
                    break;
                case "2":
                    handleSecondOption(scanner, role);
                    break;
                case "3":
                    if (role.equals("manager")) {
                        OrderService.listAllOrders();
                    } else {
                        System.out.println("\nLogging out...");
                        loggedIn = false;
                    }
                    break;
                case "4":
                    System.out.println("\nLogging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    private static void printMenu(String role) {
        System.out.println("\n=== " + capitalize(role) + " Dashboard ===\n");
        switch (role) {
            case "admin" -> System.out.println("1. View Users\n2. Manage Products\n3. View Orders\n4. Logout");
            case "manager" -> System.out.println("1. View Stock Reports\n2. View Orders\n3. View Customer Orders\n4. Logout");
            default -> System.out.println("1. View & Buy Products\n2. View Basket\n3. Logout");
        }
    }

    private static void handleFirstOption(Scanner scanner, String role) {
        switch (role) {
            case "admin" -> UserService.listUsers();
            case "manager" -> StockService.showStockReport();
            default -> ProductService.viewAndBuyProducts(scanner);
        }
    }

    private static void handleSecondOption(Scanner scanner, String role) {
        switch (role) {
            case "admin" -> ProductService.manageProducts(scanner);
            case "manager" -> OrderService.listAllOrders();
            default -> {
                System.out.print("Enter your username to view basket: ");
                String username = scanner.nextLine().trim();
                BasketService.viewBasket(username);
            }
        }
    }

    private static String capitalize(String str) {
        return str == null || str.isEmpty()
                ? str
                : str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}


