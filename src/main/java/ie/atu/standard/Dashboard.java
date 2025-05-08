// Common dashboard dispatcher
package ie.atu.standard;

import java.util.Scanner;

public class Dashboard {

    public static void show(Scanner scanner, String role) {
        switch (role.trim().toLowerCase()) {
            case "admin" -> AdminDashboard.show(scanner);
            case "manager" -> ManagerDashboard.show(scanner);
            default -> CustomerDashboard.show(scanner);
        }
    }
}


