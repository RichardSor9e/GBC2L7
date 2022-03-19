package lesson7.server.authentication;

public interface AuthenticationService {
    String getUsernameByLoginAndPassword(String login, String password);

    void startAuthentication();
    void endAuthentication();

    String getLoginByUsername(String username);
}
