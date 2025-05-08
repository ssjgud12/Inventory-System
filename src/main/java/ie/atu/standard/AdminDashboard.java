package ie.atu.standard;

import java.util.Scanner;

public class AdminDashboard {
    public static void show(Scanner scanner) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Admin Dashboard ===\n");
            System.out.println("1. View Users");
            System.out.println("2. Manage Products");
            System.out.println("3. View Orders");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> UserService.listUsers();
                case "2" -> ProductService.manageProducts(scanner);
                case "3" -> OrderService.listAllOrders();
                case "4" -> {
                    System.out.println("\nLogging out...");
                    loggedIn = false;
                }
                default -> System.out.println("\nInvalid option. Please try again.");
            }
        }
    }
}
