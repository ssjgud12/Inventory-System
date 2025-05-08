package ie.atu.standard;

import java.util.Scanner;

public class CustomerDashboard {
    public static void show(Scanner scanner) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Customer Dashboard ===\n");
            System.out.println("1. View & Buy Products");
            System.out.println("2. View Basket");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> ProductService.viewAndBuyProducts(scanner);
                case "2" -> {
                    System.out.print("Enter your username to view basket: ");
                    String username = scanner.nextLine().trim();
                    BasketService.viewBasket(username);
                }
                case "3" -> {
                    System.out.println("\nLogging out...");
                    loggedIn = false;
                }
                default -> System.out.println("\nInvalid option. Please try again.");
            }
        }
    }
}
