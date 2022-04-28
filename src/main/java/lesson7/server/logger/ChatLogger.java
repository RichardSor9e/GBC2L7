package lesson7.server.logger;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;

public class ChatLogger {

    private static Logger chatLogger = Logger.getLogger("Запуск сервера");
    private static Handler LoggerHandler;
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    static {
        try {
            LoggerHandler = new FileHandler("src/main/resources/lib/loggerHistory.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ChatLogger() throws IOException {
        chatLogger.addHandler(LoggerHandler);
        LoggerHandler.setFormatter(new SimpleFormatter());

    }




    public static void sentLoggerToHistory (String a , String message, String method){


        LoggerHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format( String.valueOf(record.getLevel()), new Date(record.getMillis()), method, record.getMessage());
            }
        });
        reentrantReadWriteLock.writeLock().lock();
        if (a.equals("INFO")) {
            chatLogger.log(Level.INFO, message);
        }
        reentrantReadWriteLock.writeLock().unlock();
    }
}
