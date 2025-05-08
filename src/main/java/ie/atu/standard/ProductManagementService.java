package ie.atu.standard;

import java.util.Scanner;

public class ProductManagementService {

    public static void manageProducts(Scanner scanner) {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== Product Management ===\n");
            System.out.println("1. Add Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Back to Admin Dashboard");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> AddProductService.addProduct(scanner);
                case "2" -> EditProductService.editProduct(scanner);
                case "3" -> DeleteProductService.deleteProduct(scanner);
                case "4" -> managing = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
