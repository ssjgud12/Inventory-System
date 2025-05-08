package ie.atu.utils;

public class MenuUtils {

    public static void displayAdminMenu() {
        System.out.println("1. View Users");
        System.out.println("2. Manage Products");
        System.out.println("3. View Orders");
        System.out.println("4. Logout");
    }

    public static void displayManagerMenu() {
        System.out.println("1. View Stock Reports");
        System.out.println("2. View All Orders");
        System.out.println("3. Logout");
    }

    public static void displayCustomerMenu() {
        System.out.println("1. View and Purchase Products");
        System.out.println("2. View Basket");
        System.out.println("3. Logout");
    }

    public static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===\n");
    }

    public static void pause() {
        System.out.println("\nPress Enter to continue...");
        try {
            System.in.read();
        } catch (Exception ignored) {}
    }
}
