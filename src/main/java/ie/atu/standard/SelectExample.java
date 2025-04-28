package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.*;

public class SelectExample {
    public static void main(String[] args) {
        // SQL statement
        String selectSQL = "SELECT u.username, u.email, u.password FROM user u";

        try (Connection connection = DatabaseUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                System.out.println("Username: " + user + ", Password: " + password + ", Email: " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
