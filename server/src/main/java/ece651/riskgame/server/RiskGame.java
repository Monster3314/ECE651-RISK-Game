package ece651.riskgame.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.GameInfo;

public class RiskGame {
  private ServerSocket serverSocket;
  private Map<Socket, Integer> sockets;

  private Board board;

  /**
   * Initialize territories based on given player number
   */
  private void initGameMap(int playerNum) {
    board = new Board();
    MapGenerator.apply(board, 1);
  }

  /**
   * Get the GameInfo of current round/initialization.
   */
  private GameInfo getCurrentGameInfo() {
    GameInfo gi = new GameInfo(board);
    return gi;
  }

  public void run() throws IOException{
    initGameMap(1);
    
    ServerSocket ss = new ServerSocket(8888);
    // only one player is allowed now
    Socket socket = serverSocket.accept();
    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
    GameInfo gi = getCurrentGameInfo();
    oos.writeObject(gi);

    while (!socket.isClosed()) {
    }

    ss.close();
  }    
}
