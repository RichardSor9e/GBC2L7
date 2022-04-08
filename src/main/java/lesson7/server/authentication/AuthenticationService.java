package lesson7.server.authentication;

import java.sql.SQLException;

public interface AuthenticationService {
    String getUsernameByLoginAndPassword(String login, String password) throws SQLException;

    void startAuthentication();
    void endAuthentication();

    String getLoginByUsername(String username) throws SQLException;
}
