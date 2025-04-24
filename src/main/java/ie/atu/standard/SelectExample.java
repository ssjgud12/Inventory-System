package ie.atu.standard;
<<<<<<< HEAD
import ie.atu.pool.DatabaseUtils;

=======
>>>>>>> a35b1978d60cf86a94e2db533f7e6a340a0229ff
import java.sql.*;
public class SelectExample {
    public static void main(String[] args) {
        // MySQL database connection details
        String url = "jdbc:mysql://localhost:3306/inventorysystem";
        String username = "root";
        String password = "password";

        // SQL statement
<<<<<<< HEAD
        String selectSQL = "SELECT u.username, u.email " +
                "FROM user u ";

        try (Connection connection = DatabaseUtils.getConnection();
=======
        String selectSQL = "SELECT u.username, u.Email  , u.Password"+
                "FROM user u " ;

        try (Connection connection = DriverManager.getConnection(url, username, password);
>>>>>>> a35b1978d60cf86a94e2db533f7e6a340a0229ff
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String email = resultSet.getString("email");
<<<<<<< HEAD

                System.out.println("Username: " + user  + ", Email: " + email);
=======
                String Password = resultSet.getString("password");

                System.out.println("Username: " + user + ", Password: " + ", Email: " + email);
>>>>>>> a35b1978d60cf86a94e2db533f7e6a340a0229ff
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}