package ece651.riskgame.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Logger {
    private static Logger logger = new Logger();
    private PrintWriter writer;
    private ArrayList<String> buffer;

    private Logger() {
        try {
            String logFile = "log.txt";
            FileWriter fileWriter = new FileWriter(logFile);
            writer = new PrintWriter(fileWriter, true);
            buffer = new ArrayList<>();
        } catch (IOException ignored) {
        }
    }

    public static Logger getInstance() {
        return logger;
    }

    public void writeLog(String log) {
        buffer.add(log);
    }

    public Iterable<String> getBuffer() {
        return buffer;
    }

    public void flushBuffer() {
        for (String s : buffer) {
            writer.println(s);
        }
        buffer.clear();
    }

}
