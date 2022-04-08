package lesson7.server.authentication;

import java.sql.*;

public class DBAuthenticationService implements AuthenticationService{

    private static Connection connection;
    private static Statement stmt;

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) throws SQLException {

    ResultSet rs = stmt.executeQuery(String.format(
            "SELECT username from auth where login = '%s' and password = '%s'", login, password));

        String un = rs.getString("username");

        System.out.printf("User name: %7s", un );

        return un;
    }

    @Override
    public void startAuthentication() {

        try {
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void connection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection(
                "jdbc:sqlite:src/main/resources/lib/mainDB.db");

        stmt = connection.createStatement();
    }
    private void disconnect() throws SQLException {
        connection.close();

    }



    @Override
    public void endAuthentication() {

    }

    @Override
    public String getLoginByUsername(String username) throws SQLException {

        ResultSet rs = stmt.executeQuery(String.format(
                "SELECT login from auth where username = '%s'", username));

        String un = rs.getString("login");

        System.out.printf("Login is: %7s", un );

        return un;

    }
}
