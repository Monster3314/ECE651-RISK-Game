package ece651.riskgame.server;

import ece651.riskgame.shared.Logger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
