package ie.atu.standard;
import java.sql.*;
public class SelectExample {
    public static void main(String[] args) {
        // MySQL database connection details
        String url = "jdbc:mysql://localhost:3306/inventorysystem";
        String username = "root";
        String password = "password";

        // SQL statement
        String selectSQL = "SELECT u.username, u.Email  , u.Password"+
                "FROM user u " ;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String email = resultSet.getString("email");
                String Password = resultSet.getString("password");

                System.out.println("Username: " + user + ", Password: " + ", Email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}