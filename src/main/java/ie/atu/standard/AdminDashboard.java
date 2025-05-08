package ie.atu.standard;

import java.util.Scanner;

public class AdminDashboard {

    public static void show(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Admin Dashboard ===\n");
            System.out.println("1. View Users");
            System.out.println("2. Manage Products");
            System.out.println("3. View Orders");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    ViewUsers.displayAllUsers();
                    break;
                case "2":
                    ProductManagementService.manageProducts(scanner);
                    break;
                case "3":
                    OrderService.viewOrders();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


}
