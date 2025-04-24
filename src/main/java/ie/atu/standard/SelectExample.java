package ie.atu.standard;
import ie.atu.pool.DatabaseUtils;

import java.sql.*;
public class SelectExample {
    public static void main(String[] args) {
        // MySQL database connection details
        String url = "jdbc:mysql://localhost:3306/inventorysystem";
        String username = "root";
        String password = "password";

        // SQL statement
        String selectSQL = "SELECT u.username, u.email " +
                "FROM user u ";

        try (Connection connection = DatabaseUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String email = resultSet.getString("email");

                System.out.println("Username: " + user  + ", Email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}