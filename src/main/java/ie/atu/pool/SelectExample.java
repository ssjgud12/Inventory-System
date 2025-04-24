package ie.atu.pool;

import java.sql.*;

public class SelectExample {
    public static void main(String[] args) {
        String selectSQL = "SELECT u.username, u.email " +
                "FROM user u " ;

        try (Connection connection = DatabaseUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String email = resultSet.getString("email");

                System.out.println("Username: " + user + ", Password: " + ", Email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}