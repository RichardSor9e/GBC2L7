package lesson7.server;

import lesson7.server.authentication.AuthenticationService;
import lesson7.server.authentication.DBAuthenticationService;
import lesson7.server.handler.ClientHandler;
import lesson7.server.logger.ChatLogger;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;

public class MyServer {

    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final List<ClientHandler> clients;
    private ArrayList <String> usersOnline;
    public static Logger thisLogger = Logger.getLogger("Запуск сервера");
    public static Handler LoggerHandler;
    public static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    static {
        try {
            LoggerHandler = new FileHandler("src/main/resources/lib/loggerHistory.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        authenticationService = new DBAuthenticationService();
        clients = new ArrayList<>();
        thisLogger.addHandler(LoggerHandler);
        LoggerHandler.setFormatter(new SimpleFormatter());


    }
    public String getUsersOnline() {

        String listString = String.join(" ", usersOnline);
        return listString;
    }


    public void start() {
 reentrantReadWriteLock.writeLock().lock();
        thisLogger.log(Level.INFO, "Сервер успешно запущен!");
        reentrantReadWriteLock.writeLock().unlock();
connectionWithDB();

        try {
            while(true) {
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectionWithDB() {
        DBAuthenticationService as = new DBAuthenticationService();
        try {
            as.connection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void waitAndProcessNewClientConnection() throws IOException {
//        logger.sentLoggerToHistory("INFO", "Ожидание клиента...");
        reentrantReadWriteLock.writeLock().lock();
        thisLogger.log(Level.INFO, "Ожидание клиента...");
        reentrantReadWriteLock.writeLock().unlock();
        connectionWithDB();
        Socket socket = serverSocket.accept();

        reentrantReadWriteLock.writeLock().lock();
        thisLogger.log(Level.INFO, "Клиент подключился.");
        reentrantReadWriteLock.writeLock().unlock();
//        logger.sentLoggerToHistory("INFO", "Клиент подключился.");
        connectionWithDB();

        processClientConnection(socket);
    }

    private void processClientConnection(Socket socket) throws IOException {
        ClientHandler handler = new ClientHandler(this, socket);
        handler.handle();
    }

    public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);



        for (ClientHandler a : clients) {

           a.refreshNameList(clients);
        }


    }

    public synchronized void unSubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);

      for (ClientHandler a : clients) {

           a.refreshNameList(clients);
        }
    }

    public synchronized boolean isUsernameBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage(sender.getUsername(), message);
        }
    }

    public synchronized void sendPrivatMessage (String accepter, String message, ClientHandler sender ) throws IOException {


        for (ClientHandler client : clients) {

            if (client.getUsername().equals(accepter)) {

                client.sendMessage(sender.getUsername(), message);
//
//                System.out.println(sender.getUsername() + " sent a message to " + accepter);
//                logger.sentLoggerToHistory("INFO", sender.getUsername() + " sent a message to " + accepter);
                reentrantReadWriteLock.writeLock().lock();
                thisLogger.log(Level.INFO, sender.getUsername() + " sent a message to " + accepter);
                reentrantReadWriteLock.writeLock().unlock();
                connectionWithDB();
            } }

    }
}
//ghbfd

