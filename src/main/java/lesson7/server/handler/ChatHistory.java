package lesson7.server.handler;

import lesson7.server.MyServer;
import lesson7.server.logger.ChatLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;


public  class  ChatHistory {

    private transient static File filePath = new File("src/main/resources/lib/СhatHistory.txt");
    private transient static int messageCounter = 0;
    private transient RandomAccessFile randomAccessFile;
    private transient static int counter = 0;
    private static ArrayList<String> messageClientList = new ArrayList<>();
    private static Logger thisLogger = MyServer.thisLogger;
    private static Handler LoggerHandler = MyServer.LoggerHandler;
    private static ReentrantReadWriteLock reentrantReadWriteLock = MyServer.reentrantReadWriteLock;

    public ChatHistory() throws IOException {

    }


    public ArrayList<String> addMessageToChatHistory(String message, ClientHandler client) throws IOException {


        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("[MM.dd hh:mm]");


        String preparedMessage = formatForDateNow.format(dateNow) + " " + client.getUsername() + ": " +
                message;


           PrintWriter pw = new PrintWriter(new FileOutputStream(filePath, true));


           if (counter < 100) {
               messageClientList.add(counter, preparedMessage);
           } else {
               messageClientList.remove(0);

               messageClientList.add(100, preparedMessage);
           }

        counter++;

        pw.println(preparedMessage);

        pw.close();
        reentrantReadWriteLock.writeLock().lock();
        thisLogger.log(Level.INFO, "Сообщение ушло в историю сообщений: " + preparedMessage);
        reentrantReadWriteLock.writeLock().unlock();
//        logger.sentLoggerToHistory("INFO", "Сообщение ушло в историю сообщений: " + preparedMessage);


return messageClientList;

//        randomAccessFile = new RandomAccessFile(filePath, "rw" );
//
//        randomAccessFile.seek(0);
//
//        randomAccessFile.writeBytes(a + " " + preparedMessage);
//        randomAccessFile.seek(0);
//        randomAccessFile.close();


//            pw.println( a + " " + preparedMessage);





//pw.close();



    }

    public void readMessageFromChatHistoryFile(ClientHandler client) throws FileNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;




        for (int i = 0; i < messageCounter; i++) {



        }
    }
    public static void sendFirst100Message(ClientHandler ch) throws IOException {
        ch.sendFirst100Message(messageClientList);
    }
}
