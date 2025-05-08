package ie.atu.standard;

import java.util.Scanner;

public class ManagerDashboard {
    public static void show(Scanner scanner) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Manager Dashboard ===\n");
            System.out.println("1. View Stock Reports");
            System.out.println("2. View Customer Orders");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> StockService.showStockReport();
                case "2" -> OrderService.listAllOrders();
                case "3" -> {
                    System.out.println("\nLogging out...");
                    loggedIn = false;
                }
                default -> System.out.println("\nInvalid option. Please try again.");
            }
        }
    }
}

