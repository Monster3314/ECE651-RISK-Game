package ece651.riskgame.server;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {
    private final Logger logger = Logger.getInstance();

    @Test
    void test_logger() {
        logger.writeLog("log1");
        logger.writeLog("log2");
        ArrayList<String> buffer = (ArrayList<String>) logger.getBuffer();
        logger.flushBuffer();
    }
}