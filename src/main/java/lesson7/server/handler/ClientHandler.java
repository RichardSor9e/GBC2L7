package lesson7.server.handler;

import lesson7.server.MyServer;
import lesson7.server.authentication.AuthenticationService;
import lesson7.server.logger.ChatLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;

public class ClientHandler {
    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + password
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
    private static final String AUTHERR_CMD_PREFIX = "/autherr"; // + error message
    private static final String CHAT_HISTORY = "/ch"; // + msg
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
    private ChatHistory chatHistory = new ChatHistory();
    private static Logger thisLogger = MyServer.thisLogger;
    private static Handler LoggerHandler = MyServer.LoggerHandler;
    private static ReentrantReadWriteLock reentrantReadWriteLock = MyServer.reentrantReadWriteLock;


    public ClientHandler(MyServer myServer, Socket socket) throws IOException {

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
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                try {
                    myServer.unSubscribe(this);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                reentrantReadWriteLock.writeLock().lock();
                thisLogger.log(Level.INFO, DateFormat.getInstance() + "Client " + this.getUsername() + " has left");
                reentrantReadWriteLock.writeLock().unlock();
//                logger.sentLoggerToHistory("INFO", DateFormat.getInstance() + "Client " + this.getUsername() + " has left");

            }
        }).start();
    }

    private void authentication() throws IOException, SQLException {
        while (true) {
            String message = in.readUTF();
            if (message.startsWith(AUTH_CMD_PREFIX)) {
                boolean isSuccessAuth = processAuthentication(message);
                if (isSuccessAuth) {
                    break;
                }
            } else {

                reentrantReadWriteLock.writeLock().lock();
                thisLogger.log(Level.WARNING, this.username + "Неудачная попытка аутентификации");
                reentrantReadWriteLock.writeLock().unlock();
                out.writeUTF(AUTHERR_CMD_PREFIX + " Ошибка аутентификации");

            }
        }
    }



    private boolean processAuthentication(String message) throws IOException, SQLException {
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
            ChatHistory.sendFirst100Message(this);
            System.out.println("Пользователь " + username + " подключился к чату");

            return true;
        } else {
            out.writeUTF(AUTHERR_CMD_PREFIX + " Логин или пароль не соответствуют действительности");
            return false;
        }
    }

    private void readMessage() throws IOException {

        ChatHistory ch = new ChatHistory();
        ch.readMessageFromChatHistoryFile(this);
        while (true) {
            String message = in.readUTF();
            System.out.println("message from " + username + ": " + message);
            if (message.startsWith(STOP_SERVER_CMD_PREFIX)) {
                System.exit(0);
            } else if (message.startsWith(END_CLIENT_CMD_PREFIX)) {
                return;
            } else if (message.startsWith(PRIVATE_MSG_CMD_PREFIX)) {

String[] dividedMessage = message.split("\\s+", 3);

                myServer.sendPrivatMessage(dividedMessage[1], dividedMessage[2], this);

            } else

            {
               new Thread (() -> {
                   try {
                      chatHistory.addMessageToChatHistory(message, this);

                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }).start();

                myServer.broadcastMessage(message, this);
            }
        }
    }


    public void sendMessage(String sender, String message) throws IOException {
        out.writeUTF(String.format("%s %s %s", CLIENT_MSG_CMD_PREFIX, sender, message));

    }
    public void sendFirst100Message (ArrayList<String> a) throws IOException {

        for (int i = 0; i < a.toArray().length; i++) {
           out.writeUTF(CHAT_HISTORY + a.get(i));
            System.out.println(CHAT_HISTORY + a.get(i));

        }

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


