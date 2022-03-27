package lesson7.server.handler;

import lesson7.server.MyServer;
import lesson7.server.authentication.AuthenticationService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + password
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
    private static final String AUTHERR_CMD_PREFIX = "/autherr"; // + error message
    private static final String CLIENT_MSG_CMD_PREFIX = "/cMsg"; // + msg
    private static final String SERVER_MSG_CMD_PREFIX = "/sMsg"; // + msg
    private static final String PRIVATE_MSG_CMD_PREFIX = "/w"; // + msg
    private static final String STOP_SERVER_CMD_PREFIX = "/stop";
    private static final String END_CLIENT_CMD_PREFIX = "/end";
    private static final String REFRESH_CLIENT_LIST = "/ref";


    private MyServer myServer;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private String username;
    private DataOutputStream privatOut;
    private boolean online = false;




    public ClientHandler(MyServer myServer, Socket socket) {

        this.myServer = myServer;
        clientSocket = socket;
    }

    public boolean isOnline() {
        return online;
    }

    public void handle() throws IOException {
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());

        InetAddress address = InetAddress.getByName("LocalHost");
        System.out.println(address.getAddress().toString());
        System.out.println(address);



        new Thread(() -> {
            try {
                authentication();

                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    myServer.unSubscribe(this);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println(DateFormat.getInstance() + "Client " + this.getUsername() + " has left");
            }
        }).start();
    }

    private void authentication() throws IOException {
        while (true) {
            String message = in.readUTF();
            if (message.startsWith(AUTH_CMD_PREFIX)) {
                boolean isSuccessAuth = processAuthentication(message);
                if (isSuccessAuth) {
                    break;
                }
            } else {
                out.writeUTF(AUTHERR_CMD_PREFIX + " Ошибка аутентификации");
                System.out.println("Неудачная попытка аутентификации");
            }
        }
    }



    private boolean processAuthentication(String message) throws IOException {
        String[] parts = message.split("\\s+");
        if (parts.length != 3) {
            out.writeUTF(AUTHERR_CMD_PREFIX + " Ошибка аутентификации");
        }
        String login = parts[1];
        String password = parts[2];

        AuthenticationService auth = myServer.getAuthenticationService();

        username = auth.getUsernameByLoginAndPassword(login, password);

        if (username != null) {
            if (myServer.isUsernameBusy(username)) {
                out.writeUTF(AUTHERR_CMD_PREFIX + " Логин уже используется");
                return false;
            }

            out.writeUTF(AUTHOK_CMD_PREFIX + " " + username);
            myServer.subscribe(this);
            System.out.println("Пользователь " + username + " подключился к чату");
            return true;
        } else {
            out.writeUTF(AUTHERR_CMD_PREFIX + " Логин или пароль не соответствуют действительности");
            return false;
        }
    }

    private void readMessage() throws IOException {

        while (true) {
            String message = in.readUTF();
            System.out.println("message | " + username + ": " + message);
            if (message.startsWith(STOP_SERVER_CMD_PREFIX)) {
                System.exit(0);
            } else if (message.startsWith(END_CLIENT_CMD_PREFIX)) {
                return;
            } else if (message.startsWith(PRIVATE_MSG_CMD_PREFIX)) {

String[] dividedMessage = message.split("\\s+", 3);

                myServer.sendPrivatMessage(dividedMessage[1], dividedMessage[2], this);

            } else  {
                myServer.broadcastMessage(message, this);
            }
        }
    }


    public void sendMessage(String sender, String message) throws IOException {
        out.writeUTF(String.format("%s %s %s", CLIENT_MSG_CMD_PREFIX, sender, message));
    }

    public String getUsername() {
        return username;
    }


    public void refreshNameList (List<ClientHandler> a) throws IOException {

String msg = String.format("%s %s" ,REFRESH_CLIENT_LIST, a.toString());
        System.out.println(msg);
        out.writeUTF(msg);
    }

    public String toString () {
        return username;
    }
}


