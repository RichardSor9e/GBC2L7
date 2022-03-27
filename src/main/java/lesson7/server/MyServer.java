package lesson7.server;

import lesson7.server.authentication.AuthenticationService;
import lesson7.server.authentication.BaseAuthenticationService;
import lesson7.server.handler.ClientHandler;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final List<ClientHandler> clients;
    private ArrayList <String> usersOnline;



    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        authenticationService = new BaseAuthenticationService();
        clients = new ArrayList<>();

    }
    public String getUsersOnline() {

        String listString = String.join(" ", usersOnline);
        return listString;
    }


    public void start() {
        System.out.println("СЕРВЕР ЗАПУЩЕН!");
        System.out.println("----------------");


        try {
            while(true) {
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void waitAndProcessNewClientConnection() throws IOException {
        System.out.println("Ожидание клиента...");
        Socket socket = serverSocket.accept();


        System.out.println("Клиент подключился!");

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
                System.out.println("ok");

                client.sendMessage(sender.getUsername(), message);

                System.out.println(sender.getUsername() + " sent a message to " + accepter);

            } }

    }
}
//ghbfd

